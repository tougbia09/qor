package com.bangma.qor;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.bangma.qor.config.Constant;
import com.bangma.qor.objects.Scene;
import com.bangma.qor.utility.FileManager;
import com.bangma.qor.utility.StateManager;

import static com.bangma.qor.utility.StateManager.State;

public class QorApplication extends ApplicationAdapter {
    Music loop;
    
    public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Quoridor | Capstone - Tim Bangma 000316312";
        config.height = 800;
        config.width = 800;
		new LwjglApplication(new QorApplication(), config);
	}
    
    @Override
    public void create() {
        StateManager.init(State.MENU_SCREEN);

	    /* Start up game music. */
        loop = FileManager.getMusic("game theme loop.ogg");
        loop.setLooping(true);
        loop.play();
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Scene currentScene = StateManager.getCurrentScene();
        currentScene.update(new Vector2(Gdx.input.getX(), Constant.SCREEN_SIZE - Gdx.input.getY()));
        currentScene.draw();
    }

    @Override
    public void dispose() {
        StateManager.dispose();
        loop.dispose();
    }
}
