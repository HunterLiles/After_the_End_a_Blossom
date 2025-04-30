package io.github.HunterLiles.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.HashMap;

public class Player extends Actor {
    private float elapsedTime;
    private Animation<TextureRegion> currentAnimation;
    private HashMap<String, Animation<TextureRegion>> animations = new HashMap<>();
    private TextureAtlas atlas;
    private static final float DEFAULT_WIDTH = 120f;
    private static final float DEFAULT_HEIGHT = 100f;

    public Player(String location, String initialState) {
        atlas = new TextureAtlas(Gdx.files.internal("images/" + location + ".atlas"));
        loadAnimation(initialState);
        currentAnimation = animations.get(initialState);
        elapsedTime = 0f;
    }

    private void loadAnimation(String state) {
        if (!animations.containsKey(state)) {
            Animation<TextureRegion> animation = new Animation<>(1/4f, atlas.findRegions(state));
            animations.put(state, animation);
        }
    }

    public void setAnimation(String state) {
        loadAnimation(state);
        currentAnimation = animations.get(state);
    }

    public void update(float delta) {
        elapsedTime += delta;
    }

    public void render(Batch batch, float x, float y) {
        if (currentAnimation != null) {
            batch.draw(currentAnimation.getKeyFrame(elapsedTime, true), x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        }
    }
}
