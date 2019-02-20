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

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonBounds;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.utils.TwoColorPolygonBatch;

public abstract class SpineEntity extends Entity implements Collidable {

    private Skeleton skeleton;
    private AnimationState animationState;
    private SkeletonBounds skeletonBounds;
    private boolean checkingForCollisions;
    private boolean usePreciseCollisions;
    private Rectangle aabb;
    private Array<Collidable> collidableList;
    private Array<Polygon> polygons;
    private boolean updatedPolygons;

    public SpineEntity(Core core, String skeletonDataPath, String animation) {
        super(core);
        setSkeletonData(skeletonDataPath, animation);
        checkingForCollisions = false;
        usePreciseCollisions = false;
        aabb = new Rectangle();
        collidableList = new Array<>();
        polygons = new Array<>();
        updatedPolygons = false;
    }

    public void setSkeletonData(String skeletonDataPath, String animation) {
        SkeletonData skeletonData = core.assetManager.get(skeletonDataPath, SkeletonData.class);
        skeleton = new Skeleton(skeletonData);
        AnimationStateData animationStateData = new AnimationStateData(skeletonData);
        animationStateData.setDefaultMix(.25f);
        animationState = new AnimationState(animationStateData);
        animationState.setAnimation(0, animation, true);

        skeletonBounds = new SkeletonBounds();
        skeletonBounds.update(skeleton, true);
    }

    @Override
    public void act(float delta) {
        updatedPolygons = false;
        
        if (skeleton != null) {
            skeleton.setPosition(getX(), getY());
            animationState.update(delta);
            animationState.apply(skeleton);
            skeleton.updateWorldTransform();
            skeletonBounds.update(skeleton, true);
        }

        actSub(delta);
    }

    public abstract void actSub(float delta);

    @Override
    public void draw(TwoColorPolygonBatch batch, float delta) {
        if (batch != null && skeleton != null) {
            core.skeletonRenderer.draw(batch, skeleton);
        }

        drawSub(batch, delta);
    }

    public abstract void drawSub(TwoColorPolygonBatch spriteBatch, float delta);

    public Skeleton getSkeleton() {
        return skeleton;
    }

    public AnimationState getAnimationState() {
        return animationState;
    }

    public SkeletonBounds getSkeletonBounds() {
        return skeletonBounds;
    }

    @Override
    public boolean isCheckingForCollisions() {
        return checkingForCollisions;
    }

    @Override
    public void setCheckingForCollisions(boolean checkingForCollisions) {
        this.checkingForCollisions = checkingForCollisions;
    }
    
    @Override
    public Rectangle getAABB() {
        aabb.set(skeletonBounds.getMinX(), skeletonBounds.getMinY(), skeletonBounds.getWidth(), skeletonBounds.getHeight());
        return aabb;
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

    public boolean isUsePreciseCollisions() {
        return usePreciseCollisions;
    }

    public void setUsePreciseCollisions(boolean usePreciseCollisions) {
        this.usePreciseCollisions = usePreciseCollisions;
    }

    @Override
    public void collision(Collidable other) {
        if (!usePreciseCollisions || (other instanceof SpineEntity && preciseCollisionTest((SpineEntity) other))) {
            collisionSub(other);
        }
    }
    
    public abstract void collisionSub(Collidable other);
    
    public boolean preciseCollisionTest(SpineEntity other) {
        boolean returnValue = false;
        
        if (!updatedPolygons) {
            updatePolygons();
        }
        
        if (!other.updatedPolygons) {
            other.updatePolygons();
        }
        
        for (var polygon : polygons) {
            for (var otherPolygon : other.polygons) {
                if (Intersector.overlapConvexPolygons(polygon, otherPolygon)) {
                    returnValue = true;
                    break;
                }
            }
        }
        
        return returnValue;
    }
    
    private void updatePolygons() {
        polygons.clear();
        for (var floats : skeletonBounds.getPolygons()) {
            var points = core.triangulator.computeTriangles(floats);
            
            for (int i = 0; i < points.size; i += 3) {
                float[] verts = new float[6];
                for (int j = 0; j < 3; j++) {
                    verts[j*2] = floats.get((points.get(i + j) * 2) % floats.size);
                    verts[j*2+1] = floats.get((points.get(i + j) * 2 + 1) % floats.size);
                }
                
                polygons.add(new Polygon(verts));
            }
        }
        
        updatedPolygons = true;
    }
}
