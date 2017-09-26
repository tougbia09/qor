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

public class ConfirmExit implements Scene {
    private List<Button> buttons;
    private SpriteBatch batch;
    private Texture background;

    public ConfirmExit(State cancel, State confirm) {
        background = FileManager.getTexture("quit background.png");
        batch = new SpriteBatch();
        buttons = new ArrayList<>();
        buttons.add(new Button(
                FileManager.getTexture("yes.png"),
                confirm,
                50,
                Constant.SCREEN_SIZE / 4
        ));
        buttons.add(new Button(
                FileManager.getTexture("no.png"),
                cancel,
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
    	for (Button b : buttons) {
    		b.getTexture().dispose();
    	}
    }
}
