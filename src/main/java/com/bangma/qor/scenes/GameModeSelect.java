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

public class GameModeSelect implements Scene {
    private List<Button> buttons;
    private SpriteBatch batch;
    private Texture background;

    public GameModeSelect() {
        batch = new SpriteBatch();
        background = FileManager.getTexture("mode background.png");
        buttons = new ArrayList<>();

        buttons.add(new Button(
                FileManager.getTexture("cpu.png"),
                State.GAME_1P,
                50,
                Constant.SCREEN_SIZE / 4
        ));
        buttons.add(new Button(
                FileManager.getTexture("2play.png"),
                State.GAME_2P,
                Constant.SCREEN_SIZE - Constant.SMALL_BUTTON_WIDTH - 50,
                Constant.SCREEN_SIZE / 4
        ));
    }

    public void update(Vector2 mousePosition) {
        Button.buttonClick(buttons, mousePosition);
    }

    public void draw() {
        Button.drawButtons(buttons, batch, background);
    }

    public void dispose() {

    }
}
