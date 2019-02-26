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

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;

/**
 *
 * @author Raymond
 */
public class SpineMap {
    private Array<ImageNode> imageNodes;
    private Array<BboxNode> bboxNodes;
    private Array<PointNode> pointNodes;
    private Array<PathNode> pathNodes;
    private Array<EventNode> eventNodes;
    private Array<BoneNode> boneNodes;

    public SpineMap(FileHandle levelFile) {
        imageNodes = new Array<>();
        bboxNodes = new Array<>();
        pointNodes = new Array<>();
        pathNodes = new Array<>();
        eventNodes = new Array<>();
        boneNodes = new Array<>();
        
        var jsonReader = new JsonReader();
        var json = jsonReader.parse(levelFile);
        
        //attachments
        if (json.has("skins")) for (var value : json.get("skins").get("default").iterator()) {
            value = value.child;
                
            switch(value.getString("type", "image")) {
                case "image":
                    var imageNode = new ImageNode();
                    imageNodes.add(imageNode);

                    imageNode.name = value.parent().name();
                    imageNode.x = value.getFloat("x", 0.0f);
                    imageNode.y = value.getFloat("y", 0.0f);
                    imageNode.rotation = value.getFloat("rotation", 0.0f);
                    imageNode.scaleX = value.getFloat("scaleX", 0.0f);
                    imageNode.scaleY = value.getFloat("scaleY", 0.0f);
                    imageNode.color = Color.valueOf(value.getString("color", "ffffffff"));
                    break;
                case "path":
                    var pathNode = new PathNode();
                    pathNodes.add(pathNode);
                    
                    pathNode.name = value.parent().name();
                    pathNode.closed = value.getBoolean("closed");
                    pathNode.vertices = value.get("vertices").asFloatArray();
                    pathNode.color = Color.valueOf(value.getString("color", "ff8000ff"));
                    break;
                case "boundingbox":
                    var bboxNode = new BboxNode();
                    bboxNodes.add(bboxNode);
                    
                    bboxNode.name = value.parent().name();
                    bboxNode.vertices = value.get("vertices").asFloatArray();
                    bboxNode.color = Color.valueOf(value.getString("color", "5ff000ff"));
                    break;
                case "point":
                    var pointNode = new PointNode();
                    pointNodes.add(pointNode);
                    
                    pointNode.name = value.parent().name();
                    pointNode.x = value.getFloat("x", 0.0f);
                    pointNode.y = value.getFloat("y", 0.0f);
                    pointNode.rotation = value.getFloat("rotation", 0.0f);
                    pointNode.color = Color.valueOf(value.getString("color", "f0f100ff"));
                    break;
            }
        }
        
        //events
        if (json.has("events")) for (var value : json.get("events").iterator()) {
            var eventNode = new EventNode();
            eventNodes.add(eventNode);
            
            eventNode.name = value.name();
            eventNode.intValue = value.getInt("int", 0);
            eventNode.floatValue = value.getFloat("float", 0.0f);
            eventNode.stringValue = value.getString("string", "");
        }
        
        //bones
        if (json.has("bones")) for (var value : json.get("bones").iterator()) {
            var boneNode = new BoneNode();
            boneNodes.add(boneNode);
            
            boneNode.name = value.getString("name", "bone");
            boneNode.parent = value.getString("parent", "root");
            boneNode.length = value.getFloat("length", 0.0f);
            boneNode.x = value.getFloat("x", 0.0f);
            boneNode.y = value.getFloat("y", 0.0f);
            boneNode.rotation = value.getFloat("rotation", 0.0f);
            boneNode.scaleX = value.getFloat("scaleX", 0.0f);
            boneNode.scaleY = value.getFloat("scaleY", 0.0f);
            boneNode.shearX = value.getFloat("shearX", 0.0f);
            boneNode.shearY = value.getFloat("shearY", 0.0f);
            boneNode.color = Color.valueOf(value.getString("color", "ffffffff"));
        }
    }

    public Array<ImageNode> getImageNodes() {
        return imageNodes;
    }

    public Array<BboxNode> getBboxNodes() {
        return bboxNodes;
    }

    public Array<PointNode> getPointNodes() {
        return pointNodes;
    }

    public Array<PathNode> getPathNodes() {
        return pathNodes;
    }

    public Array<EventNode> getEventNodes() {
        return eventNodes;
    }

    public Array<BoneNode> getBoneNodes() {
        return boneNodes;
    }
    
    public static class ImageNode {
        public String name;
        public float x;
        public float y;
        public float scaleX;
        public float scaleY;
        public float rotation;
        public Color color;

        @Override
        public String toString() {
            return "Image " + name + " x:" + x + " y:" + y + " scaleX:" + scaleX + " scaleY:" + scaleY + " rotation:" + rotation + " color:" + color;
        }
    }
    
    public static class BboxNode {
        public String name;
        public float[] vertices;
        public Color color;
        
        @Override
        public String toString() {
            return "Bbox " + name + " vertices:" + vertices + " color:" + color;
        }
    }
    
    public static class PointNode {
        public String name;
        public float x;
        public float y;
        public float rotation;
        public Color color;
        
        @Override
        public String toString() {
            return "Point " + name + " x:" + x + " y:" + y + " rotation:" + rotation + " color:" + color;
        }
    }
    
    public static class PathNode {
        public String name;
        public boolean closed;
        public float[] vertices;
        public Color color;
        
        @Override
        public String toString() {
            return "Path " + name + " closed:" + closed + " vertices:" + vertices + " color:" + color;
        }
    }
    
    public static class EventNode {
        public String name;
        public int intValue;
        public float floatValue;
        public String stringValue;
        
        @Override
        public String toString() {
            return "Event " + name + " intValue:" + intValue + " floatValue:" + floatValue + " stringValue:" + stringValue;
        }
    }
    
    public static class BoneNode {
        public String name;
        public String parent;
        public float length;
        public float rotation;
        public float x;
        public float y;
        public float scaleX;
        public float scaleY;
        public float shearX;
        public float shearY;
        public Color color;
        
        @Override
        public String toString() {
            return "Bone " + name + " parent:" + parent + " x:" + x + " y:" + y + " scaleX:" + scaleX + " scaleY:" + scaleY + " rotation:" + rotation + " shearX:" + shearX + " shearY:" + shearY + " length:" + length + " color:" + color;
        }
    }
}
