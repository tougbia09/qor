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
import com.bangma.qor.math.Position;
import com.bangma.qor.math.WallVector;
import com.bangma.qor.objects.Button;
import com.bangma.qor.objects.Player;
import com.bangma.qor.objects.RobotPlayer;
import com.bangma.qor.objects.Scene;
import com.bangma.qor.objects.Square;
import com.bangma.qor.objects.Wall;
import com.bangma.qor.utility.FileManager;
import com.bangma.qor.utility.StateManager;

import testable.GraphUpdater;

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
    private State gameMode;					// One Player or Two Player Indicator
    private Texture background;				// the background image to display. (the game board)
    private SpriteBatch batch;				// the sprite batch for drawing images.
    private List<Button> buttons;			// a list of all buttons on screen.
    private List<Sprite> placedWalls;		// a list of walls that have been placed.
    private Map<Integer, Square> squares;	// a list of all the gameboard squares on screen.
    private Map<String, Wall> walls;		// a list of all the walls on the board.
    private Graph graph;					// the mathematical representation of the board.
    private GraphUpdater graphUpdater;		// tool for interacting with the graph.
    private boolean turn = true; 			// turn toggle; true for player one, false for player two.
    private Square hoveredSquare;			// placeholder for a square that is being hovered over.
    private Wall hoveredWall;				// placeholder for a wall that is being hovered over.
    private Texture playerOneWall;			// the image for player one's walls.
    private Texture playerTwoWall;			// the image for player two's walls.
    private BitmapFont fontOne;				// font for player one's wall counter.
    private BitmapFont fontTwo;				// font for player two's wall counter.
    private WallVector wallToPlace;			// verified position for a wall to go.
    private Player currentPlayer;			// shortcut to whichever player is active.
    private Player playerOne;				// the first player.
    private RobotPlayer playerTwo;			// the second player.
    
    /**
     * Create an instance of GameBoard.
     * @param gameMode State; either one player or two player
     */
    public GameBoard(State gameMode) {
        buttons 		= new ArrayList<>(2);
        squares 		= new HashMap<>(81); // 9 x 9 board
        walls			= new HashMap<>();
        batch 			= new SpriteBatch();
        placedWalls 	= new ArrayList<>(20); // 10 walls x 2 players
        fontOne			= new BitmapFont();
        fontTwo			= new BitmapFont();
        this.gameMode 	= gameMode;
        hoveredSquare 	= null;
        
        fontOne.getData().setScale(8f, 8f); // there has to be a better way :(
        fontTwo.getData().setScale(8f, 8f);
        
        buttons.add(new Button(
                FileManager.getTexture("reset.png"),
                this.gameMode, // what happens when you click this button.
                Constant.SCREEN_SIZE - Constant.MINI_BUTTON_WIDTH - 10,
                15
        ));
        buttons.add(new Button(
                FileManager.getTexture("exit.png"),
                // send the player to an exit confirmation, that returns you to the game or sends you to the menu.
                new ConfirmExit(this.gameMode, State.MENU_SCREEN),
                10,
                15
        ));
        
        background = FileManager.getTexture("gameboard.png");
        playerOneWall = FileManager.getTexture("placed wall player one.png");
        playerTwoWall = FileManager.getTexture("placed wall player two.png");
        
        playerOne = new Player(FileManager.getTexture("playerOne.png"), 4, 0);
        playerTwo = new RobotPlayer(FileManager.getTexture("playerTwo.png"), 4, 8, playerOne);
        wallToPlace = null;
        setupGameBoard();
    }
    
    public void onePlayerTurn(boolean wallPlaced, boolean charMoved) {
    	// confirm that the mouse has been clicked over a square that isnt either players position.
        if (turn && Gdx.input.justTouched()) {
        	if (hoveredSquare != null) {
        		playerOne.move(hoveredSquare.getGridPosition());
        		charMoved = true;
        	}
            if (hoveredWall != null) {
            	wallPlaced = placeNewWall();
            }
            if (wallPlaced || charMoved) {
            	changeTurn();
            }
        }
        if (!turn) {
        	playerTwo.update();
        	playerTwo.makeOwnMove();
        	changeTurn();
        }
    }
    public void twoPlayerTurn(boolean wallPlaced, boolean charMoved) {
    	// confirm that the mouse has been clicked over a square that isnt either players position.
        if (Gdx.input.justTouched()) {
        	if (hoveredSquare != null) {

        		// move the player, who's turn it currently is.
        		if (turn) playerOne.move(hoveredSquare.getGridPosition());
        		else playerTwo.move(hoveredSquare.getGridPosition());
        		
        		charMoved = true;
        	}
            if (hoveredWall != null) {
            	wallPlaced = placeNewWall();
            }
            if (wallPlaced || charMoved) {
            	changeTurn();
            }
        }
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
        
        if (this.gameMode == State.GAME_1P) { 
        	// only allow mouse input to have an effect during the players turn.
            onePlayerTurn(wallPlaced, charMoved);

        } else { 
        	// Allow input to control both players in two player mode.
            twoPlayerTurn(wallPlaced, charMoved);
        }

        if (graph.convertTupleToId(playerOne.getGridPosition()) >= 72) {
            StateManager.setCurrentScene(State.PLAYER_ONE_WIN);
        }
        if (graph.convertTupleToId(playerTwo.getGridPosition()) < 9) {
            StateManager.setCurrentScene(State.PLAYER_TWO_WIN);
        }
        if (playerOne.getRemainingWalls() == 0)  fontOne.setColor(Color.RED);
        if (playerTwo.getRemainingWalls() == 0)  fontTwo.setColor(Color.RED);
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
        
        // choose which player to draw a guide for.
        for (int node : graph.getNeighbors(graph.convertTupleToId(currentPlayer.getGridPosition()))) {
        	Square square = squares.get(node);
        	square.setAlpha(0.7f);
        	square.draw(batch);
        }
        if (hoveredSquare != null && hoveredWall == null) 	hoveredSquare.draw(batch);
        if (hoveredWall != null) 	hoveredWall.draw(batch);
        for (Sprite wall : placedWalls) wall.draw(batch);
        playerOne.draw(batch);
        playerTwo.draw(batch);
        fontOne.draw(batch, 
        		String.format("%02d", playerOne.getRemainingWalls()),
        		215f, Constant.SCREEN_SIZE - 30f);
        fontTwo.draw(batch, 
        		String.format("%02d", playerTwo.getRemainingWalls()),
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
        graphUpdater = new GraphUpdater(graph);
        currentPlayer = playerOne;
        if (this.gameMode == State.GAME_1P) {
        	playerTwo.setGraph(graph);
        }
        
        Texture squareTexture 	= FileManager.getTexture("hover.png");
        Texture wallTexture   	= FileManager.getTexture("wall.png");
        
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int id = graph.convertTupleToId(new Position(i, j));
                // create invisible layer of squares, that will become visible when the player hovers over them.
                squares.put(id, new Square( squareTexture, i, j,
                        Constant.GRID_OFFSET_X + i*Constant.SQUARE_SIZE + 9,
                        Constant.GRID_OFFSET_Y + j*Constant.SQUARE_SIZE + 8
                ));
                if (i > 0) {
                	// create invisible layer of vertical walls.
	                walls.put(id + " v", new Wall(wallTexture, i, j, 
	            		Constant.GRID_OFFSET_X + i*Constant.SQUARE_SIZE + 1,
	            		Constant.GRID_OFFSET_Y + j*Constant.SQUARE_SIZE + 8, 'v'
	        		));
                }
                if(j < 8) {
                	// create invisible layer of horizontal walls.
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
    	wallToPlace = null;
        Square hovered = null;
        for (Square s : squares.values()) {
            if (s.getBoundingRectangle().contains(mousePosition) && // mouse is over this square.
            		
            		// make sure that the hovered square is not either player's current position.
                    !s.getGridPosition().equals( playerTwo.getGridPosition() ) &&
                    !s.getGridPosition().equals( playerOne.getGridPosition() ) && 
                    
                    // make sure that the player can move to this position.
                    graph.neighborExists(
                		graph.convertTupleToId( currentPlayer.getGridPosition() ), 
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
    	if (currentPlayer.getRemainingWalls() == 0) return null;
    	Wall hovered = null;
    	for (Wall w : walls.values()) {
	    	if (w.getBoundingRectangle().contains(mousePosition)) {
	    		WallVector possibleWall = WallVector.createWallVector(w.getGridPosition(), w.orientation, graph);
	    		if (graphUpdater.wallAllowed(possibleWall)) {
	    			w.setAlpha(0.7f);
		            hovered = w;
		            wallToPlace = possibleWall;
		            break;
	    		}
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
    	if (currentPlayer.getRemainingWalls() == 0) {return false;}
    	Sprite newWall;
    	if (turn) newWall = new Sprite(playerOneWall);
    	else newWall = new Sprite(playerTwoWall);
    	Position pos = hoveredWall.getGridPosition();
    	
    	if (hoveredWall.orientation == 'h') {
    		if (pos.x == 8) {
        		newWall.setX(hoveredWall.getX() + 1 - Constant.SQUARE_SIZE / 2f);
            	newWall.setY(hoveredWall.getY() - Constant.SQUARE_SIZE / 2f);
        	} else {
        		newWall.setX(hoveredWall.getX() + 1 + Constant.SQUARE_SIZE / 2f);
            	newWall.setY(hoveredWall.getY() - Constant.SQUARE_SIZE / 2f);
        	}
    		newWall.rotate(90);
    	}
    	if (hoveredWall.orientation == 'v') {
        	if (pos.y == 8) {
        		newWall.setX(hoveredWall.getX());
            	newWall.setY(hoveredWall.getY() - Constant.SQUARE_SIZE);
        	} else {
        		newWall.setX(hoveredWall.getX());
            	newWall.setY(hoveredWall.getY());
        	}
    	}
    	
    	placedWalls.add(newWall); // place visual wall
    	graphUpdater.placeWall(wallToPlace); 
    	currentPlayer.decrementWalls();
    	return true;
    }
    
    /**
     * Changes the current player, and toggles the turn boolean.
     */
    private void changeTurn() {
    	turn = !turn;
    	currentPlayer = (turn) ? playerOne : playerTwo;
    }
    
    /**
     * Open the rules of the game on wikipedia.
     */
    private static void openHelp() {
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
