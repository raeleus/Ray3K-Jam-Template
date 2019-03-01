/*
 * The MIT License
 *
 * Copyright 2019 Raymond Buckley.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.ray3k.jam.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Input;
import com.ray3k.jam.Core;
import com.ray3k.jam.DesktopWorker;
import com.ray3k.jam.Utils;
import java.awt.SplashScreen;

public class DesktopLauncher implements DesktopWorker {

    public static void main(String[] arg) {
        DesktopLauncher desktopLauncher = new DesktopLauncher();
        if (!Utils.isWindows()) {
            desktopLauncher.closeSplash();
        }
        
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(800, 800);
        config.setWindowIcon("icons/icon_16x16.png", "icons/icon_32x32.png", "icons/icon_48x48.png");
        config.useVsync(true);
        config.setTitle("Please, Don't Go Back");
        Core core = new Core();
        core.desktopWorker = desktopLauncher;
        new Lwjgl3Application(core, config);
    }

    @Override
    public void closeSplash() {
        SplashScreen  splashScreen = SplashScreen.getSplashScreen();
        if (splashScreen != null) {
            splashScreen.close();
        }
    }
    
    @Override
    public char getKeyName(int keyCode) {
        int glfwKeyCode = Lwjgl3Input.getGlfwKeyCode(keyCode);
        try {
            String output = org.lwjgl.glfw.GLFW.glfwGetKeyName(glfwKeyCode, 0);
            return (output == null) ? ' ' : output.toLowerCase().charAt(0);
        } catch (Exception e) {
            return ' ';
        }
    }
}
