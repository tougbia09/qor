package com.bangma.qor.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.bangma.qor.config.Constant;
import com.bangma.qor.math.Position;

public class Player extends Sprite {
    private Position gridPosition;
    private int walls;

    public Player(Texture t, int x, int y) {
        super(t);
        walls = 10;
        Position pos = new Position(x, y);
        move(pos);
    }

    public void move(Position newPosition) {
        gridPosition = newPosition;
        setPixelPosition(gridPosition);
    }

    public Position getGridPosition() {
        return gridPosition;
    }
    private void setPixelPosition(Position gridPosition) {
        this.setX((float) Constant.GRID_OFFSET_X + (gridPosition.x * Constant.SQUARE_SIZE + 9));
        this.setY((float) Constant.GRID_OFFSET_Y + (gridPosition.y * Constant.SQUARE_SIZE + 8));
    }

    public int getRemainingWalls() {
        return this.walls;
    }
    public void decrementWalls() {
    	this.walls--;
    }
}
