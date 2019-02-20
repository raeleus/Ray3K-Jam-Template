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

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 *
 * @author Raymond
 */
public abstract class EntityCollidable extends Entity implements Collidable {
    private boolean checkingForCollisions;
    private Rectangle rect;
    private Array<Collidable> collidableList;

    public EntityCollidable(Core core) {
        super(core);
        checkingForCollisions = true;
        rect = new Rectangle();
        collidableList = new Array<>();
    }

    @Override
    public void act(float delta) {
        rect.setPosition(getX(), getY());
        
        actSub(delta);
    }
    
    public abstract void actSub(float delta);

    @Override
    public boolean isCheckingForCollisions() {
        return checkingForCollisions;
    }

    @Override
    public void setCheckingForCollisions(boolean checkingForCollisions) {
        this.checkingForCollisions = checkingForCollisions;
        if (checkingForCollisions) {
            entityManager.addCollidable(this);
        } else {
            entityManager.removeCollidable(this);
        }
    }

    @Override
    public Rectangle getAABB() {
        return rect;
    }

    @Override
    public void setCollidableList(Array<Collidable> collidableList) {
        this.collidableList.clear();
        this.collidableList.addAll(collidableList);
    }

    @Override
    public Array<Collidable> getCollidableList() {
        return collidableList;
    }
}
