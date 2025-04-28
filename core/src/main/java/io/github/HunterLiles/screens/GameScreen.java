package io.github.HunterLiles.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import io.github.HunterLiles.Main;
import io.github.HunterLiles.logic.ParallaxLayer;

public class GameScreen extends InputAdapter implements Screen {
    private Stage stage;
    private Main game;
    private SpriteBatch batch;
    private float playerX = -100, playerY = -250;
    private ParallaxLayer[] layers;
    private ParallaxLayer[] foreground;
    private OrthographicCamera camera;
    private static final float FRAME_TIME = 0.07f;
    private float elapsedTime = 0;
    private Sprite currentAnimation;
    private TextureAtlas atlas;
    private Sprite idle, run, jump;
    //World world = new World(new Vector2(0, -10), true);
    //Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();


    public GameScreen(Main game) {
        this.game = game;

        //Music
        Music music = Gdx.audio.newMusic(Gdx.files.internal("audio/Movie_Theme.mp3"));
        music.setLooping(true);
        music.play(); }

    //This is pretty much the same as the create method.
    @Override public void show() {
        Gdx.input.setInputProcessor(stage);
        batch = new SpriteBatch();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        //ParallaxLayer loader.
        parallaxLayer();

        //Animation loader and changer.
        animationLoader();
        animationHandler(idle, run, jump); }

    private void animationLoader(){
        //This is where the animation file is being loaded and split into its different animation states.
        atlas = new TextureAtlas(Gdx.files.internal("images/PlayerAnimation.atlas"));
        Animation<TextureRegion> idleAnimation = new Animation<>(0.1f, atlas.findRegions("idle"));
        Animation<TextureRegion> runAnimation = new Animation<>(0.1f, atlas.findRegions("run"));
        Animation<TextureRegion> jumpAnimation = new Animation<>(0.1f, atlas.findRegions("jump"));
        TextureRegion idleFrame = idleAnimation.getKeyFrame(elapsedTime, true);
        TextureRegion runFrame = runAnimation.getKeyFrame(elapsedTime, true);
        TextureRegion jumpFrame = jumpAnimation.getKeyFrame(elapsedTime, true);
        idle = new Sprite(idleFrame);
        run = new Sprite(runFrame);
        jump = new Sprite(jumpFrame);
        currentAnimation = idle; }

    private void animationHandler(Sprite idle, Sprite run, Sprite jump) {
        //This changes the animation of the character based on input. Flips it if it is going the opposite direction.
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.A) {
                    currentAnimation = run;
                    if (!run.isFlipX()) { run.flip(true, false); }
                    return true; }
                if (keyCode == Input.Keys.D) {
                    currentAnimation = run;
                    if (run.isFlipX()) { run.flip(true, false); }
                    return true; }
                if (keyCode == Input.Keys.LEFT) {
                    currentAnimation = run;
                    if (!run.isFlipX()) { run.flip(true, false); }
                    return true; }
                if (keyCode == Input.Keys.RIGHT) {
                    currentAnimation = run;
                    if (run.isFlipX()) { run.flip(true, false); }
                    return true; }
                return false; }

            @Override public boolean keyUp(int keyCode) {
                if (keyCode == Input.Keys.A) {
                    currentAnimation = idle;
                    if (!idle.isFlipX()) { idle.flip(true, false); }
                    return true; }
                if (keyCode == Input.Keys.D) {
                    currentAnimation = idle;
                    if (idle.isFlipX()) { idle.flip(true, false); }
                    return true; }
                if (keyCode == Input.Keys.LEFT) {
                    currentAnimation = idle;
                    if (!idle.isFlipX()) { idle.flip(true, false); }
                    return true; }
                if (keyCode == Input.Keys.RIGHT) {
                    currentAnimation = idle;
                    if (idle.isFlipX()) { idle.flip(true, false); }
                    return true; }
                return false; }});}

    private void parallaxLayer() {
        //Setting each parallax layer individually.
        layers = new ParallaxLayer[11];
        for (int i = 1; i <= 11; i++) {
            layers[i-1] = new ParallaxLayer(new Texture("images/background/" + i + ".png"), i / 12f, true, false); }

        //Makes the foreground actually in front of the character to look better.
        foreground = new ParallaxLayer[1];
        foreground[0] = new ParallaxLayer(new Texture(Gdx.files.internal("images/background/12.png")), 0.14f, true, false);

        //Sets the parallax affect.
        for (ParallaxLayer layer : layers) {
            layer.setCamera(camera);
            layer.setPosition(0, 0);
            layer.setSize(6.25f, 3.125f); }

        for (ParallaxLayer layer : foreground) {
            layer.setCamera(camera);
            layer.setPosition(0, 0);
            layer.setSize(6.25f, 3.125f); }}

    @Override public void render(float delta) {
        input();
        logic();
        draw();}

    //Neat little methods to keep things organized in render.
    //Movement, I still need to add jumping/physics.
    private void input() {
        float speed = 300.0f;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            playerX -= Gdx.graphics.getDeltaTime() * speed;
            camera.position.x -= Gdx.graphics.getDeltaTime() * speed;}
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            playerX += Gdx.graphics.getDeltaTime() * speed;
            camera.position.x += Gdx.graphics.getDeltaTime() * speed;}
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            playerX -= Gdx.graphics.getDeltaTime() * speed;
            camera.position.x -= Gdx.graphics.getDeltaTime() * speed;}
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            playerX += Gdx.graphics.getDeltaTime() * speed;
            camera.position.x += Gdx.graphics.getDeltaTime() * speed;}}

    //Don't know what I'm using this for yet.
    private void logic() {
        elapsedTime += Gdx.graphics.getDeltaTime();
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        //debugRenderer.render(world, camera.combined);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); }

    //Actual drawing of things.
    private void draw() {
        //All actual renderings happen between "begin" and "end".
        batch.begin();
            for (ParallaxLayer layer : layers) { layer.render(batch); }
            batch.draw(currentAnimation, playerX, playerY, 120, 100);
            for (ParallaxLayer layer : foreground) { layer.render(batch); }
        batch.end(); }
/*
    private void playerPhysics() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(playerX, playerY);

        Body body = world.createBody(bodyDef);
    }

    private void groundPhysics() {
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.StaticBody;
        groundBodyDef.position.set(new Vector2(0, 10));

        Body groundBody = world.createBody(groundBodyDef);

        PolygonShape groundShape = new PolygonShape();
        groundShape.setAsBox(camera.viewportWidth, 0);
        groundBody.createFixture(groundShape, 1f);
        groundShape.dispose(); }
 */
    //I shouldn't need to touch any of this.
    @Override public void resize(int width, int height) { }
    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }
    @Override public void dispose() { stage.dispose(); batch.dispose(); }}
