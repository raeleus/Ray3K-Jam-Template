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
package com.ray3k.jam;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;

/**
 *
 * @author Raymond
 */
public class SoundFader {
    private float life;
    private float duration;
    private Sound sound;
    private long instance;
    private SoundFadeListener listener;

    public SoundFader(float duration, Sound sound, long instance, SoundFadeListener listener) {
        this.life = 0.0f;
        this.duration = duration;
        this.sound = sound;
        this.instance = instance;
        this.listener = listener;
    }
    
    public void update(float delta) {
        life += delta;
        life = MathUtils.clamp(life, 0, duration);
        
        sound.setVolume(instance, 1f - Interpolation.circleOut.apply(life / duration));
        
        if (MathUtils.isEqual(life, duration) && listener != null) {
            sound.stop(instance);
            listener.complete();
        }
    }
    
    public static interface SoundFadeListener {
        public void complete();
    }
}
