package com.bangma.qor.utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.bangma.qor.config.Constant;

import java.util.HashMap;
import java.util.Map;

public class FileManager {
    private static Map<String, Texture> textures = new HashMap<>();
    private static Map<String, Music> music = new HashMap<>();

    public static Texture getTexture(String file) {
        if (textures.get(file) == null)
            textures.put(file, new Texture(Gdx.files.local(Constant.ASSET_PATH + file)));
        return textures.get(file);
    }

    public static Music getMusic(String file) {
        if (music.get(file) == null)
            music.put(file, Gdx.audio.newMusic(Gdx.files.local(Constant.ASSET_PATH + file)));
        return music.get(file);
    }

}
