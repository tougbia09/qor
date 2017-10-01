package com.bangma.qor.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bangma.qor.math.Position;

import java.util.Map;

public class Square extends Sprite {
    private Position gridPosition;

    public Square(Texture t, int gx, int gy, float px, float py) {
        super(t);
        gridPosition = new Position(gx, gy);
        this.setX(px);
        this.setY(py);
    }

    public static void drawSquares(Map<Integer, Square> squares, SpriteBatch batch) {
        for (Square square : squares.values()) {
            square.draw(batch);
        }
    }
    public Position getGridPosition() {
        return gridPosition;
    }
}
