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
package com.ray3k.jam.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ray3k.jam.Core;
import com.ray3k.jam.EntityManager;
import com.ray3k.jam.JamScreen;
import com.ray3k.jam.entities.intro.LogoEntity;

/**
 *
 * @author Raymond
 */
public class LogoScreen extends JamScreen {
    private Core core;
    private EntityManager entityManager;
    private Viewport viewport;
    
    public LogoScreen(final Core core) {
        this.core = core;
        entityManager = new EntityManager();
        viewport = new ScreenViewport();
        viewport.update(800, 800, true);
        viewport.apply();
        
        var logo = new LogoEntity(core);
        entityManager.addEntity(logo);
    }

    @Override
    public void show() {
    }

    @Override
    public void act(float delta) {
        entityManager.act(delta);
    }

    @Override
    public void draw(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        core.batch.setProjectionMatrix(viewport.getCamera().combined);
        core.batch.begin();
        core.batch.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_ALPHA);
        entityManager.draw(core.batch, delta);
        core.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        core.assetManager.unload("sfx/ray3k intro.mp3");
        core.assetManager.unload("sfx/shimmer.mp3");
        core.assetManager.unload("sfx/tick.mp3");
        core.assetManager.unload("spine/intro/logo.json");
        core.assetManager.unload("spine/intro/textureatlas.atlas");
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        
    }
}
