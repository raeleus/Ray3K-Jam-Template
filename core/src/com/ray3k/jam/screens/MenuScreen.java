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
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ray3k.jam.Core;
import com.ray3k.jam.JamScreen;

/**
 *
 * @author Raymond
 */
public class MenuScreen extends JamScreen {
    private Stage stage;
    private Skin skin;
    private Core core;

    public MenuScreen(final Core core) {
        this.core = core;
    }

    @Override
    public void show() {
        this.skin = core.assetManager.get("ui/ui.json");
        
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        
        var root = new Table();
        root.setFillParent(true);
        stage.addActor(root);
        
        root.setColor(1, 1, 1, 0);
        root.addAction(Actions.fadeIn(.5f));
        
        var label = new Label("Game Title", skin);
        root.add(label);
        
        root.row();
        var table = new Table();
        root.add(table);
        
        table.defaults().growX();
        var textButton = new TextButton("PLAY", skin);
        table.add(textButton);
        textButton.addListener(core.handListener);
        
        table.row();
        textButton = new TextButton("OPTIONS", skin);
        table.add(textButton);
        textButton.addListener(core.handListener);
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                root.addAction(Actions.sequence(Actions.fadeOut(.5f), new Action() {
                    @Override
                    public boolean act(float delta) {
                        core.setScreen(new OptionsScreen(core));
                        return true;
                    }
                }));
            }
        });
        
        table.row();
        textButton = new TextButton("CREDITS", skin);
        table.add(textButton);
        textButton.addListener(core.handListener);
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                root.addAction(Actions.sequence(Actions.fadeOut(.5f), new Action() {
                    @Override
                    public boolean act(float delta) {
                        core.setScreen(new CreditsScreen(core));
                        return true;
                    }
                }));
            }
        });
    }

    @Override
    public void act(float delta) {
        stage.act();
        
        if (Gdx.input.isKeyJustPressed(Keys.F5)) {
            dispose();
            show();
        }
    }

    @Override
    public void draw(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        core.batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
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
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
