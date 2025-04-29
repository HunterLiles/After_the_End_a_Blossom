package io.github.HunterLiles.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class AnimationHandler {
    Animation<TextureAtlas.AtlasRegion> idleAnimation, runAnimation, jumpAnimation, flippedIdleAnimation, flippedRunAnimation, currentAnimation;
    TextureAtlas atlas;
    private float elapsedTime = 0;

    private void animationHandler() {
        //This is where the animation file is being loaded and split into its different animation states.
        atlas = new TextureAtlas(Gdx.files.internal("images/PlayerAnimation.atlas"));
        idleAnimation = new Animation<>(1/2f, atlas.findRegions("idle"));
        runAnimation = new Animation<>(1/5f, atlas.findRegions("run"));
        jumpAnimation = new Animation<>(1/5f, atlas.findRegions("jump"));
        flippedIdleAnimation = new Animation<>(1/5f, atlas.findRegions("flipped-idle"));
        flippedRunAnimation = new Animation<>(1/5f, atlas.findRegions("flipped-run"));
        currentAnimation = idleAnimation;

        //This changes the animation of the character based on input. Flips it if it is going the opposite direction.
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.A) {
                    currentAnimation = flippedRunAnimation;
                    return true; }
                if (keyCode == Input.Keys.D) {
                    currentAnimation = runAnimation;
                    return true; }
                if (keyCode == Input.Keys.LEFT) {
                    currentAnimation = flippedRunAnimation;
                    return true; }
                if (keyCode == Input.Keys.RIGHT) {
                    currentAnimation = runAnimation;
                    return true; }
                return false; }

            @Override public boolean keyUp(int keyCode) {
                if (keyCode == Input.Keys.A) {
                    currentAnimation = flippedIdleAnimation;
                    return true; }
                if (keyCode == Input.Keys.D) {
                    currentAnimation = idleAnimation;
                    return true; }
                if (keyCode == Input.Keys.LEFT) {
                    currentAnimation = flippedIdleAnimation;
                    return true; }
                if (keyCode == Input.Keys.RIGHT) {
                    currentAnimation = idleAnimation;
                    return true; }
                return false; }});
    }
    public void render (SpriteBatch batch) {
        batch.draw(currentAnimation.getKeyFrame(elapsedTime, true), -100, -250, 120, 100);
    }
}
