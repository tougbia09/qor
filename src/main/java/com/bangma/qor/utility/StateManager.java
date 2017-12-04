package com.bangma.qor.utility;

import com.bangma.qor.objects.Scene;
import com.bangma.qor.scenes.ConfirmExit;
import com.bangma.qor.scenes.GameBoard;
import com.bangma.qor.scenes.GameModeSelect;
import com.bangma.qor.scenes.MainMenu;
import com.bangma.qor.scenes.WinScreen;

import java.util.EnumMap;

public class StateManager {
    public enum State {
        MENU_SCREEN,        // the game's main menu
        STATS_SCREEN,       // game stats screen, (games played, wins, losses, etc.)
        QUIT_GAME,          // confirm game exit screen
        GAME_1P,            // single player game board
        GAME_2P,            // two player game board
        PLAYER_ONE_WIN,     // player one has won the game
        PLAYER_TWO_WIN,     // player two has won the game
        MODE_SELECT,        // choose one player or two player mode
        KILL_GAME           // flag the main loop to kill the application
    }
    private static Scene currentScene;
    private static EnumMap<State, Scene> scenes;

    public static void init(State startingState) {
        scenes = new EnumMap<>(State.class);

        scenes.put(State.MENU_SCREEN, new MainMenu());
        scenes.put(State.MODE_SELECT, new GameModeSelect());
        scenes.put(State.GAME_1P, new GameBoard(State.GAME_1P));
        scenes.put(State.GAME_2P, new GameBoard(State.GAME_2P));
        scenes.put(State.PLAYER_ONE_WIN, new WinScreen(true));
        scenes.put(State.PLAYER_TWO_WIN, new WinScreen(false));
        scenes.put(State.STATS_SCREEN, new MainMenu());
        scenes.put(State.QUIT_GAME, new ConfirmExit(State.MENU_SCREEN, State.KILL_GAME));

        currentScene = scenes.get(startingState);
    }

    public static void setCurrentScene(Scene scene) {
        currentScene = scene;
    }

    public static Scene getCurrentScene() {
        return currentScene;
    }

    public static void setCurrentScene(State state) {
        currentScene = scenes.get(state);
    }

    public static void dispose() {
        for (Scene scene : scenes.values()) {
            scene.dispose();
        }
    }
    public static void resetGameState() {
    	scenes.put(State.GAME_1P, new GameBoard(State.GAME_1P));
    	scenes.put(State.GAME_2P, new GameBoard(State.GAME_2P));
    }
}
