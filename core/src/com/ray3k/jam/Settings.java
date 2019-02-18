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

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.ObjectMap;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Raymond
 */
public class Settings implements Preferences {
    private FileHandle file;
    private Json json;
    private ObjectMap<String, Object> objectMap;

    @SuppressWarnings("unchecked")
    public Settings(FileHandle file) {
        this.file = file;
        json = new Json(JsonWriter.OutputType.json);
        
        if (file.exists()) {
            objectMap = json.fromJson(ObjectMap.class, file);
        } else {
            objectMap = new ObjectMap<>();
        }
    }
    
    @Override
    public Settings putBoolean(String key, boolean val) {
        objectMap.put(key, val);
        return this;
    }

    @Override
    public Settings putInteger(String key, int val) {
        objectMap.put(key, val);
        return this;
    }

    @Override
    public Settings putLong(String key, long val) {
        objectMap.put(key, val);
        return this;
    }

    @Override
    public Settings putFloat(String key, float val) {
        objectMap.put(key, val);
        return this;
    }

    @Override
    public Settings putString(String key, String val) {
        objectMap.put(key, val);
        return this;
    }

    @Override
    public Settings put(Map<String, ?> vals) {
        for (var entry : vals.entrySet()) {
            objectMap.put(entry.getKey(), entry.getValue());
        }
        return this;
    }

    @Override
    public boolean getBoolean(String key) {
        return (boolean) objectMap.get(key);
    }

    @Override
    public int getInteger(String key) {
        return (int) objectMap.get(key);
    }

    @Override
    public long getLong(String key) {
        return (long) objectMap.get(key);
    }

    @Override
    public float getFloat(String key) {
        return (float) objectMap.get(key);
    }

    @Override
    public String getString(String key) {
        return (String) objectMap.get(key);
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        return (boolean) objectMap.get(key, defValue);
    }

    @Override
    public int getInteger(String key, int defValue) {
        return (int) objectMap.get(key, defValue);
    }

    @Override
    public long getLong(String key, long defValue) {
        return (long) objectMap.get(key, defValue);
    }

    @Override
    public float getFloat(String key, float defValue) {
        return (float) objectMap.get(key, defValue);
    }

    @Override
    public String getString(String key, String defValue) {
        return (String) objectMap.get(key, defValue);
    }

    @Override
    public Map<String, ?> get() {
        var returnValue = new HashMap<String, Object>();
        
        for (var entry : objectMap.iterator()) {
            returnValue.put(entry.key, entry.value);
        }
        
        return returnValue;
    }

    @Override
    public boolean contains(String key) {
        return objectMap.containsKey(key);
    }

    @Override
    public void clear() {
        objectMap.clear();
    }

    @Override
    public void remove(String key) {
        objectMap.remove(key);
    }

    @Override
    public void flush() {
        file.writeString(json.prettyPrint(objectMap), false);
    }
}
