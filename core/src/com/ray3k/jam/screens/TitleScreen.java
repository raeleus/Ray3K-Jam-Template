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
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ray3k.jam.Core;
import com.ray3k.jam.JamScreen;

/**
 *
 * @author Raymond
 */
public class TitleScreen extends JamScreen {
    private Stage stage;
    private Skin skin;
    private Core core;

    public TitleScreen(final Core core) {
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
        root.addAction(Actions.fadeIn(1));
        
        var image = new Image(skin, "title");
        image.setScaling(Scaling.fit);
        root.add(image).grow();
        
        root.row();
        var label = new Label("Press any key to begin...", skin);
        root.add(label);

        stage.addListener(core.handListener);
        
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                execute();
                return true;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                execute();
                return true;
            }
            
            private void execute() {
                Gdx.input.setInputProcessor(null);
                root.addAction(Actions.sequence(Actions.fadeOut(1), new Action() {
                    @Override
                    public boolean act(float delta) {
                        core.setScreen(new MenuScreen(core));
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
