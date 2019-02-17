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
package com.ray3k.jam.dialogs;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.ObjectIntMap;
import com.ray3k.jam.Core;
import com.ray3k.jam.Core.KeyAction;

/**
 *
 * @author Raymond
 */
public class KeyBindingDialog extends Dialog {
    private Skin skin;
    private Core core;
    private ObjectIntMap<KeyAction> keyBindings;

    public KeyBindingDialog(Core core) {
        super("Key Bindings", core.assetManager.get("ui/ui.json", Skin.class));
        skin = getSkin();
        this.core = core;
        
        keyBindings = new ObjectIntMap<>();
        copyKeyBindingsFromSource();
        populate();
    }
    
    private void populate() {
        var root = getContentTable();
        root.clear();
        
        var label = new Label("Action", skin);
        root.add(label);
        
        label = new Label("Key", skin);
        root.add(label);
        
        var enabled = true;
        
        for (var keyAction : Core.KeyAction.values()) {
            root.row();
            label = new Label(keyAction.displayName, skin);
            root.add(label);
            
            var textButton = new TextButton(Keys.toString(keyBindings.get(keyAction, Keys.UNKNOWN)), skin);
            if (keyBindings.get(keyAction, Keys.UNKNOWN) == Keys.UNKNOWN) {
                textButton.setText("Unbound");
                textButton.getLabel().setColor(Color.RED);
                enabled = false;
            }
            root.add(textButton).fillX();
            textButton.addListener(core.handListener);
            textButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    var dialog = new Dialog("", skin, "keybinding");
                    var label = new Label("Press a key for action " + keyAction.displayName + "...\nPress ESC to cancel...", skin);
                    label.setAlignment(Align.center);
                    dialog.getContentTable().add(label);
                    dialog.show(getStage());
                    dialog.addListener(new InputListener() {
                        @Override
                        public boolean keyDown(InputEvent event, int keycode) {
                            if (keycode != Keys.ESCAPE) {
                                removeDuplicates(keycode);
                                keyBindings.put(keyAction, keycode);
                                populate();
                            }
                            dialog.hide();
                            return true;
                        }
                        
                    });
                }
            });
        }
        
        root.row();
        var table = new Table();
        root.add(table).colspan(2);
        
        var textButton = new TextButton("OK", skin);
        textButton.setDisabled(!enabled);
        table.add(textButton).uniformX().fillX();
        if (enabled) textButton.addListener(core.handListener);
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                saveKeyBindings();
                hide();
            }
        });
        
        textButton = new TextButton("Defaults", skin);
        table.add(textButton).uniformX();
        textButton.addListener(core.handListener);
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                resetToDefaults();
                populate();
            }
        });
        
        textButton = new TextButton("Cancel", skin);
        table.add(textButton).uniformX();
        textButton.addListener(core.handListener);
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                hide();
            }
        });
    }
    
    private void resetToDefaults() {
        for (var keyAction : Core.KeyAction.values()) {
            keyBindings.put(keyAction, keyAction.getDefaultKey());
        }
    }
    
    private void copyKeyBindingsFromSource() {
        for (var keyAction : Core.KeyAction.values()) {
            keyBindings.put(keyAction, keyAction.getKey());
        }
    }
    
    private void saveKeyBindings() {
        for (var keyAction : Core.KeyAction.values()) {
            keyAction.setKey(keyBindings.get(keyAction, keyAction.getKey()));
        }
        
        Json json = new Json(JsonWriter.OutputType.json);
        Gdx.files.local(Core.KEY_BINDINGS_SAVE_NAME).writeString(json.toJson(keyBindings), false);
    }
    
    private void removeDuplicates(int keycode) {
        for (var keyAction : Core.KeyAction.values()) {
            if (keyBindings.get(keyAction, Keys.UNKNOWN) == keycode) {
                keyBindings.put(keyAction, Keys.UNKNOWN);
            }
        }
    }
}
