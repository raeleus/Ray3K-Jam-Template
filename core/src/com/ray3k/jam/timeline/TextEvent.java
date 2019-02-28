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

    public TextEvent(String text, float time) {
        this.text = text;
        this.time = time;
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
        gameScreen.actionsManager.addAction(new TemporalAction(time) {
            @Override
            protected void update(float percent) {
                gameScreen.progressBar.setAnimateDuration(.1f);
                gameScreen.progressBar.setValue(1 - percent);
            }
        });
    }
}
