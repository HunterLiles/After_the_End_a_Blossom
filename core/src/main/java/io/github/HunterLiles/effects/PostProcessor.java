package io.github.HunterLiles.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Disposable;

/**
 * Handles post-processing effects for the game.
 * This class manages frame buffers and shaders to apply visual effects
 * like bloom, color grading, and vignette to the rendered scene.
 */
public class PostProcessor implements Disposable {
    private FrameBuffer frameBuffer;
    private ShaderProgram bloomShader;
    private ShaderProgram vignetteShader;
    private ShaderProgram colorGradingShader;
    private ShaderProgram currentShader;

    private boolean enabled = false;
    private boolean bloomEnabled = false;
    private boolean vignetteEnabled = false;
    private boolean colorGradingEnabled = false;

    private float bloomIntensity = 0.5f;
    private float bloomThreshold = 0.5f;
    private float vignetteIntensity = 0.5f;
    private float colorGradingIntensity = 0.5f;

    private int width, height;
    private Matrix4 projectionMatrix;

    /**
     * Creates a new PostProcessor with the specified dimensions.
     *
     * @param width The width of the frame buffer
     * @param height The height of the frame buffer
     */
    public PostProcessor(int width, int height) {
        this.width = width;
        this.height = height;

        // Create frame buffer
        frameBuffer = new FrameBuffer(Format.RGBA8888, width, height, false);

        // Load shaders
        loadShaders();

        // Set up projection matrix for rendering to screen
        projectionMatrix = new Matrix4();
        projectionMatrix.setToOrtho2D(0, 0, width, height);
    }

    /**
     * Loads and compiles the shader programs.
     */
    private void loadShaders() {
        // Enable shader compilation error logging
        ShaderProgram.pedantic = false;

        // Load bloom shader
        bloomShader = new ShaderProgram(
            Gdx.files.internal("shaders/bloom.vert"),
            Gdx.files.internal("shaders/bloom.frag")
        );
        if (!bloomShader.isCompiled()) {
            Gdx.app.error("PostProcessor", "Bloom shader compilation failed: " + bloomShader.getLog());
            bloomEnabled = false;
        }

        // Load vignette shader
        vignetteShader = new ShaderProgram(
            Gdx.files.internal("shaders/vignette.vert"),
            Gdx.files.internal("shaders/vignette.frag")
        );
        if (!vignetteShader.isCompiled()) {
            Gdx.app.error("PostProcessor", "Vignette shader compilation failed: " + vignetteShader.getLog());
            vignetteEnabled = false;
        }

        // Load color grading shader
        colorGradingShader = new ShaderProgram(
            Gdx.files.internal("shaders/colorgrading.vert"),
            Gdx.files.internal("shaders/colorgrading.frag")
        );
        if (!colorGradingShader.isCompiled()) {
            Gdx.app.error("PostProcessor", "Color grading shader compilation failed: " + colorGradingShader.getLog());
            colorGradingEnabled = false;
        }

        // Default to bloom shader
        currentShader = bloomShader;
    }

    /**
     * Begins capturing the scene to the frame buffer.
     * Call this before rendering your scene.
     */
    public void begin() {
        if (!enabled) return;

        frameBuffer.begin();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    /**
     * Ends capturing and renders the processed scene to the screen.
     * Call this after rendering your scene.
     *
     * @param batch The sprite batch to use for rendering
     */
    public void end(SpriteBatch batch) {
        if (!enabled) return;

        frameBuffer.end();

        // Save the batch's current projection matrix
        Matrix4 batchProjectionMatrix = batch.getProjectionMatrix().cpy();

        // Set the batch to use our projection matrix
        batch.setProjectionMatrix(projectionMatrix);

        // Apply post-processing effects
        batch.begin();

        // Apply bloom effect
        if (bloomEnabled) {
            batch.setShader(bloomShader);
            bloomShader.setUniformf("u_bloomIntensity", bloomIntensity);
            bloomShader.setUniformf("u_bloomThreshold", bloomThreshold);
        }

        // Apply vignette effect
        if (vignetteEnabled) {
            batch.setShader(vignetteShader);
            vignetteShader.setUniformf("u_vignetteIntensity", vignetteIntensity);
            vignetteShader.setUniformf("u_resolution", width, height);
        }

        // Apply color grading effect
        if (colorGradingEnabled) {
            batch.setShader(colorGradingShader);
            colorGradingShader.setUniformf("u_colorGradingIntensity", colorGradingIntensity);
        }

        // Draw the frame buffer texture to the screen
        batch.draw(frameBuffer.getColorBufferTexture(), 0, 0, width, height, 0, 0, 1, 1);

        // Reset the shader
        batch.setShader(null);

        batch.end();

        // Restore the batch's projection matrix
        batch.setProjectionMatrix(batchProjectionMatrix);
    }

    /**
     * Resizes the post-processor to match the new dimensions.
     *
     * @param width The new width
     * @param height The new height
     */
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;

        // Dispose of the old frame buffer
        if (frameBuffer != null) {
            frameBuffer.dispose();
        }

        // Create a new frame buffer with the new dimensions
        frameBuffer = new FrameBuffer(Format.RGBA8888, width, height, false);

        // Update the projection matrix
        projectionMatrix.setToOrtho2D(0, 0, width, height);
    }

    /**
     * Enables or disables the post-processor.
     *
     * @param enabled Whether the post-processor should be enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Checks if the post-processor is enabled.
     *
     * @return Whether the post-processor is enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Enables or disables the bloom effect.
     *
     * @param enabled Whether the bloom effect should be enabled
     */
    public void setBloomEnabled(boolean enabled) {
        this.bloomEnabled = enabled;
    }

    /**
     * Checks if the bloom effect is enabled.
     *
     * @return Whether the bloom effect is enabled
     */
    public boolean isBloomEnabled() {
        return bloomEnabled;
    }

    /**
     * Sets the intensity of the bloom effect.
     *
     * @param intensity The bloom intensity (0-1)
     */
    public void setBloomIntensity(float intensity) {
        this.bloomIntensity = intensity;
    }

    /**
     * Sets the threshold of the bloom effect.
     *
     * @param threshold The bloom threshold (0-1)
     */
    public void setBloomThreshold(float threshold) {
        this.bloomThreshold = threshold;
    }

    /**
     * Enables or disables the vignette effect.
     *
     * @param enabled Whether the vignette effect should be enabled
     */
    public void setVignetteEnabled(boolean enabled) {
        this.vignetteEnabled = enabled;
    }

    /**
     * Checks if the vignette effect is enabled.
     *
     * @return Whether the vignette effect is enabled
     */
    public boolean isVignetteEnabled() {
        return vignetteEnabled;
    }

    /**
     * Sets the intensity of the vignette effect.
     *
     * @param intensity The vignette intensity (0-1)
     */
    public void setVignetteIntensity(float intensity) {
        this.vignetteIntensity = intensity;
    }

    /**
     * Enables or disables the color grading effect.
     *
     * @param enabled Whether the color grading effect should be enabled
     */
    public void setColorGradingEnabled(boolean enabled) {
        this.colorGradingEnabled = enabled;
    }

    /**
     * Checks if the color grading effect is enabled.
     *
     * @return Whether the color grading effect is enabled
     */
    public boolean isColorGradingEnabled() {
        return colorGradingEnabled;
    }

    /**
     * Sets the intensity of the color grading effect.
     *
     * @param intensity The color grading intensity (0-1)
     */
    public void setColorGradingIntensity(float intensity) {
        this.colorGradingIntensity = intensity;
    }

    @Override
    public void dispose() {
        if (frameBuffer != null) {
            frameBuffer.dispose();
        }

        if (bloomShader != null) {
            bloomShader.dispose();
        }

        if (vignetteShader != null) {
            vignetteShader.dispose();
        }

        if (colorGradingShader != null) {
            colorGradingShader.dispose();
        }
    }
}
