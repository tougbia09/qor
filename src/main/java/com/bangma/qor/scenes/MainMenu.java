package com.bangma.qor.scenes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.bangma.qor.config.Constant;
import com.bangma.qor.objects.Button;
import com.bangma.qor.objects.Scene;
import com.bangma.qor.utility.FileManager;
import java.util.ArrayList;
import java.util.List;

import static com.bangma.qor.utility.StateManager.State;

public class MainMenu implements Scene {
    private Texture background;
    private SpriteBatch batch;
    private List<Button> buttons;

    public MainMenu() {

        buttons = new ArrayList<>(3);
        batch = new SpriteBatch();

        buttons.add(new Button(
                FileManager.getTexture("start.png"),
                State.MODE_SELECT,
                Constant.LARGE_BUTTON_CENTER,
                425)
        );
        buttons.add(new Button(
                FileManager.getTexture("stats.png"),
                State.STATS_SCREEN,
                Constant.LARGE_BUTTON_CENTER,
                225)
        );
        buttons.add(new Button(
                FileManager.getTexture("quit game.png"),
                State.QUIT_GAME,
                Constant.LARGE_BUTTON_CENTER,
                25)
        );
        background = FileManager.getTexture("menu background.png");
    }

    public void update(Vector2 mousePosition) {
        Button.buttonClick(buttons, mousePosition);
    }

    public void draw() {
        Button.drawButtons(buttons, batch, background);
    }

    public void dispose() {
        background.dispose();
        for (Button button : buttons) {
            button.getTexture().dispose();
        }
    }
}
