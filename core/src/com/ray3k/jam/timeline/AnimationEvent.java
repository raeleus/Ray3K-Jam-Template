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
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
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
                
                //doctor
                character = gameScreen.findCharacter("doctor");
                character.getAnimationState().setAnimation(0, "walk", true);
                character.followTarget(character.getX() + 100, character.getY(), 40, () -> {
                    //doctor
                    var doctor = gameScreen.findCharacter("doctor");
                    doctor.getAnimationState().setAnimation(0, "push", false);
                    gameScreen.actionsManager.addAction(Actions.delay(1, new Action() {
                        @Override
                        public boolean act(float delta) {
                            doctor.getAnimationState().setAnimation(0, "walk", true);
                            doctor.followTarget(doctor.getX() - 50, doctor.getY(), 50, () -> {
                                doctor.getAnimationState().setAnimation(0, "stand", true);
                            });
                            return true;
                        }
                    }));
                    
                    //player
                    var player = gameScreen.findCharacter("player");
                    player.getAnimationState().setAnimation(3, "muffled", false);
                });
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
                character.getAnimationState().setAnimation(0, "slap", false);
                character.getAnimationState().addAnimation(1, "talking", true, 0);
                break;
            case 22:
                //player
                character = gameScreen.findCharacter("player");
                character.getAnimationState().setAnimation(1, "talking", false);
                
                //doctor
                character = gameScreen.findCharacter("doctor");
                character.getAnimationState().clearTrack(1);
                break;
            case 23:
                //doctor
                character = gameScreen.findCharacter("doctor");
                character.getAnimationState().addAnimation(1, "talking", true, 0);
                break;
            case 24:
                //doctor
                character = gameScreen.findCharacter("doctor");
                character.getAnimationState().clearTrack(1);
                break;
            case 25:
                //doctor
                character = gameScreen.findCharacter("doctor");
                character.getAnimationState().addAnimation(1, "talking", true, 0);
                break;
            case 26:
                proceedImmediately = false;
                
                //doctor
                character = gameScreen.findCharacter("doctor");
                character.getAnimationState().clearTrack(1);
                character.getAnimationState().setAnimation(0, "walk", true);
                character.followTarget(character.getX() + 50, character.getY(), 60, () -> {
                    //doctor
                    var doctor = gameScreen.findCharacter("doctor");
                    doctor.getAnimationState().setAnimation(0, "push", false);
                    gameScreen.actionsManager.addAction(Actions.delay(1, new Action() {
                        @Override
                        public boolean act(float delta) {
                            doctor.getAnimationState().setAnimation(0, "walk", true);
                            doctor.followTarget(doctor.getX() - 50, doctor.getY(), 50, () -> {
                                doctor.getAnimationState().setAnimation(0, "stand", true);
                            });
                            return true;
                        }
                    }));
                    
                    gameScreen.actionsManager.addAction(Actions.delay(.25f, new Action() {
                        @Override
                        public boolean act(float delta) {
                            //player
                            var character = gameScreen.findCharacter("player");
                            character.getSkeleton().setSkin("nude");
                            gameScreen.queue.next();
                            return true;
                        }
                    }));
                });
                
                break;
            case 27:
                //doctor
                character = gameScreen.findCharacter("doctor");
                character.getAnimationState().addAnimation(1, "talking", true, 0);
                break;
            case 28:
                proceedImmediately = false;
                //doctor
                character = gameScreen.findCharacter("doctor");
                character.getAnimationState().clearTrack(1);
                character.getAnimationState().setAnimation(0, "walk", true);
                character.followTarget(character.getX() + 50, character.getY(), 60, () -> {
                    //doctor
                    var doctor = gameScreen.findCharacter("doctor");
                    doctor.getAnimationState().setAnimation(0, "aim-knife", false);
                    doctor.getAnimationState().addAnimation(0, "shoot-knife", false, 1);
                    gameScreen.actionsManager.addAction(Actions.delay(3, new Action() {
                        @Override
                        public boolean act(float delta) {
                            doctor.getAnimationState().setAnimation(0, "walk", true);
                            doctor.followTarget(doctor.getX() - 50, doctor.getY(), 50, () -> {
                                doctor.getAnimationState().setAnimation(0, "stand", true);
                            });
                            return true;
                        }
                    }));

                    gameScreen.actionsManager.addAction(Actions.delay(1.25f, new Action() {
                        @Override
                        public boolean act(float delta) {
                            //player
                            var character = gameScreen.findCharacter("player");
                            character.getAnimationState().setAnimation(3, "unmuffled", false);

                            gameScreen.queue.next();
                            return true;
                        }
                    }));
                });
                break;
            case 29:
                break;
            case 30:
                //doctor
                character = gameScreen.findCharacter("doctor");
                character.getAnimationState().addAnimation(1, "talking", true, 0);
                break;
            case 31:
                //doctor
                character = gameScreen.findCharacter("doctor");
                character.getAnimationState().clearTrack(1);
                
                //player
                character = gameScreen.findCharacter("player");
                character.getAnimationState().addAnimation(1, "talking", true, 0);
                break;
                
            case 32:
                //player
                character = gameScreen.findCharacter("player");
                character.getAnimationState().clearTrack(1);
                break;
            case 33:
                //doctor
                character = gameScreen.findCharacter("doctor");
                character.getAnimationState().addAnimation(1, "talking", true, 0);
                character.getAnimationState().setAnimation(0, "walk", true);
                character.followTarget(character.getX() + 50, character.getY(), 60, () -> {
                    //doctor
                    var doctor = gameScreen.findCharacter("doctor");
                    doctor.getAnimationState().setAnimation(0, "push", false);
                    gameScreen.actionsManager.addAction(Actions.delay(1, new Action() {
                        @Override
                        public boolean act(float delta) {
                            doctor.getAnimationState().setAnimation(0, "walk", true);
                            doctor.followTarget(doctor.getX() - 50, doctor.getY(), 50, () -> {
                                doctor.getAnimationState().setAnimation(0, "stand", true);
                            });
                            return true;
                        }
                    }));
                    
                    //player
                    var player = gameScreen.findCharacter("player");
                    player.getAnimationState().setAnimation(3, "muffled", false);
                });
                break;
            case 34:
                //doctor
                character = gameScreen.findCharacter("doctor");
                character.getAnimationState().clearTrack(1);
                break;
            case 35:
                proceedImmediately = false;
                //doctor
                character = gameScreen.findCharacter("doctor");
                character.getAnimationState().setAnimation(0, "walk", true);
                character.followTarget(412, character.getY(), 100, () -> {
                    var doctor = gameScreen.findCharacter("doctor");
                    doctor.getAnimationState().setAnimation(0, "lie", false);
                    doctor.getAnimationState().addAnimation(1, "talking", true, 10 / 30);
                    gameScreen.queue.next();
                    
                    var player = gameScreen.findCharacter("player");
                    player.getAnimationState().setAnimation(0, "lie", false);
                });
                break;
            case 36:
                //doctor
                character = gameScreen.findCharacter("doctor");
                character.getAnimationState().clearTrack(1);
                break;
            case 37:
                proceedImmediately = false;
                character = gameScreen.findCharacter("player");
                character.getAnimationState().setAnimation(0, "lie-punch", false);
                character.getAnimationState().addAnimation(0, "stand", false, 0);
                character.getAnimationState().addAnimation(0, "aim-knife", false, .25f);
                character.getAnimationState().addAnimation(0, "shoot-knife", false, .7f);
                
                //doctor
                character = gameScreen.findCharacter("doctor");
                character.getAnimationState().setAnimation(0, "stand", false);
                character.followTarget(200, character.getY(), 200, () -> {
                    var doctor = gameScreen.findCharacter("doctor");
                    doctor.getAnimationState().addAnimation(1, "talking", true, 1);
                    
                    gameScreen.queue.next();
                });
                break;
            case 38:
                //doctor
                character = gameScreen.findCharacter("doctor");
                character.getAnimationState().clearTracks();
                character.getAnimationState().setAnimation(0, "die", false);
                break;
            case 39:
                proceedImmediately = false;
                
                //player
                character = gameScreen.findCharacter("player");
                character.getAnimationState().setAnimation(0, "run", true);
                character.followTarget(-150, character.getY(), 200, () -> {
                    gameScreen.queue.next();
                });
                break;
            case 40:
                //player
                character = gameScreen.findCharacter("player");
                character.getSkeleton().setSkin("bodyarmor");
                character.getSkeleton().setScaleX(-character.getSkeleton().getScaleX());
                character.followTarget(950, character.getY(), 200, () -> {
                    var entity = gameScreen.findCharacter("player");
                    entity.destroy();
                    
                    entity = gameScreen.findCharacter("chair");
                    entity.destroy();
                    
                    entity = gameScreen.findCharacter("doctor");
                    entity.destroy();
                });
                break;
            case 41:
                proceedImmediately = false;
                
                character = new CharacterEntity(core, "player", "stand");
                character.setName("player");
                gameScreen.entityManager.addEntity(character);
                character.getAnimationState().setAnimation(0, "run", true);
                character.getAnimationState().addAnimation(2, "blink", true, MathUtils.random(2));
                character.getAnimationState().setAnimation(3, "aim-c4", true);
                character.setPosition(-150, 265);
                character.getSkeleton().setSkin("bodyarmor");
                character.followTarget(350, character.getY(), 200, () -> {
                    var character1 = gameScreen.findCharacter("player");
                    character1.getAnimationState().setAnimation(0, "stand", true);
                    character1.getAnimationState().clearTrack(3);
                    character1.getAnimationState().addAnimation(0, "shoot-c4", false, 1.5f);
                    
                    gameScreen.actionsManager.addAction(Actions.delay(2, new Action() {
                        @Override
                        public boolean act(float delta) {
                            var character2 = gameScreen.findCharacter("player");
                            character2.getSkeleton().setScale(-character2.getSkeleton().getScaleX(), character2.getSkeleton().getScaleY());
                            character2.getAnimationState().setAnimation(0, "run", true);
                            character2.followTarget(-150, character2.getY(), 200);
                            
                            var character3 = gameScreen.findCharacter("door");
                            character3.getAnimationState().setAnimation(0, "c4", false);
                            
                            gameScreen.queue.next();
                            return true;
                        }
                    }));
                });
                
                character = new CharacterEntity(core, "door", "animation");
                character.setName("door");
                gameScreen.entityManager.addEntity(character);
                character.getSkeleton().setSkin("door");
                character.getSkeleton().setScale(.35f, .35f);
                character.setPosition(400, 300);
                character.setDepth(100);

                break;
            case 42:
                break;
            case 43:
                break;
            case 44:
                proceedImmediately = false;
                
                character = gameScreen.findCharacter("door");
                character.getAnimationState().setAnimation(0, "explode", false);
                
                //player
                character = gameScreen.findCharacter("player");
                character.getSkeleton().setScaleX(-character.getSkeleton().getScaleX());
                character.getAnimationState().setAnimation(0, "run", true);
                character.followTarget(400, character.getY(), 200, () -> {
                    var character2 = gameScreen.findCharacter("player");
                    character2.getAnimationState().setAnimation(0, "stand", true);
                    character2.getAnimationState().setAnimation(1, "aim-rifle", false);
                    character2.getAnimationState().addAnimation(0, "run", true, 2);
                    character2.getAnimationState().addAnimation(3, "door-exit", false, 2);
                    
                    gameScreen.actionsManager.addAction(Actions.delay(4, new Action() {
                        @Override
                        public boolean act(float delta) {
                            var character2 = gameScreen.findCharacter("door");
                            character2.setX(200);
                            character2.followTarget(-200, character2.getY(), 200);
                            
                            //soldier
                            character2 = new CharacterEntity(core, "soldier", "dead");
                            character2.setName("soldier");
                            gameScreen.entityManager.addEntity(character2);
                            character2.getSkeleton().setScaleX(-character2.getSkeleton().getScaleX());
                            character2.setPosition(300, 250);
                            character2.getSkeleton().setSkin("soldier");
                            character2.followTarget(-400, character2.getY(), 200);
                            
                            character2 = gameScreen.findCharacter("player");
                            character2.setX(200);
                            character2.getAnimationState().setAnimation(3, "door-enter", false);
                            
                            gameScreen.queue.next();
                            return true;
                        }
                    }));
                });
                break;
            case 45:
                character = gameScreen.findCharacter("door");
                character.destroy();
                
                character = gameScreen.findCharacter("soldier");
                character.destroy();
                break;
            case 46:
                break;
            case 47:
                character = gameScreen.findCharacter("player");
                character.getAnimationState().setAnimation(3, "shoot-rifle", true);
                break;
            case 48:
                break;
            case 49:
                break;
            case 51:
                break;
            case 52:
                proceedImmediately = false;
                
                //officer
                character = new CharacterEntity(core, "soldier", "crawling");
                character.setName("officer");
                gameScreen.entityManager.addEntity(character);
                character.getSkeleton().setScaleX(-character.getSkeleton().getScaleX());
                character.setPosition(950, 200);
                character.getSkeleton().setSkin("officer");
                character.followTarget(500, character.getY(), 250, () -> {
                    var character2 = gameScreen.findCharacter("officer");
                    character2.getAnimationState().setAnimation(0, "crawling-die", false);
                    character2.followTarget(-250, character2.getY(), 200, () -> {
                        var character3 = gameScreen.findCharacter("officer");
                        character3.destroy();
                        
                        character3 = gameScreen.findCharacter("player");
                        character3.getAnimationState().setAnimation(0, "stand", true);
                        character3.getAnimationState().setAnimation(3, "disarm-rifle", false);
                        
                        //soldier
                        character3 = new CharacterEntity(core, "soldier", "run");
                        character3.setName("soldier");
                        gameScreen.entityManager.addEntity(character3);
                        character3.getAnimationState().addAnimation(2, "blink", true, MathUtils.random(2));
                        character3.getSkeleton().setScaleX(-character3.getSkeleton().getScaleX());
                        character3.setPosition(950, 250);
                        character3.getSkeleton().setSkin("soldier");
                        character3.followTarget(650, character3.getY(), 100, () -> {
                            var character4 = gameScreen.findCharacter("soldier");
                            character4.getAnimationState().setAnimation(0, "stand", true);
                            character4.getAnimationState().setAnimation(1, "talking", true);
                            gameScreen.queue.next();
                            
                            character4 = gameScreen.findCharacter("player");
                            character4.getAnimationState().setAnimation(3, "aim-pistol", true);
                        });
                    });
                });
                
                break;
            case 53:
                character = gameScreen.findCharacter("soldier");
                character.getAnimationState().clearTrack(1);
                
                break;
            case 54:
                character = gameScreen.findCharacter("player");
                character.getAnimationState().setAnimation(1, "talking", true);
                
                break;
            case 55:
                proceedImmediately = false;
                
                character = gameScreen.findCharacter("player");
                character.getAnimationState().clearTrack(1);
                character.getAnimationState().setAnimation(3, "shoot-pistol", false);
                
                character = gameScreen.findCharacter("soldier");
                character.getAnimationState().clearTracks();
                character.getAnimationState().setAnimation(0, "die", false);
                
                gameScreen.actionsManager.addAction(Actions.delay(2, new Action() {
                    @Override
                    public boolean act(float delta) {
                        var character = gameScreen.findCharacter("player");
                        character.getAnimationState().setAnimation(0, "run", true);
                        
                        character = gameScreen.findCharacter("soldier");
                        character.followTarget(-400, character.getY(), 200);
                        gameScreen.queue.next();
                        return true;
                    }
                }));
                
                break;
            case 56:
                proceedImmediately = false;
                character = gameScreen.findCharacter("player");
                character.followTarget(400, character.getY(), 50, () -> {
                    var character2 = gameScreen.findCharacter("player");
                    character2.getAnimationState().setAnimation(0, "stand", true);
                    character2.getAnimationState().setAnimation(3, "idle-pistol", false);
                    gameScreen.queue.next();
                });
                
                character = new CharacterEntity(core, "door", "animation");
                character.setName("door");
                gameScreen.entityManager.addEntity(character);
                character.getSkeleton().setSkin("open");
                character.getSkeleton().setScale(.35f, .35f);
                character.setPosition(1000, 300);
                character.setDepth(100);
                character.followTarget(400, character.getY(), 150);
                break;
            case 57:
                character = gameScreen.findCharacter("player");
                character.getAnimationState().setAnimation(1, "talking", true);
                break;
            case 58:
                character = gameScreen.findCharacter("player");
                character.getAnimationState().clearTrack(1);
                character.getAnimationState().setAnimation(3, "disarm-pistol", false);
                break;
            case 59:
                character = gameScreen.findCharacter("player");
                character.getAnimationState().setAnimation(1, "talking", true);
                break;
            case 60:
                character = gameScreen.findCharacter("player");
                character.getAnimationState().clearTrack(1);
                character.getAnimationState().setAnimation(0, "aim-suicide", false);
                break;
            case 61:
                break;
            case 62:
                break;
            case 63:
                proceedImmediately = false;
                
                character = gameScreen.findCharacter("player");
                character.getAnimationState().clearTracks();
                character.getAnimationState().setAnimation(0, "die", false);
                
                gameScreen.actionsManager.addAction(Actions.delay(3, new Action() {
                    @Override
                    public boolean act(float delta) {
                        gameScreen.queue.next();
                        return true;
                    }
                }));
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
