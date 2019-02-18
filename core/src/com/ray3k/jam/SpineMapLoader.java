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

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.ray3k.jam.SpineMapLoader.SpineMapParameter;

/**
 *
 * @author Raymond
 */
public class SpineMapLoader extends SynchronousAssetLoader<SpineMap, SpineMapParameter> {

    public SpineMapLoader(FileHandleResolver resolver) {
        super(resolver);
    }
    
    private SpineMap spineMap;
    
    public SpineMap getLoadedSpineMap() {
        return spineMap;
    }

    @Override
    public SpineMap load(AssetManager assetManager, String fileName, FileHandle file, SpineMapParameter parameter) {
        spineMap = new SpineMap(file);
        
        return spineMap;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, SpineMapParameter parameter) {
        return null;
    }
    
    static public class SpineMapParameter extends AssetLoaderParameters<SpineMap> {
    }
}
