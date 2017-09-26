package com.bangma.qor.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.bangma.qor.utility.StateManager;

import static com.bangma.qor.utility.StateManager.State;

import java.util.List;

public class Button extends Sprite {
    State state;
    Scene scene;

    private Button(Texture t, float x, float y) {
        super(t);
        this.setX(x);
        this.setY(y);
    }
    public Button(Texture t, State state, float x, float y) {
        this(t, x, y);
        this.state = state;
        this.scene = null;
    }
    public Button(Texture t, Scene scene, float x, float y) {
        this(t, x, y);
        this.scene = scene;
        this.state = null;
    }

    public static void buttonClick(List<Button> buttons, Vector2 mousePosition) {
        for (Button s : buttons) {
            if (s.getBoundingRectangle().contains(mousePosition)) {
                s.setAlpha(0.5f);
                if (Gdx.input.justTouched()) {
                    if (s.state == State.KILL_GAME) Gdx.app.exit();
                    if (s.state != null) StateManager.setCurrentScene(s.state);
                    else StateManager.setCurrentScene(s.scene);
                }
            } else {
                s.setAlpha(1f);
            }
        }
    }

    public static void drawButtons(List<Button> buttons, SpriteBatch batch, Texture background) {
        batch.begin();
        batch.draw(background, 0, 0);
        for (Button button : buttons) {
            button.draw(batch);
        }
        batch.end();
    }

    public static void drawButtons(List<Button> buttons, SpriteBatch batch) {
        for (Button button : buttons) {
            button.draw(batch);
        }
    }
}
