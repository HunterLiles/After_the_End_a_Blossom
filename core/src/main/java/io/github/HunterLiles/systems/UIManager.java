package io.github.HunterLiles.systems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;

// Handles all UI-related functionality
public class UIManager implements Disposable {
    // UI constants
    private static final float LEVEL_LABEL_FADE_TIME = 3f;

    // UI components
    private Stage stage;
    private BitmapFont font;
    private Label levelNameLabel;

    // UI state
    private float levelLabelTimer;
    private boolean showLevelLabel;

    // Constructor - creates a new UI manager
    public UIManager(Stage stage) {
        this.stage = stage;
        initializeUI();
    }

    // Initialize UI components
    private void initializeUI() {
        // Create font for UI
        font = new BitmapFont();
        font.getData().setScale(1.5f);

        // Create label style
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);

        // Create level name label
        levelNameLabel = new Label("", labelStyle);
        levelNameLabel.setAlignment(Align.center);
        levelNameLabel.setVisible(false);

        // Add label to stage
        Table table = new Table();
        table.setFillParent(true);
        table.top().padTop(50);
        table.add(levelNameLabel).expandX();

        stage.addActor(table);
    }

    // Show the level name
    public void showLevelName(String levelName, String eraName, boolean gameStarted) {
        String levelText;
        if (!gameStarted) {
            levelText = levelName + " - " + eraName + "\nPress any key to start";
        } else {
            levelText = levelName + " - " + eraName;
        }

        levelNameLabel.setText(levelText);
        levelNameLabel.setVisible(true);
        showLevelLabel = true;
        levelLabelTimer = LEVEL_LABEL_FADE_TIME;
    }

    // Hide the level name
    public void hideLevelName() {
        levelNameLabel.setVisible(false);
        showLevelLabel = false;
    }

    // Update UI elements
    public void update(float delta) {
        // Update level label timer
        if (showLevelLabel) {
            levelLabelTimer -= delta;
            if (levelLabelTimer <= 0) {
                levelNameLabel.setVisible(false);
                showLevelLabel = false;
            }
        }

        // Update stage
        if (stage != null) {
            stage.act(delta);
        }
    }

    // Draw UI elements
    public void render() {
        if (stage != null) {
            stage.draw();
        }
    }

    // Clean up resources
    @Override
    public void dispose() {
        if (font != null) {
            font.dispose();
            font = null;
        }
    }
}
