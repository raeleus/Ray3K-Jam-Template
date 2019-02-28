package com.ray3k.jam.timeline;

import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.ray3k.jam.screens.GameScreen;

/**
 *
 * @author raymond
 */
public class TextEvent implements TimelineEvent {
    private String text;
    private float time;
    private int words;
    private TemporalAction action;
    public static final float HANDICAP = 4;

    public TextEvent(String text, float time) {
        this.text = text;
        this.time = time + HANDICAP;
        words = text.split(" ").length;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    @Override
    public void execute() {
        var gameScreen = GameScreen.gameScreen;
        gameScreen.messageLabel.setText(text);
        gameScreen.progressBar.setAnimateDuration(0);
        gameScreen.progressBar.setValue(1);
        action = new TemporalAction(time) {
            @Override
            protected void update(float percent) {
                gameScreen.progressBar.setAnimateDuration(.1f);
                gameScreen.progressBar.setValue(1 - percent);
                gameScreen.time += getTime();
                gameScreen.score += time - getTime();
                gameScreen.words += words;
            }
        };
        gameScreen.actionsManager.addAction(action);
    }

    @Override
    public void end() {
        var gameScreen = GameScreen.gameScreen;
        gameScreen.actionsManager.removeAction(action);
        
        float difference = time - action.getTime();
        if (difference > 5) gameScreen.showAward('s');
        else if (difference > 4) gameScreen.showAward('a');
        else if (difference > 3) gameScreen.showAward('b');
        else if (difference > 2) gameScreen.showAward('c');
        else if (difference > 1) gameScreen.showAward('d');
        else gameScreen.showAward('f');
        
    }
}
