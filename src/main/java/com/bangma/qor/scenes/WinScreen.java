package com.bangma.qor.scenes;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.bangma.qor.config.Constant;
import com.bangma.qor.objects.Button;
import com.bangma.qor.objects.Scene;
import com.bangma.qor.utility.FileManager;
import com.bangma.qor.utility.StateManager.State;

public class WinScreen implements Scene {
	private final Texture background;
	private final List<Button> buttons;
	private SpriteBatch batch;
	
	public WinScreen(boolean playerOneWin) {
		if (playerOneWin) background = FileManager.getTexture("winner player one.png");
		else background = FileManager.getTexture("winner player two.png");
		batch = new SpriteBatch();
		
		buttons = new ArrayList<>();
		
		buttons.add(new Button(
                FileManager.getTexture("menu.png"),
                State.MENU_SCREEN,
                50,
                Constant.SCREEN_SIZE / 6)
        );
        buttons.add(new Button(
                FileManager.getTexture("quit.png"),
                State.QUIT_GAME,
                Constant.SCREEN_SIZE - Constant.SMALL_BUTTON_WIDTH - 50,
                Constant.SCREEN_SIZE / 6)
        );
        
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
