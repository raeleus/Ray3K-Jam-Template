package com.ray3k.jam.timeline;

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
        
    }
}
