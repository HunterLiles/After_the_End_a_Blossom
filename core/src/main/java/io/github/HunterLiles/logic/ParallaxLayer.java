package io.github.HunterLiles.logic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

//Creates a parallax scrolling effect for background layers.
//Each layer moves at a different speed relative to the camera position,
//creating an illusion of depth.
public class ParallaxLayer extends Actor {
    private Texture texture;
    private float factor;
    private boolean wrapHorizontally, wrapVertically;
    private OrthographicCamera camera;
    private Color tint = new Color(1, 1, 1, 1); // Default white tint (no tinting)

    //Creates a new parallax layer with the specified texture and movement properties.
    //factor: 1.0 = moves with camera, 0.5 = moves at half speed
    public ParallaxLayer(Texture texture, float factor, boolean wrapHorizontally, boolean wrapVertically) {
        this.texture = texture;
        this.factor = factor;
        this.wrapHorizontally = wrapHorizontally;
        this.wrapVertically = wrapVertically;

        Texture.TextureWrap horizontalWrap = wrapHorizontally ?
                Texture.TextureWrap.Repeat : Texture.TextureWrap.ClampToEdge;
        Texture.TextureWrap verticalWrap = wrapVertically ?
                Texture.TextureWrap.Repeat : Texture.TextureWrap.ClampToEdge;
        this.texture.setWrap(horizontalWrap, verticalWrap);
    }

    //Sets the camera to track for parallax movement
    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

    //Renders the parallax layer relative to the camera position
    //The layer will move at a speed determined by the factor value
    public void render(SpriteBatch batch) {
        if (camera == null) {
            return;
        }

        int xOffset = (int) (camera.position.x * factor);

        //No vertical parallax effect
        int yOffset = 0;

        TextureRegion region = new TextureRegion(texture);
        region.setRegionX(xOffset % texture.getWidth());
        region.setRegionY(yOffset);

        region.setRegionWidth(wrapHorizontally ? (int) camera.viewportWidth : texture.getWidth());
        region.setRegionHeight(wrapVertically ? (int) camera.viewportHeight : texture.getHeight());

        float drawX = camera.position.x - camera.viewportWidth / 2;
        float drawY = camera.position.y - camera.viewportHeight / 2;

        // Save the current color
        Color prevColor = batch.getColor();

        // Apply tint
        batch.setColor(tint);
        batch.draw(region, drawX, drawY);

        // Restore the previous color
        batch.setColor(prevColor);
    }

    /**
     * Sets the tint color for this parallax layer.
     *
     * @param r Red component (0-1)
     * @param g Green component (0-1)
     * @param b Blue component (0-1)
     * @param a Alpha component (0-1)
     */
    public void setTint(float r, float g, float b, float a) {
        tint.set(r, g, b, a);
    }

    /**
     * Sets the tint color for this parallax layer.
     *
     * @param color The color to use for tinting
     */
    public void setTint(Color color) {
        tint.set(color);
    }
}
