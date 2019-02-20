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

import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.utils.TwoColorPolygonBatch;
import java.util.Iterator;

public class EntityManager {
    private final Array<Entity> entities;
    private final Array<Collidable> collidableList;
    
    public EntityManager() {
        entities = new Array<>();
        collidableList = new Array<>();
    }
    
    public void addEntity(Entity entity) {
        entities.add(entity);
        if (entity instanceof Collidable) {
            collidableList.add((Collidable) entity);
        }
        entity.setEntityManager(this);
        entity.create();
    }
    
    public Array<Entity> getEntities() {
        return new Array<>(entities);
    }
    
    public void act(float delta) {
        //act loop
        var iter = entities.iterator();
        while (iter.hasNext()) {
            var entity = iter.next();
            if (!entity.isDestroyed()) {
                entity.addXspeed(entity.getGravityX() * delta);
                entity.addYspeed(entity.getGravityY() * delta);
                
                entity.addX(entity.getXspeed() * delta);
                entity.addY(entity.getYspeed() * delta);
                
                entity.act(delta);
            } else {
                iter.remove();
            }
        }
        
        //update collision lists
        var collidableIter = collidableList.iterator();
        while (collidableIter.hasNext()) {
            var collidable = collidableIter.next();
            collidable.setCollidableList(collidableList);
            collidable.getCollidableList().removeValue(collidable, false);
        }
        
        //check collisions
        collidableIter = collidableList.iterator();
        while (collidableIter.hasNext()) {
            var collidable = collidableIter.next();
            
            for (var other : collidable.getCollidableList()) {                
                if (collidable.getAABB().overlaps(other.getAABB())) {
                    collidable.collision(other);
                    other.collision(collidable);

                    collidable.getCollidableList().removeValue(other, false);
                    other.getCollidableList().removeValue(collidable, false);
                }
            }
        }
        
        //destroy loop
        iter = entities.iterator();
        while (iter.hasNext()) {
            var entity = iter.next();
            if (entity.isDestroyed()) {
                entity.destroyEvent();
                iter.remove();
            }
        }
        
        //act end loop
        iter = entities.iterator();
        while (iter.hasNext()) {
            var entity = iter.next();
            entity.actEnd(delta);
        }
    }
    
    public void draw(TwoColorPolygonBatch batch, float delta) {
        entities.sort((Entity o1, Entity o2) -> o2.getDepth() - o1.getDepth());
        
        for (Entity entity : entities) {
            if (!entity.isDestroyed()) {
                entity.draw(batch, delta);
            }
        }
    }
    
    public void clear(boolean clearPersistent) {
        Iterator<Entity> iter = getEntities().iterator();
        
        while(iter.hasNext()) {
            Entity entity = iter.next();
            if (clearPersistent || !entity.isPersistent()) {
                entity.destroy();
            }
        }
    }
    
    public void clear() {
        clear(false);
    }
    
    @SuppressWarnings("unchecked")
    public <T extends Entity> T get(Class<T> clazz) {
        for (int i = 0; i < entities.size; i++) {
            Entity entity = entities.get(i);
            
            if (!entity.isDestroyed() && clazz.isInstance(entity)) {
                return (T) entity;
            }
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
    public <T> Array<T> getAll(Class<T> clazz) {
        Array<T> list = new Array<>();
        
        for (int i = 0; i < entities.size; i++) {
            Entity entity = entities.get(i);
            
            if (!entity.isDestroyed() && clazz.isInstance(entity)) {
                list.add((T) entity);
            }
        }
        
        return list;
    }
    
    public void addCollidable(Collidable collidable) {
        collidableList.add(collidable);
    }
    
    public void removeCollidable(Collidable collidable) {
        collidableList.removeValue(collidable, false);
    }

    public Array<Collidable> getCollidables() {
        return collidableList;
    }
}
