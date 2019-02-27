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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ray3k.jam.Core;
import com.ray3k.jam.EntityManager;
import com.ray3k.jam.GameInputProcessor;
import com.ray3k.jam.JamScreen;
import com.ray3k.jam.timeline.TextEvent;
import com.ray3k.jam.timeline.TimelineEventQueue;

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
    private Label messageLabel;
    private Label typingLabel;
    private TimelineEventQueue queue;

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
        
        queue = new TimelineEventQueue();
        populateQueue();

        populateStage();
        Gdx.input.setInputProcessor(inputMultiplexer);
        
        processNextEvent();
    }

    @Override
    public void act(float delta) {
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

    private void populateStage() {
        var root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        root.pad(10);
        var table = new Table();
        table.setBackground(skin.getDrawable("black"));
        
        var scrollPane = new ScrollPane(table);
        scrollPane.setTouchable(Touchable.disabled);
        root.add(scrollPane).fill(1f, .5f).expand().bottom();
        
        messageLabel = new Label("", skin, "game");
        messageLabel.setColor(Color.RED);
        table.add(messageLabel).growX();
        
        table.row();
        typingLabel = new Label("", skin, "game");
        table.add(typingLabel).growX();
        
        stage.addListener(new InputListener() {
            @Override
            public boolean keyTyped(InputEvent event, char character) {
                var index = typingLabel.getText().length;
                if (index < messageLabel.getText().length) {
                    if (character == messageLabel.getText().charAt(index)) {
                        typingLabel.setText(typingLabel.getText() + Character.toString(character));
                        if (typingLabel.getGlyphLayout().width > typingLabel.getParent().getParent().getWidth() / 2f) {
                            messageLabel.setText(messageLabel.getText().substring(1));
                            typingLabel.setText(typingLabel.getText().substring(1));
                        }
                        
                        if (typingLabel.getText().length == messageLabel.getText().length) {
                            processNextEvent();
                        }
                    } else {
                        //error, subtract point
                    }
                }
                return super.keyTyped(event, character);
            }            
        });
    }
    
    public void populateQueue() {
        queue.add(new TextEvent("This is a typing game! Type the text that you see here to win!", 10));
        queue.add(new TextEvent("Fuck up or go too slow: see the consequences...", 10));
        queue.add(new TextEvent("Just keep it together, they don't have anything on you. They can't touch you.", 10));
        queue.add(new TextEvent("Don't say anything. They are just trying to scare you. I believe in you.", 10));
        queue.add(new TextEvent("Just hold it together a little longer and they'll realize that this has been a big mistake. You're innocent.", 10));
        queue.add(new TextEvent("This guy is a prick. Don't let him push you around.", 10));
        queue.add(new TextEvent("Like I said before, don't say anything.", 10));
        queue.add(new TextEvent("That's not exactly what I meant. Keep that sass to yourself.", 10));
        queue.add(new TextEvent("This isn't normal. You need to get out of here right now.", 10));
        queue.add(new TextEvent("He's distracted, wriggle out of those cuffs. These are a bunch of amateurs.", 10));
        queue.add(new TextEvent("This idiot is going to wish he didn't fuck with us. Get ready. We're almost out of here.", 10));
        queue.add(new TextEvent("This is a pathetic man. He couldn't stand up to you in a fair fight. Let's get you flat on your feet and you'll show him. Any time now... Hurry!", 10));
        queue.add(new TextEvent("God dammit, can you take any fucking longer? This guy is seriously on one. Take him out and head for the exit!", 10));
        queue.add(new TextEvent("That's fucking it. You're worthless. He's going to fucking rape you and you're just going to let it happen.", 10));
        queue.add(new TextEvent("JESUS CHRIST! If you don't do it. I will. I'm not going to let you fucking die. Not to this creep.", 10));
        queue.add(new TextEvent("Calling for help is pointless. I'm in control now.", 10));
        queue.add(new TextEvent("There is going to be a sharp pain in your wrist...", 10));
        queue.add(new TextEvent("I have you now you snivelling little shit.", 10));
        queue.add(new TextEvent("End him. He was going to kill you. He deserves it.", 10));
        queue.add(new TextEvent("They all deserve it. They were going to let you be raped and murdered!", 10));
        queue.add(new TextEvent("Let's kill all of these stupid bastards.", 10));
        queue.add(new TextEvent("They do not know who they are fucking with! Leave none living. Execute every single one of these rapists. These cowards.", 10));
        queue.add(new TextEvent("You wimp. Don't slow down. You must murderize these short-dicked mother fuckers.", 10));
        queue.add(new TextEvent("You're nowhere close to being done yet. Every life you take brings you closer to salvation.", 10));
        queue.add(new TextEvent("Kill him. He's the one that raped you. He's the one that killed you! Take his mana.", 10));
        queue.add(new TextEvent("Yes! Bathe in his blood. Take his power. Let him fill you. Do you feel his energy coursing through your veins?", 10));
        queue.add(new TextEvent("That's not the way. Go back and kill them. Kill all the men!", 10));
        queue.add(new TextEvent("No, no, NO! There is much killing left to be done. You will kill all of the sad fuckers in this building and you will drink their blood.", 10));
        queue.add(new TextEvent("You sorry little bitch. You don't have the balls to do it. You can't do anything without me allowing it. I am in control. There is no such thing as free will.", 10));
        queue.add(new TextEvent("A game by Raeleus.", 10));
        queue.add(new TextEvent("An exploration of identity, mana, and intrusive thoughts.", 10));
        queue.add(new TextEvent("And for this, I am very sorry. Thank you for playing.", 10));
    }
    
    private void processNextEvent() {
        var event = queue.next();
        
        if (event == null) {
            core.setScreen(new CreditsScreen(core));
        } else if (event instanceof TextEvent) {
            var textEvent = (TextEvent) event;
            messageLabel.setText(textEvent.getText());
            typingLabel.setText("");
        }
    }
}
