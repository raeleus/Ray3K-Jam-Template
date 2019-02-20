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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.IntArray;

/**
 *
 * @author Raymond
 */
public class GameInputProcessor implements InputProcessor {
    public float mouseX;
    public float mouseY;
    private IntArray keysJustPressed;
    private IntArray keySequence;
    private float keySequenceTimer;
    private float keySequenceCooldown;

    public GameInputProcessor() {
        keysJustPressed = new IntArray();
        keySequence = new IntArray();
        keySequenceCooldown = .25f;
    }

    @Override
    public boolean keyDown(int keycode) {
        keysJustPressed.add(keycode);
        keySequence.add(keycode);
        keySequenceTimer = keySequenceCooldown;
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        mouseX = screenX;
        mouseY = Gdx.graphics.getHeight() - screenY;
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        mouseX = screenX;
        mouseY = Gdx.graphics.getHeight() - screenY;
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
    
    public void act(float delta) {
        keysJustPressed.clear();
        
        keySequenceTimer -= delta;
        if (keySequenceTimer < 0) {
            keySequenceTimer = 0;
            keySequence.clear();
        }
    }
    
    public boolean isKeyJustPressed(int keycode) {
        if (keycode == Input.Keys.ANY_KEY) return keysJustPressed.size > 0;
        return keysJustPressed.contains(keycode);
    }

    public IntArray getKeySequence() {
        return keySequence;
    }
    
    public boolean keySequenceEndsWithCombo(IntArray combo) {
        var returnValue = false;
        
        if (combo.size <= keySequence.size) for (int i = 0; i < combo.size; i++) {
            if (combo.get(i) == keySequence.get(keySequence.size - combo.size + i)) {
                returnValue = true;
            } else {
                returnValue = false;
                break;
            }
        }
        
        return returnValue;
    }
}
