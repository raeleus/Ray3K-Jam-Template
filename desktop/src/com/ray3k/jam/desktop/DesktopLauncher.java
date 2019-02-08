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

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ray3k.jam.Core;
import com.ray3k.jam.DesktopWorker;
import java.awt.SplashScreen;

public class DesktopLauncher implements DesktopWorker {

    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 800;
        config.height = 800;
        config.addIcon("icons/icon_16x16.png", Files.FileType.Internal);
        config.addIcon("icons/icon_32x32.png", Files.FileType.Internal);
        config.addIcon("icons/icon_48x48.png", Files.FileType.Internal);
        config.allowSoftwareMode = true;
        new LwjglApplication(new Core(), config);
    }

    @Override
    public void closeSplash() {
        SplashScreen  splashScreen = SplashScreen.getSplashScreen();
        if (splashScreen != null) {
            splashScreen.close();
        }
    }
}
