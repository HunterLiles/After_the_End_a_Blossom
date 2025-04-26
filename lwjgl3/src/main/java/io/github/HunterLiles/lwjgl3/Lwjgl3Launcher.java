package io.github.HunterLiles.lwjgl3;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import io.github.HunterLiles.Main;

public class Lwjgl3Launcher {
    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return;
        createApplication(); }

    private static Lwjgl3Application createApplication() { return new Lwjgl3Application(new Main(), getDefaultConfiguration()); }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("After the End, A Blossom");
        configuration.useVsync(true);
        configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1);
        configuration.setWindowedMode(1000, 600);
        //This is the code to make it fullscreen when I want it to be.
        //Graphics.DisplayMode displayMode = Lwjgl3ApplicationConfiguration.getDisplayMode();
        //configuration.setFullscreenMode(displayMode);
        return configuration; }}
