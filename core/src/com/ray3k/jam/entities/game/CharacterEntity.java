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
package com.ray3k.jam.entities.game;

import com.esotericsoftware.spine.utils.TwoColorPolygonBatch;
import com.ray3k.jam.Collidable;
import com.ray3k.jam.Core;
import com.ray3k.jam.SpineEntity;

/**
 *
 * @author raymond
 */
public class CharacterEntity extends SpineEntity {
    private float targetX, targetY;
    private boolean followTarget;
    private float followSpeed;
    private String name;
    private TargetListener targetListener;

    public CharacterEntity(Core core, String skeletonPath, String animation) {
        super(core, "spine/game/" + skeletonPath + ".json", animation);
        getSkeleton().setScale(.5f, .5f);
    }
    
    @Override
    public void create() {
        
    }

    @Override
    public void actSub(float delta) {
        if (followTarget) {
            if (moveTowards(targetX, targetY, followSpeed * delta)) {
                if (targetListener != null) {
                    targetListener.hit();
                }
                followTarget = false;
                targetListener = null;
            }
        }
    }

    @Override
    public void drawSub(TwoColorPolygonBatch spriteBatch, float delta) {
    }

    @Override
    public void collisionSub(Collidable other) {
    }

    @Override
    public void actEnd(float delta) {
    }

    @Override
    public void destroyEvent() {
    }
    
    public void followTarget(float targetX, float targetY, float followSpeed) {
        followTarget(targetX, targetY, followSpeed, null);
    }
    
    public void followTarget(float targetX, float targetY, float followSpeed, TargetListener targetListener) {
        followTarget = true;
        this.targetX = targetX;
        this.targetY = targetY;
        this.followSpeed = followSpeed;
        this.targetListener = targetListener;
    }
    
    public void stopFollowing() {
        followTarget = false;
        targetListener = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public static interface TargetListener {
        public void hit();
    }
}
