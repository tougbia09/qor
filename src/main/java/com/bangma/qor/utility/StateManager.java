package com.bangma.qor.utility;

import com.bangma.qor.objects.Scene;
import com.bangma.qor.scenes.ConfirmExit;
import com.bangma.qor.scenes.GameBoard;
import com.bangma.qor.scenes.GameModeSelect;
import com.bangma.qor.scenes.MainMenu;

import java.util.HashMap;
import java.util.Map;

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

    private static Scene CurrentScene;
    private static Map<State, Scene> scenes;

    public static void init(State startingState) {
        scenes = new HashMap<>();

        scenes.put(State.MENU_SCREEN, new MainMenu());
        scenes.put(State.MODE_SELECT, new GameModeSelect());
        scenes.put(State.GAME_1P, new GameBoard(State.GAME_1P));
        scenes.put(State.GAME_2P, new GameBoard(State.GAME_2P));
        scenes.put(State.STATS_SCREEN, new MainMenu());
        scenes.put(State.QUIT_GAME, new ConfirmExit(State.MENU_SCREEN, State.KILL_GAME));

        CurrentScene = scenes.get(startingState);
    }

    public static void setCurrentScene(Scene scene) {
        CurrentScene = scene;
    }

    public static Scene getCurrentScene() {
        return CurrentScene;
    }

    public static void setCurrentScene(State state) {
        CurrentScene = scenes.get(state);
    }

    public void dispose() {
        for (Scene scene : scenes.values()) {
            scene.dispose();
        }
    }
}
