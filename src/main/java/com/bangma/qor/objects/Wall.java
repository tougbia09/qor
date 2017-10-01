package com.bangma.qor.objects;

import java.util.Map;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bangma.qor.math.Position;

public class Wall extends Sprite {
	private Position gridPosition;
	public final char orientation;
	
    public Wall(Texture t, int gx, int gy, float px, float py, char orient) {
        super(t);
        gridPosition = new Position(gx, gy);
        this.setX(px);
        this.setY(py);
        this.orientation = orient;
    }

    public static void drawWalls(Map<String, Wall> walls, SpriteBatch batch) {
        for (Wall wall : walls.values()) {
        	wall.draw(batch);
        }
    }
    public Position getGridPosition() {
        return gridPosition;
    }
}
