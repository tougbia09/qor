package com.bangma.qor.objects;

import com.badlogic.gdx.math.Vector2;


/**
 * A game scene, all the logic and objects required for a scene will be contained in a Scene.
 */
public interface Scene {
    /**
     * Update the game world as required based on interaction
     * @param mousePosition the position of the mouse.
     */
    void update(Vector2 mousePosition);

    /**
     * Draw the sprites and backgrounds to the screen.
     */
    void draw();

    /**
     * Remove all resource references and dispose of any memory the scene would hang onto otherwise.
     */
    void dispose();
}
