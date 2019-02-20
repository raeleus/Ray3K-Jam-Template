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

import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.esotericsoftware.spine.utils.TwoColorPolygonBatch;
import com.ray3k.jam.Collidable;
import com.ray3k.jam.Core;
import com.ray3k.jam.EntityManager;
import com.ray3k.jam.GameInputProcessor;
import com.ray3k.jam.JamScreen;
import com.ray3k.jam.SpineEntity;

/**
 *
 * @author Raymond
 */
public class GameScreen extends JamScreen {

    public static GameScreen gameScreen;
    private Actor actionsManager;
    private EntityManager entityManager;
    private Stage stage;
    private Skin skin;
    private Core core;
    private OrthographicCamera gameCamera;
    private Viewport gameViewport;
    private InputMultiplexer inputMultiplexer;
    private GameInputProcessor gameInputProcessor;

    public GameScreen(Core core) {
        this.core = core;
        gameScreen = this;
    }

    @Override
    public void show() {
        actionsManager = new Actor();
        entityManager = new EntityManager();
        stage = new Stage(new ScreenViewport(), core.batch);
        skin = core.assetManager.get("ui/ui.json", Skin.class);
        gameCamera = new OrthographicCamera();
        gameViewport = new ExtendViewport(800, 800, gameCamera);
        gameCamera.position.set(400, 400, 0);
        gameCamera.update();
        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        gameInputProcessor = new GameInputProcessor();
        inputMultiplexer.addProcessor(gameInputProcessor);

        populateStage();
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void act(float delta) {
        label.setText(Integer.toString(Gdx.graphics.getFramesPerSecond()));
        label.setColor(Color.RED);

        actionsManager.act(delta);
        entityManager.act(delta);
        stage.act(delta);

        if (gameInputProcessor.isKeyJustPressed(Keys.F5)) {
            dispose();
            show();
        }
        gameInputProcessor.act(delta);
    }

    @Override
    public void draw(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        core.batch.setProjectionMatrix(gameCamera.combined);
        core.batch.begin();
        gameViewport.apply();
        core.batch.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_ALPHA);
        entityManager.draw(core.batch, delta);
        core.batch.end();

        core.batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        stage.dispose();
        Gdx.input.setInputProcessor(null);
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    private Label label;

    private void populateStage() {
        var root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        var textButton = new TextButton("test", skin);
        root.add(textButton).expand().bottom().right();
        textButton.addListener(core.handListener);
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                System.out.println("it works");
            }
        });

        label = new Label("", skin);
        label.setColor(Color.RED);
        root.add(label).bottom().pad(100);
    }
}
