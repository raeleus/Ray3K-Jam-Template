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
package com.ray3k.jam.timeline;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.MathUtils;
import com.ray3k.jam.Core;
import com.ray3k.jam.entities.game.CharacterEntity;
import com.ray3k.jam.screens.GameScreen;

/**
 *
 * @author raymond
 */
public class AnimationEvent implements TimelineEvent {
    private int index;
    private Core core;
    private GameScreen gameScreen;
    private boolean proceedImmediately;

    public AnimationEvent(int index) {
        this(false, index);
    }
    
    public AnimationEvent(boolean proceedImmediately, int index) {
        this.index = index;
        gameScreen = GameScreen.gameScreen;
        core = gameScreen.core;
        this.proceedImmediately = proceedImmediately;
    }

    @Override
    public void execute() {
        switch (index) {
            case 0:
                //player
                var character = new CharacterEntity(core, "player", "stand");
                gameScreen.entityManager.addEntity(character);
                character.getAnimationState().addAnimation(2, "blink", true, MathUtils.random(2));
                character.getSkeleton().setScaleX(-character.getSkeleton().getScaleX());
                character.setPosition(700, 250);
                character.getSkeleton().setSkin("clothed");
                
                //doctor
                character = new CharacterEntity(core, "soldier", "stand");
                gameScreen.entityManager.addEntity(character);
                character.getAnimationState().addAnimation(2, "blink", true, MathUtils.random(2));
                character.setPosition(125, 250);
                character.getSkeleton().setSkin("doctor");
                
                //soldier
                character = new CharacterEntity(core, "soldier", "stand");
                gameScreen.entityManager.addEntity(character);
                character.getAnimationState().addAnimation(2, "blink", true, MathUtils.random(2));
                character.getSkeleton().setScaleX(-character.getSkeleton().getScaleX());
                character.setPosition(950, 250);
                character.getSkeleton().setSkin("soldier");
                
                //chair
                character = new CharacterEntity(core, "chair", "animation");
                gameScreen.entityManager.addEntity(character);
                character.setPosition(450, 300);
                
                break;
            default:
                break;
        }
        
        if (proceedImmediately) {
            gameScreen.queue.next();
        }
    }
    
    @Override
    public void step(float delta) {
        
    }

    @Override
    public void end() {
    }
    
}
