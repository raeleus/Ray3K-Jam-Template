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
        this(true, index);
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
                character.setName("player");
                gameScreen.entityManager.addEntity(character);
                character.getAnimationState().addAnimation(2, "blink", true, MathUtils.random(2));
                character.getSkeleton().setScaleX(-character.getSkeleton().getScaleX());
                character.setPosition(700, 265);
                character.getSkeleton().setSkin("clothed");
                
                //doctor
                character = new CharacterEntity(core, "soldier", "stand");
                character.setName("doctor");
                gameScreen.entityManager.addEntity(character);
                character.getAnimationState().addAnimation(2, "blink", true, MathUtils.random(2));
                character.setPosition(125, 250);
                character.getSkeleton().setSkin("doctor");
                
                //soldier
                character = new CharacterEntity(core, "soldier", "stand");
                character.setName("soldier");
                gameScreen.entityManager.addEntity(character);
                character.getAnimationState().addAnimation(2, "blink", true, MathUtils.random(2));
                character.getSkeleton().setScaleX(-character.getSkeleton().getScaleX());
                character.setPosition(950, 250);
                character.getSkeleton().setSkin("soldier");
                
                //chair
                character = new CharacterEntity(core, "chair", "animation");
                character.setName("chair");
                gameScreen.entityManager.addEntity(character);
                character.setPosition(450, 300);
                character.setDepth(100);
                break;
            case 1:
                //player
                character = gameScreen.findCharacter("player");
                character.getAnimationState().setAnimation(0, "walk", true);
                character.followTarget(429, character.getY(), 100, () -> {
                    var person = gameScreen.findCharacter("player");
                    person.getAnimationState().setAnimation(0, "stand", true);
                });
                
                //soldier
                character = gameScreen.findCharacter("soldier");
                character.getAnimationState().setAnimation(0, "walk", true);
                character.getAnimationState().addAnimation(1, "talking", true, 0);
                character.followTarget(600, character.getY(), 100, () -> {
                    var person = gameScreen.findCharacter("soldier");
                    person.getAnimationState().setAnimation(0, "stand", true);
                });
                break;
            case 2:
                //soldier
                character = gameScreen.findCharacter("soldier");
                character.getAnimationState().clearTrack(1);
                break;
            case 3:
                //doctor
                character = gameScreen.findCharacter("doctor");
                character.getAnimationState().addAnimation(1, "talking", true, 0);
                break;
            case 4:
                //doctor
                character = gameScreen.findCharacter("doctor");
                character.getAnimationState().clearTrack(1);
                break;
            case 5:
                //soldier
                character = gameScreen.findCharacter("soldier");
                character.getAnimationState().addAnimation(1, "talking", true, 0);
                break;
            case 6:
                //soldier
                character = gameScreen.findCharacter("soldier");
                character.getAnimationState().clearTrack(1);
                character.getAnimationState().setAnimation(0, "push", false);
                
                //player
                character = gameScreen.findCharacter("player");
                character.getAnimationState().addAnimation(0, "sit", false, 20/30f);
                break;
            case 7:
                //doctor
                character = gameScreen.findCharacter("doctor");
                character.getAnimationState().addAnimation(1, "talking", true, 0);
                break;
            case 8:
                //soldier
                character = gameScreen.findCharacter("soldier");
                character.getSkeleton().setScaleX(-character.getSkeleton().getScaleX());
                character.getAnimationState().setAnimation(0, "walk", true);
                character.followTarget(950, character.getY(), 100, () -> {
                    var person = gameScreen.findCharacter("soldier");
                    person.destroy();
                });
                break;
            case 9:
                //doctor
                character = gameScreen.findCharacter("doctor");
                character.getAnimationState().clearTrack(1);
                break;
            case 10:
                //player
                character = gameScreen.findCharacter("player");
                character.getAnimationState().addAnimation(1, "talking", true, 0);
                break;
            case 11:
                //player
                character = gameScreen.findCharacter("player");
                character.getAnimationState().clearTrack(1);
                break;
            case 12:
                //doctor
                character = gameScreen.findCharacter("doctor");
                character.getAnimationState().addAnimation(1, "talking", true, 0);
                break;
            case 13:
                //doctor
                character = gameScreen.findCharacter("doctor");
                character.getAnimationState().clearTrack(1);
                break;
            case 14:
                //player
                character = gameScreen.findCharacter("player");
                character.getAnimationState().addAnimation(1, "talking", true, 0);
                
                character.getAnimationState().addAnimation(3, "muffled", true, 3);
                
                //doctor
                character = gameScreen.findCharacter("doctor");
                character.getAnimationState().setAnimation(0, "stand", false);
                character.getAnimationState().addAnimation(0, "push", false, 2.75f);
                break;
            case 15:
                //player
                character = gameScreen.findCharacter("player");
                character.getAnimationState().clearTrack(1);
                break;
            case 16:
                //doctor
                character = gameScreen.findCharacter("doctor");
                character.getAnimationState().addAnimation(1, "talking", true, 0);
                break;
            case 17:
                //doctor
                character = gameScreen.findCharacter("doctor");
                character.getAnimationState().clearTrack(1);
                
                //player
                character = gameScreen.findCharacter("player");
                character.getAnimationState().addAnimation(1, "talking", true, 0);
                break;
            case 18:
                //player
                character = gameScreen.findCharacter("player");
                character.getAnimationState().clearTrack(1);
                break;
            case 19:
                //doctor
                character = gameScreen.findCharacter("doctor");
                character.getAnimationState().addAnimation(1, "talking", true, 0);
                break;
            case 20:
                //doctor
                character = gameScreen.findCharacter("doctor");
                character.getAnimationState().clearTrack(1);
                
                //player
                character = gameScreen.findCharacter("player");
                character.getAnimationState().addAnimation(1, "talking", true, 0);
                break;
            case 21:
                //player
                character = gameScreen.findCharacter("player");
                character.getAnimationState().clearTrack(1);
                
                //doctor
                character = gameScreen.findCharacter("doctor");
                character.getAnimationState().setAnimation(0, "push", false);
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
