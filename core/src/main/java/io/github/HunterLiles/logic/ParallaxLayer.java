package io.github.HunterLiles.logic;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ParallaxLayer extends Actor {
    private Texture texture;
    private float factor;
    private boolean wrapHorizontally, wrapVertically;
    private Camera camera;

    public ParallaxLayer(Texture texture, float factor, boolean wrapHorizontally, boolean wrapVertically) {
        this.texture = texture;
        this.factor = factor;
        this.wrapHorizontally = wrapHorizontally;
        this.wrapVertically = wrapVertically;
        this.texture.setWrap(wrapHorizontally ? Texture.TextureWrap.Repeat : Texture.TextureWrap.ClampToEdge, wrapVertically ? Texture.TextureWrap.Repeat : Texture.TextureWrap.ClampToEdge); }

    public void setCamera(Camera camera) { this.camera = camera; }

    public void render(SpriteBatch batch) {
        int xOffset = (int) (camera.position.x * factor);
        int yOffset = (int) (camera.position.y * factor);
        TextureRegion region = new TextureRegion(texture);
        region.setRegionX(xOffset % texture.getWidth());
        region.setRegionY(yOffset % texture.getHeight());
        region.setRegionWidth(wrapHorizontally ? (int) camera.viewportWidth : texture.getWidth());
        region.setRegionHeight(wrapVertically ? (int) camera.viewportHeight : texture.getHeight());
        batch.draw(region, camera.position.x - camera.viewportWidth/2, camera.position.y - camera.viewportHeight/2); }}
