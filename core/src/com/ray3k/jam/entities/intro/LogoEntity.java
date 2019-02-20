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
package com.ray3k.jam.entities.intro;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.audio.Sound;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Event;
import com.esotericsoftware.spine.utils.TwoColorPolygonBatch;
import com.ray3k.jam.Collidable;
import com.ray3k.jam.Core;
import com.ray3k.jam.SoundFadeAction;
import com.ray3k.jam.SpineEntity;
import com.ray3k.jam.screens.TitleScreen;

/**
 *
 * @author Raymond
 */
public class LogoEntity extends SpineEntity {
    private long songId;

    public LogoEntity(Core core) {
        super(core, "spine/intro/logo.json", "animation");
        
        getAnimationState().addListener(new AnimationState.AnimationStateAdapter() {
            @Override
            public void event(AnimationState.TrackEntry entry, Event event) {
                var path = event.getData().getAudioPath();
                if (path != null) {
                    var id = core.playSound(path, event.getVolume());
                    if (path.equals("ray3k intro.mp3")) {
                        songId = id;
                    }
                }
            }

            @Override
            public void complete(AnimationState.TrackEntry entry) {
                if (entry.getAnimation().getName().equals("exit")) {
                    core.setScreen(new TitleScreen(core));
                }
            }
        });
        
        getAnimationState().getCurrent(0).setLoop(false);
        getAnimationState().addAnimation(0, "exit", false, 0);
        setPosition(400, 400);
        
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                execute();
                return true;
            }

            @Override
            public boolean keyDown(int keycode) {
                execute();
                return true;
            }
            
            private void execute() {
                if (!getAnimationState().getCurrent(0).getAnimation().getName().equals("exit")) {
                    getAnimationState().setAnimation(0, "exit", false);
                }
                
                core.actionsManager.addAction(new SoundFadeAction(1.0f, core.assetManager.get("sfx/ray3k intro.mp3", Sound.class), songId, null));
            }
        });
    }

    @Override
    public void actSub(float delta) {
    }

    @Override
    public void drawSub(TwoColorPolygonBatch spriteBatch, float delta) {
        
    }

    @Override
    public void create() {
    }

    @Override
    public void actEnd(float delta) {
    }

    @Override
    public void destroyEvent() {
    }

    @Override
    public void collisionSub(Collidable other) {
    }
}
