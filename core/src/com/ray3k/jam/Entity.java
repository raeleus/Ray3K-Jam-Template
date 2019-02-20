/*
 * The MIT License
 *
 * Copyright 2018 Raymond Buckley.
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

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.spine.utils.TwoColorPolygonBatch;

public abstract class Entity {
    private static final Vector2 temp = new Vector2();
    private final Vector2 position;
    private final Vector2 speed;
    private boolean destroyed;
    private final Vector2 gravity;
    private int depth;
    private boolean persistent;
    protected Core core;
    protected EntityManager entityManager;

    public Entity(Core core) {
        this.core = core;
        
        position = new Vector2();
        speed = new Vector2();
        gravity = new Vector2();
        depth = 0;
        destroyed = false;
        persistent = false;
    }
    
    public abstract void create();
    
    public abstract void act(float delta);
    
    public abstract void actEnd(float delta);
    
    public abstract void draw(TwoColorPolygonBatch batch, float delta);
    
    public abstract void destroyEvent();
    
    public void destroy() {
        if (!destroyed) {
            destroyed = true;
        }
    }

    public Vector2 getPosition() {
        return position.cpy();
    }
    
    public float getX() {
        return position.x;
    }
    
    public float getY() {
        return position.y;
    }
    
    public void setPosition(Vector2 position) {
        this.position.set(position);
    }
    
    public void setPosition(float x, float y) {
        setX(x);
        setY(y);
    }
    
    public void setX(float x) {
        this.position.x = x;
    }
    
    public void setY(float y) {
        this.position.y = y;
    }
    
    public void addX(float x) {
        this.position.x += x;
    }
    
    public void addY(float y) {
        this.position.y += y;
    }

    public float getSpeed() {
        return speed.len();
    }
    
    public float getXspeed() {
        return speed.x;
    }
    
    public float getYspeed() {
        return speed.y;
    }
    
    public void setSpeed(Vector2 speed) {
        this.speed.set(speed);
    }
    
    public void setXspeed(float x) {
        this.speed.x = x;
    }
    
    public void setYspeed(float y) {
        this.speed.y = y;
    }
    
    public void addXspeed(float x) {
        this.speed.x += x;
    }
    
    public void addYspeed(float y) {
        this.speed.y += y;
    }
    
    public void setMotion(float speed, float direction) {
        this.speed.set(speed, 0);
        this.speed.rotate(direction);
    }
    
    public void addMotion(float speed, float direction) {
        temp.set(speed, 0);
        temp.rotate(direction);
        this.speed.add(temp);
    }
    
    public float getDirection() {
        return this.speed.angle();
    }

    public boolean isDestroyed() {
        return destroyed;
    }
    
    public void setGravityX(float gravityX) {
        gravity.x = gravityX;
    }
    
    public void setGravityY(float gravityY) {
        gravity.y = gravityY;
    }
    
    public void setGravity(float speed, float direction) {
        gravity.set(speed, 0);
        gravity.rotate(direction);
    }
    
    public float getGravityX() {
        return gravity.x;
    }
    
    public float getGravityY() {
        return gravity.y;
    }
    
    public Vector2 getGravity() {
        return gravity.cpy();
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public boolean isPersistent() {
        return persistent;
    }

    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }
    
    public boolean moveTowards(float targetX, float targetY, float distance) {
        temp.set(targetX, targetY);
        temp.sub(getX(), getY());
        float dir = temp.angle();
        
        temp.set(distance, 0.0f);
        temp.rotate(dir);
        
        setPosition(getX() + temp.x, getY() + temp.y);
        
        temp.set(targetX, targetY);
        temp.sub(getX(), getY());
        
        boolean reachedDestination = !Maths.isEqual360(dir, temp.angle(), 30.0f);
        
        if (reachedDestination) {
            setPosition(targetX, targetY);
        }
        
        return reachedDestination;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
