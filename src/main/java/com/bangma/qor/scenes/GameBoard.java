package com.bangma.qor.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.bangma.qor.config.Constant;
import com.bangma.qor.math.Graph;
import com.bangma.qor.math.Tuple;
import com.bangma.qor.objects.Button;
import com.bangma.qor.objects.Player;
import com.bangma.qor.objects.Scene;
import com.bangma.qor.objects.Square;
import com.bangma.qor.objects.Wall;
import com.bangma.qor.utility.FileManager;
import com.bangma.qor.utility.StateManager;
import java.awt.Desktop;
import java.net.URI;
import java.net.URL;
import java.util.*;

import static com.bangma.qor.utility.StateManager.State;

/**
 * The gameplay scene of the application. This creates and displays
 * the objects necessary for the game to be played.
 * 
 * @author tim bangma
 */
public class GameBoard implements Scene {
    private State gameMode;
    private Texture background;
    private SpriteBatch batch;
    private List<Button> buttons;
    private List<Sprite> placedWalls;
    private Map<Integer, Square> squares;
    private Map<String, Wall> walls;
    private Graph graph;
    private Map<Boolean, Player> players;
    private boolean turn = true; // true for player one, false for player two.
    private Square hoveredSquare;
    private Wall hoveredWall;
    private Texture playerOneWall;
    private Texture playerTwoWall;
    private BitmapFont fontOne;
    private BitmapFont fontTwo;
    /**
     * Create an instance of GameBoard.
     * @param gameMode State; either one player or two player
     */
    public GameBoard(State gameMode) {
        buttons 		= new ArrayList<>(2);
        squares 		= new HashMap<>(81);
        walls			= new HashMap<>();
        batch 			= new SpriteBatch();
        players 		= new HashMap<>(2);
        placedWalls 	= new ArrayList<>(20);
        fontOne			= new BitmapFont();
        fontTwo			= new BitmapFont();
        this.gameMode 	= gameMode;
        hoveredSquare 	= null;
        
        fontOne.getData().setScale(8f, 8f);
        fontTwo.getData().setScale(8f, 8f);
        
        buttons.add(new Button(
                FileManager.getTexture("reset.png"),
                this.gameMode,
                Constant.SCREEN_SIZE - Constant.MINI_BUTTON_WIDTH - 10,
                15
        ));
        buttons.add(new Button(
                FileManager.getTexture("exit.png"),
                new ConfirmExit(this.gameMode, State.MENU_SCREEN),
                10,
                15
        ));
        
        background = FileManager.getTexture("gameboard.png");
        playerOneWall = FileManager.getTexture("placed wall player one.png");
        playerTwoWall = FileManager.getTexture("placed wall player two.png");
        players.put(true , new Player(FileManager.getTexture("playerOne.png"), 4, 0));
        players.put(false, new Player(FileManager.getTexture("playerTwo.png"), 4, 8));
        
        setupGameBoard();
    }
    /**
     * update the game world / objects in the game world each cycle (tick).
     */
    public void update(Vector2 mousePosition) {
        input();
        Button.buttonClick(buttons, mousePosition);
        hoveredSquare 		= squareHover(squares, mousePosition);
        hoveredWall			= wallHover(walls, mousePosition);
        boolean wallPlaced 	= false;
        boolean charMoved	= false;
        
        
        // confirm that the mouse has been clicked over a square that isnt either players position.
        if (Gdx.input.justTouched()) {
        	if (hoveredSquare != null) {
        		players.get(turn).move(hoveredSquare.getGridPosition());
        		charMoved = true;
        	}
            if (hoveredWall != null) {
            	wallPlaced = placeNewWall();
            }
            if (wallPlaced || charMoved) {
            	turn = !turn;
            }
        }

        if (graph.convertTupleToId(players.get(true).getGridPosition()) >= 72) {
            StateManager.setCurrentScene(State.PLAYER_ONE_WIN);
        }
        if (graph.convertTupleToId(players.get(false).getGridPosition()) < 9) {
            StateManager.setCurrentScene(State.PLAYER_TWO_WIN);
        }
        if (players.get(true).getRemainingWalls() == 0)  fontOne.setColor(Color.RED);
        if (players.get(false).getRemainingWalls() == 0)  fontTwo.setColor(Color.RED);
    }

    /**
     * Accept user keyboard input.
     */
    public void input() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            StateManager.setCurrentScene(new ConfirmExit(this.gameMode, State.MENU_SCREEN));
        }
    }
    
    /**
     * Draw all scene objects to the screen.
     */
    public void draw() {
        batch.begin();
        batch.draw(background, 0, 0);
        Button.drawButtons(buttons, batch);
        for (int node : graph.getNeighbors(graph.convertTupleToId(players.get(turn).getGridPosition()))) {
        	Square square = squares.get(node);
        	square.setAlpha(0.7f);
        	square.draw(batch);
        }
        if (hoveredSquare != null && hoveredWall == null) 	hoveredSquare.draw(batch);
        if (hoveredWall != null) 	hoveredWall.draw(batch);
        for (Sprite wall : placedWalls) wall.draw(batch);
        players.get(true).draw(batch);
        players.get(false).draw(batch);
        fontOne.draw(batch, 
        		String.format("%02d", players.get(true).getRemainingWalls()),
        		215f, Constant.SCREEN_SIZE - 30f);
        fontTwo.draw(batch, 
        		String.format("%02d", players.get(false).getRemainingWalls()),
        		Constant.SCREEN_SIZE - 240f, Constant.SCREEN_SIZE - 30f);
        batch.end();
    }

    /**
     * Discard all textures used by the application, so they do not stay in
     * memory. I am fairly certain this is for the android build of libgdx
     * only.
     */
    public void dispose() {
    	for (Button b : buttons) {
    		b.getTexture().dispose();
    	}
    	playerOneWall.dispose();
    	playerTwoWall.dispose();
    }

    /**
     * Create and place all of the objects necessary for the visual game board. 
     * Create the graph to use. and create the hoverable squares. Also creates 
     * the single-width wall placeholders.
     */
    private void setupGameBoard() {
        graph = new Graph(9,9);

        Texture squareTexture 	= FileManager.getTexture("hover.png");
        Texture wallTexture   	= FileManager.getTexture("wall.png");
        
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int id = graph.convertTupleToId(new Tuple<>(i, j));
                squares.put(id, new Square( squareTexture, i, j,
                        Constant.GRID_OFFSET_X + i*Constant.SQUARE_SIZE + 9,
                        Constant.GRID_OFFSET_Y + j*Constant.SQUARE_SIZE + 8
                ));
                if (i > 0) {
	                walls.put(id + " v", new Wall(wallTexture, i, j, 
	            		Constant.GRID_OFFSET_X + i*Constant.SQUARE_SIZE + 1,
	            		Constant.GRID_OFFSET_Y + j*Constant.SQUARE_SIZE + 8, 'v'
	        		));
                }
                if(j < 8) {
                	Wall wall = new Wall(wallTexture, i, j, 
	                		Constant.GRID_OFFSET_X + i*Constant.SQUARE_SIZE + 1 + Constant.SQUARE_SIZE / 2, 
	                		Constant.GRID_OFFSET_Y + j*Constant.SQUARE_SIZE + 8 + Constant.SQUARE_SIZE / 2, 'h');
	                wall.rotate(90f);
	                walls.put(id + " h", wall);
                }
            }
        }
    }

    /**
     * This function checks each square given as squares, to see if the vector 
     * mousePosition is inside of it. This function is facilitated by the rectangle 
     * collision provided by the libgdx classes; Sprite, and Rectangle.
     * @param squares the squares to check against.
     * @param mousePosition the mouse position in pixels.
     * @return the square being hovered over or null.
     */
    public Square squareHover(Map<Integer, Square> squares, Vector2 mousePosition) {
        Square hovered = null;
        for (Square s : squares.values()) {
            if (s.getBoundingRectangle().contains(mousePosition) &&
                    !s.getGridPosition().equals(players.get(false).getGridPosition()) &&
                    !s.getGridPosition().equals(players.get(true).getGridPosition()) &&
                    graph.neighborExists(
                		graph.convertTupleToId( players.get(turn).getGridPosition() ), 
                		graph.convertTupleToId( s.getGridPosition() )
            		)) {
                s.setAlpha(0.7f);
                hovered = s;
                break;
            } else {
                s.setAlpha(0);
            }
        }
        return hovered;
    }
    
    /**
     * This function checks each wall given as walls, to see if the vector mousePosition
     * is inside of it. This function is facilitated by the rectangle collision provided
     * by the libgdx classes; Sprite, and Rectangle.
     * 
     * @param walls the list of walls to check against.
     * @param mousePosition the position of the mouse in pixels.
     * @return the wall that is being hovered, or null.
     */
    public Wall wallHover(Map<String, Wall> walls, Vector2 mousePosition) {
    	if (players.get(turn).getRemainingWalls() == 0) return null;
    	Wall hovered = null;
    	for (Wall w : walls.values()) {
    		Tuple<Integer> pos = w.getGridPosition();
    		int id = graph.convertTupleToId(pos);
    		
	    	if (w.getBoundingRectangle().contains(mousePosition)) {
	            w.setAlpha(0.7f);
	            hovered = w;
	            break;
	    	} else {
                w.setAlpha(0);
            }
        }
    	return hovered;
    }
    
    /**
     * When called, this function places a wall where the coordinates of hoveredWall are.
     * It will always try to place the two-length wall either above the mouse (if the wall 
     * is vertical) or to the right of the mouse (if the wall is horizontal). The only 
     * exception is when you click one of the edges on the top row or the far right column, 
     * the wall will be placed so that it stays in bounds.
     */
    public boolean placeNewWall() {
    	if (players.get(turn).getRemainingWalls() == 0) {return false;}
    	Sprite newWall;
    	if (turn) newWall = new Sprite(playerOneWall);
    	else newWall = new Sprite(playerTwoWall);
    	Tuple<Integer> pos = hoveredWall.getGridPosition();
    	int id = graph.convertTupleToId(hoveredWall.getGridPosition());
    	
    	if (hoveredWall.orientation == 'h') {
    		if (pos.x() == 8) {
        		newWall.setX(hoveredWall.getX() + 1 - Constant.SQUARE_SIZE / 2f);
            	newWall.setY(hoveredWall.getY() - Constant.SQUARE_SIZE / 2f);
            	graph.removeNeighbor(id-1, id-1 + 9);
        	} else {
        		newWall.setX(hoveredWall.getX() + 1 + Constant.SQUARE_SIZE / 2f);
            	newWall.setY(hoveredWall.getY() - Constant.SQUARE_SIZE / 2f);
            	graph.removeNeighbor(id+1, id+1 + 9);
        	}
    		newWall.rotate(90);
    		graph.removeNeighbor(id, id + 9);
    	}
    	if (hoveredWall.orientation == 'v') {
        	if (pos.y() == 8) {
        		newWall.setX(hoveredWall.getX());
            	newWall.setY(hoveredWall.getY() - Constant.SQUARE_SIZE);
        	} else {
        		newWall.setX(hoveredWall.getX());
            	newWall.setY(hoveredWall.getY());
        	}
        	graph.removeNeighbor(id, id - 1);
    	}
    	
    	placedWalls.add(newWall);
    	players.get(turn).decrementWalls();
    	return true;
    }
    
    /**
     * Open the rules of the game on wikipedia.
     */
    public static void openHelp() {
    	URI uri;
    	try {
    		uri = new URL("https://en.wikipedia.org/wiki/Quoridor#Rules_of_the_game").toURI();
    		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
            if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                desktop.browse(uri);
            }
        } catch (Exception e) {
            return;
        }
    }
}
