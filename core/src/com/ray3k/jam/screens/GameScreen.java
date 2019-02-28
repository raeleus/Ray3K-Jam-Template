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
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ray3k.jam.Core;
import com.ray3k.jam.EntityManager;
import com.ray3k.jam.GameInputProcessor;
import com.ray3k.jam.JamScreen;
import com.ray3k.jam.timeline.AudioEvent;
import com.ray3k.jam.timeline.DelayEvent;
import com.ray3k.jam.timeline.GameEndEvent;
import com.ray3k.jam.timeline.TextEvent;
import com.ray3k.jam.timeline.TimelineEventQueue;

/**
 *
 * @author Raymond
 */
public class GameScreen extends JamScreen {

    public static GameScreen gameScreen;
    public Actor actionsManager;
    private EntityManager entityManager;
    private Stage stage;
    private Skin skin;
    private Core core;
    private OrthographicCamera gameCamera;
    private Viewport gameViewport;
    private InputMultiplexer inputMultiplexer;
    private GameInputProcessor gameInputProcessor;
    public Label messageLabel;
    public Label typingLabel;
    public TimelineEventQueue queue;
    public ProgressBar progressBar;
    public float score;
    public int characters;
    public float time;
    public int errors;

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
        
        queue.next();
        skin.getDrawable("progress-bar-red").setMinWidth(0);
        score = 0f;
        characters = 0;
        time = 0f;
        errors = 0;
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
        
        progressBar = new ProgressBar(0, 1, .0001f, false, skin);
        root.add(progressBar).expandY().bottom().growX();
        
        root.row();
        var scrollPaneTable = new Table();
        scrollPaneTable.setBackground(skin.getDrawable("black"));
        scrollPaneTable.pad(10);
        
        var scrollPane = new ScrollPane(scrollPaneTable);
        scrollPane.setTouchable(Touchable.disabled);
        root.add(scrollPane).height(200);
        
        messageLabel = new Label("", skin, "game");
        messageLabel.setColor(Color.RED);
        scrollPaneTable.add(messageLabel).growX().colspan(2);
        
        scrollPaneTable.row();
        typingLabel = new Label("", skin, "game");
        scrollPaneTable.add(typingLabel);
        
        var label = new Label("<", skin, "game");
        scrollPaneTable.add(label).expandX().left().spaceLeft(10);
        
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
                            messageLabel.setText("");
                            typingLabel.setText("");
                            queue.next();
                        }
                    } else {
                        ((TextEvent) queue.getCurrent()).error();
                    }
                }
                return super.keyTyped(event, character);
            }            
        });
    }
    
    public void populateQueue() {
        queue.add(new TextEvent("This is a typing game! Type the text that you see here to win!", 11));
        queue.add(new DelayEvent(1));
        queue.add(new TextEvent("Fuck up or go too slow: see the consequences...", 10));
        queue.add(new DelayEvent(1));
        queue.add(new TextEvent("Just keep it together, they don't have anything on you. They can't touch you.", 13));
        queue.add(new DelayEvent(1));
        queue.add(new GameEndEvent(core));
        queue.add(new AudioEvent("bgm/01.ogg", core));
        queue.add(new DelayEvent(1));
        queue.add(new TextEvent("Don't say anything. They are just trying to scare you. I believe in you.", 13));
        queue.add(new DelayEvent(1));
        queue.add(new AudioEvent("bgm/02.ogg", core));
        queue.add(new DelayEvent(1));
        queue.add(new TextEvent("Just hold it together a little longer and they'll realize that this has been a big mistake. You're innocent.", 17));
        queue.add(new DelayEvent(1));
        queue.add(new AudioEvent("bgm/03.ogg", core));
        queue.add(new DelayEvent(1));
        queue.add(new TextEvent("This guy is a prick. Don't let him push you around.", 9));
        queue.add(new DelayEvent(1));
        queue.add(new AudioEvent("bgm/04.ogg", core));
        queue.add(new DelayEvent(1));
        queue.add(new AudioEvent("bgm/05.ogg", core));
        queue.add(new DelayEvent(1));
        queue.add(new TextEvent("Like I said before, don't say anything.", 7));
        queue.add(new DelayEvent(1));
        queue.add(new AudioEvent("bgm/06.ogg", core));
        queue.add(new DelayEvent(1));
        queue.add(new TextEvent("That's not exactly what I meant. Keep that sass to yourself.", 10));
        queue.add(new DelayEvent(1));
        queue.add(new AudioEvent("bgm/07.ogg", core));
        queue.add(new DelayEvent(1));
        queue.add(new TextEvent("This isn't normal. You need to get out of here right now.", 9));
        queue.add(new DelayEvent(1));
        queue.add(new AudioEvent("bgm/08.ogg", core));
        queue.add(new DelayEvent(1));
        queue.add(new AudioEvent("bgm/08a.ogg", core));
        queue.add(new DelayEvent(1));
        queue.add(new TextEvent("He's distracted, wriggle out of those cuffs. These are a bunch of amateurs.", 12));
        queue.add(new DelayEvent(1));
        queue.add(new AudioEvent("bgm/09.ogg", core));
        queue.add(new DelayEvent(1));
        queue.add(new AudioEvent("bgm/09a.ogg", core));
        queue.add(new DelayEvent(1));
        queue.add(new TextEvent("This idiot is going to wish he didn't fuck with us. Get ready. We're almost out of here.", 14));
        queue.add(new DelayEvent(1));
        queue.add(new AudioEvent("bgm/10.ogg", core));
        queue.add(new DelayEvent(1));
        queue.add(new AudioEvent("bgm/10a.ogg", core));
        queue.add(new DelayEvent(1));
        queue.add(new AudioEvent("bgm/11.ogg", core));
        queue.add(new DelayEvent(1));
        queue.add(new TextEvent("This is a pathetic man. He couldn't stand up to you in a fair fight. Let's get you flat on your feet and you'll show him. Any time now... Hurry!", 24));
        queue.add(new DelayEvent(1));
        queue.add(new AudioEvent("bgm/12.ogg", core));
        queue.add(new DelayEvent(1));
        queue.add(new TextEvent("God dammit, can you take any fucking longer? This guy is seriously on one. Take him out and head for the exit!", 20));
        queue.add(new DelayEvent(1));
        queue.add(new AudioEvent("bgm/13.ogg", core));
        queue.add(new DelayEvent(1));
        queue.add(new TextEvent("That's fucking it. You're worthless. He's going to fucking rape you and you're just going to let it happen.", 19));
        queue.add(new DelayEvent(1));
        queue.add(new AudioEvent("bgm/14.ogg", core));
        queue.add(new DelayEvent(1));
        queue.add(new AudioEvent("bgm/15.ogg", core));
        queue.add(new DelayEvent(1));
        queue.add(new TextEvent("JESUS CHRIST! If you don't do it, I will. I'm not going to let you fucking die. Not to this creep.", 22));
        queue.add(new DelayEvent(1));
        queue.add(new AudioEvent("bgm/16.ogg", core));
        queue.add(new DelayEvent(1));
        queue.add(new AudioEvent("bgm/17.ogg", core));
        queue.add(new DelayEvent(1));
        queue.add(new TextEvent("Calling for help is pointless. I'm in control now.", 9));
        queue.add(new DelayEvent(1));
        queue.add(new AudioEvent("bgm/18.ogg", core));
        queue.add(new DelayEvent(1));
        queue.add(new TextEvent("There is going to be a sharp pain in your wrist...", 8));
        queue.add(new DelayEvent(1));
        queue.add(new AudioEvent("bgm/19.ogg", core));
        queue.add(new DelayEvent(1));
        queue.add(new TextEvent("I have you now you snivelling little shit.", 7));
        queue.add(new DelayEvent(1));
        queue.add(new AudioEvent("bgm/20.ogg", core));
        queue.add(new DelayEvent(1));
        queue.add(new TextEvent("End him. He was going to kill you. He deserves it.", 11));
        queue.add(new DelayEvent(1));
        queue.add(new AudioEvent("bgm/21.ogg", core));
        queue.add(new DelayEvent(1));
        queue.add(new TextEvent("They all deserve it. They were going to let you be raped and murdered!", 12));
        queue.add(new DelayEvent(1));
        queue.add(new AudioEvent("bgm/22.ogg", core));
        queue.add(new DelayEvent(1));
        queue.add(new TextEvent("Let's kill all of these stupid bastards.", 7));
        queue.add(new DelayEvent(1));
        queue.add(new AudioEvent("bgm/23.ogg", core));
        queue.add(new DelayEvent(1));
        queue.add(new AudioEvent("bgm/24.ogg", core));
        queue.add(new DelayEvent(1));
        queue.add(new TextEvent("They do not know who they are fucking with! Leave none living. Execute every single one of these rapists. These cowards.", 20));
        queue.add(new DelayEvent(1));
        queue.add(new AudioEvent("bgm/25.ogg", core));
        queue.add(new DelayEvent(1));
        queue.add(new TextEvent("You wimp. Don't slow down. You must murderize these short-dicked mother fuckers.", 16));
        queue.add(new DelayEvent(1));
        queue.add(new AudioEvent("bgm/26.ogg", core));
        queue.add(new DelayEvent(1));
        queue.add(new AudioEvent("bgm/27.ogg", core));
        queue.add(new DelayEvent(1));
        queue.add(new AudioEvent("bgm/28.ogg", core));
        queue.add(new DelayEvent(1));
        queue.add(new TextEvent("You're nowhere close to being done yet. Every life you take brings you closer to salvation.", 15));
        queue.add(new DelayEvent(1));
        queue.add(new AudioEvent("bgm/29.ogg", core));
        queue.add(new DelayEvent(1));
        queue.add(new TextEvent("Kill him. He's the one that raped you. He's the one that killed you! Take his mana.", 16));
        queue.add(new DelayEvent(1));
        queue.add(new AudioEvent("bgm/30.ogg", core));
        queue.add(new DelayEvent(1));
        queue.add(new TextEvent("Yes! Bathe in his blood. Take his power. Let him fill you. Do you feel his energy coursing through your veins?", 20));
        queue.add(new DelayEvent(1));
        queue.add(new TextEvent("That's not the way. Go back and kill them. Kill all the men!", 10));
        queue.add(new DelayEvent(1));
        queue.add(new AudioEvent("bgm/31.ogg", core));
        queue.add(new DelayEvent(1));
        queue.add(new TextEvent("No, no, NO! There is much killing left to be done. You will kill all of the sad fuckers in this building and you will drink their blood.", 23));
        queue.add(new DelayEvent(1));
        queue.add(new AudioEvent("bgm/32.ogg", core));
        queue.add(new DelayEvent(1));
        queue.add(new AudioEvent("bgm/33.ogg", core));
        queue.add(new DelayEvent(1));
        queue.add(new TextEvent("You sorry little bitch. You don't have the balls to do it. You can't do anything without me allowing it. I am in control. There is no such thing as free will.", 25));
        queue.add(new DelayEvent(1));
        queue.add(new AudioEvent("bgm/34.ogg", core));
        queue.add(new DelayEvent(1));
        queue.add(new TextEvent("A game by Raeleus.", 4));
        queue.add(new DelayEvent(1));
        queue.add(new TextEvent("An exploration of identity, mana, and intrusive thoughts.", 11));
        queue.add(new DelayEvent(1));
        queue.add(new TextEvent("And for this, I am very sorry. Thank you for playing.", 10));
        queue.add(new DelayEvent(1));
        queue.add(new GameEndEvent(core));
    }
    
    public void showAward(char grade) {
        var label = new Label("", skin, "award");
        
        var list = new Array<String>();
        switch (grade) {
            case 's':
                list.addAll("Stylish!", "Sexy!", "Super!", "Supreme!", "Saucy!", "Sassy!", "Suprising!", "Sweet!");
                label.setColor(Color.RED);
                break;
            case 'a':
                list.addAll("Ace!", "Awesome!", "Amazing!", "A+!", "Axel!", "Admirable!", "Authentic!");
                label.setColor(Color.GREEN);
                break;
            case 'b':
                list.addAll("Baby!", "Bold!", "Bright!", "Bustling!", "Bananas!", "Beautiful!", "Brazen!");
                label.setColor(Color.BLUE);
                break;
            case 'c':
                list.addAll("Calm", "Casual", "Crazy", "Cookie", "Civilian", "Contrary", "Clumsy");
                label.setColor(Color.ORANGE);
                break;
            case 'd':
                list.addAll("Demeaning", "Demoted", "Demonstrable", "Demonic", "Dumb", "Droopy");
                label.setColor(Color.BROWN);
                break;
            case 'f':
                list.addAll("Failure", "Fucked", "Fumbled", "Facetious", "False", "Fake", "Fallacious", "Fallen", "Farce", "Flagrant", "Flaky", "Floundering", "Forsaken");
                label.setColor(Color.LIGHT_GRAY);
                break;
            default:
                break;
        }
        
        label.setText(list.random());
        
        label.setPosition(0, stage.getHeight());
        stage.addActor(label);
        label.pack();
        
        label.addAction(Actions.sequence(Actions.moveBy(0, -label.getHeight(), .2f), Actions.delay(1), Actions.moveBy(0, label.getHeight(), .2f), Actions.removeActor()));
    }
}
