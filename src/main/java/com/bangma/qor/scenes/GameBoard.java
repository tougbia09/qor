package com.bangma.qor.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.bangma.qor.config.Constant;
import com.bangma.qor.math.Graph;
import com.bangma.qor.math.Tuple;
import com.bangma.qor.objects.Button;
import com.bangma.qor.objects.Player;
import com.bangma.qor.objects.Scene;
import com.bangma.qor.objects.Square;
import com.bangma.qor.utility.FileManager;
import com.bangma.qor.utility.StateManager;

import java.util.*;

import static com.bangma.qor.utility.StateManager.State;

public class GameBoard implements Scene {
    private State gameMode;
    private Texture background;
    private SpriteBatch batch;
    private List<Button> buttons;
    private Map<Integer,Square> squares;
    private Graph graph;
    private Map<Boolean, Player> players;
    private boolean turn = true; // true for player one, false for player two.
    private Square hoveredSquare;

    public GameBoard(State gameMode) {
    	hoveredSquare = null;
        buttons = new ArrayList<>();
        squares = new HashMap<>();
        batch = new SpriteBatch();
        this.gameMode = gameMode;
        players = new HashMap<>();
        players.put(true , new Player(FileManager.getTexture("playerOne.png"), 4, 0));
        players.put(false, new Player(FileManager.getTexture("playerTwo.png"), 4, 8));

        background = FileManager.getTexture("gameboard.png");

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

        setupGameBoard();
    }

    public void update(Vector2 mousePosition) {
        input();
        Button.buttonClick(buttons, mousePosition);
        hoveredSquare = mouseHover(squares, mousePosition);
        // confirm that the mouse has been clicked over a square that isnt either players position.

        if (Gdx.input.justTouched() && hoveredSquare != null) {
            players.get(turn).move(hoveredSquare.getGridPosition());

            turn = !turn;
        }

        if (graph.convertTupleToId(players.get(true).getGridPosition()) >= 72) {
            StateManager.setCurrentScene(State.MENU_SCREEN); // player one win
        }
        if (graph.convertTupleToId(players.get(false).getGridPosition()) < 9) {
            StateManager.setCurrentScene(State.MENU_SCREEN); // player two win
        }
    }

    public void input() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            StateManager.setCurrentScene(new ConfirmExit(this.gameMode, State.MENU_SCREEN));
        }
    }

    public void draw() {
        batch.begin();
        batch.draw(background, 0, 0);
        Button.drawButtons(buttons, batch);
        Square.drawSquares(squares, batch);
        players.get(true).draw(batch);
        players.get(false).draw(batch);
        batch.end();
    }

    public void dispose() {
    	for (Button b : buttons) {
    		b.getTexture().dispose();
    	}
    	
    }

    /**
     * Set up the all the things necessary for the game board.
     * Create the graph to use. and create the hoverable squares.
     */
    private void setupGameBoard() {
        graph = new Graph(9,9);

        Texture squareTexture = FileManager.getTexture("hover.png");
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Tuple<Integer> t = new Tuple<>(i, j);
                squares.put(graph.convertTupleToId(t), new Square( squareTexture, i, j,
                        Constant.GRID_OFFSET_X + (i*Constant.SQUARE_SIZE + 9),
                        Constant.GRID_OFFSET_Y + (j*Constant.SQUARE_SIZE + 8)
                ));
            }
        }
    }

    public Square mouseHover(Map<Integer, Square> squares, Vector2 mousePosition) {
        Square hovered = null;
        for (Square s : squares.values()) {
            if (s.getBoundingRectangle().contains(mousePosition) &&
                    !s.getGridPosition().equals(players.get(false).getGridPosition()) &&
                    !s.getGridPosition().equals(players.get(true).getGridPosition()) &&
                    graph.areNeighbors(players.get(turn).getGridPosition(), s.getGridPosition())) {
                s.setAlpha(0.7f);
                hovered = s;
            } else {
                s.setAlpha(0);
            }
        }
        return hovered;
    }
}
