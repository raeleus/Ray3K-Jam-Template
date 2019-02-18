package com.ray3k.jam;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.TimeUtils;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.esotericsoftware.spine.utils.TwoColorPolygonBatch;
import com.ray3k.jam.SkeletonDataLoader.SkeletonDataLoaderParameter;
import com.ray3k.jam.screens.LoadScreen;
import com.ray3k.jam.screens.LogoScreen;
import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Core extends Game {
    public static final String GAME_NAME = "Ray3K Jam";
    public static final String VERSION = "1";
    public static final String KEY_BINDINGS_SAVE_NAME = "keybindings";
    public static enum KeyAction {
        LEFT, RIGHT, UP, DOWN, SHOOT, BOMB, SHIELD;
        
        private int defaultKey;
        private int key = Keys.UNKNOWN;
        public String displayName;
        
        static {
            LEFT.defaultKey = Keys.LEFT;
            LEFT.displayName = "Left";
            
            RIGHT.defaultKey = Keys.RIGHT;
            RIGHT.displayName = "Right";
            
            UP.defaultKey = Keys.UP;
            UP.displayName = "Up";
            
            DOWN.defaultKey = Keys.DOWN;
            DOWN.displayName = "Down";
            
            SHOOT.defaultKey = Keys.Z;
            SHOOT.displayName = "Shoot";
            
            BOMB.defaultKey = Keys.X;
            BOMB.displayName = "Bomb";
            
            SHIELD.defaultKey = Keys.C;
            SHIELD.displayName = "Shield";
        }
        
        public int getDefaultKey() {
            return defaultKey;
        }
        
        public int getKey() {
            return key == Keys.UNKNOWN ? defaultKey : key;
        }
        
        public void setKey(int key) {
            this.key = key;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }
    
    public TwoColorPolygonBatch batch;
    public AssetManager assetManager;
    public SkeletonRenderer skeletonRenderer;
    public Preferences preferences;
    public DesktopWorker desktopWorker;
    public Actor actionsManager;
    
    private final static long MS_PER_UPDATE = 10;
    private long previous;
    private long lag;
    
    public HandListener handListener;
    public IbeamListener ibeamListener;
    
    @Override
    public void create() {
        previous = TimeUtils.millis();
        lag = 0;
            
        batch = new TwoColorPolygonBatch();
        skeletonRenderer = new SkeletonRenderer();
        skeletonRenderer.setPremultipliedAlpha(true);
        preferences = Gdx.app.getPreferences(GAME_NAME);
        
        loadKeyBindings();
        
        addAssets();
        
        setScreen(new LoadScreen(this, () -> {
            Core.this.setScreen(new LogoScreen(Core.this));
        }));
        
        actionsManager = new Actor();
        
        handListener = new HandListener();
        ibeamListener = new IbeamListener();
    }
    
    @SuppressWarnings("unchecked")
    private void loadKeyBindings() {
        var keyBindingsFile = Gdx.files.local(KEY_BINDINGS_SAVE_NAME);
        if (keyBindingsFile.exists()) {
            Json json = new Json(JsonWriter.OutputType.json);
            ObjectMap<String, Integer> keyBindings = json.fromJson(ObjectMap.class, Integer.class, keyBindingsFile);
            for (var keyAction : KeyAction.values()) {
                keyAction.setKey(keyBindings.get(keyAction.name(), Keys.UNKNOWN));
            }
        }
    }
    
    private void addAssets() {
        assetManager = new AssetManager(new InternalFileHandleResolver());
        assetManager.setLoader(SkeletonData.class, new SkeletonDataLoader(assetManager.getFileHandleResolver()));
        assetManager.setLoader(SpineMap.class, new SpineMapLoader(assetManager.getFileHandleResolver()));
        
        for (FileHandle fileHandle : getInternalFiles("ui")) {
            if (fileHandle.extension().toLowerCase(Locale.ROOT).equals("json")) {
                assetManager.load(fileHandle.path(), Skin.class);
                break;
            }
        }
        
        for (FileHandle atlasHandle : getInternalFiles("textures")) {
            if (atlasHandle.extension().toLowerCase(Locale.ROOT).equals("atlas")) {
                assetManager.load(atlasHandle.path(), TextureAtlas.class);
            }
        }
        
        for (var pack : getInternalFiles("spine")) {
            if (pack.isDirectory()) {
                for (FileHandle atlasHandle : pack.list()) {
                    if (atlasHandle.extension().toLowerCase(Locale.ROOT).equals("atlas")) {
                        assetManager.load(atlasHandle.path(), TextureAtlas.class);
                        
                        SkeletonDataLoaderParameter parameter = new SkeletonDataLoaderParameter(atlasHandle.path());
                        for (FileHandle fileHandle : pack.list()) {
                            if (fileHandle.extension().toLowerCase(Locale.ROOT).equals("json")) {
                                assetManager.load(fileHandle.path(), SkeletonData.class, parameter);
                            }
                        }
                        break;
                    }
                }
            }
        }
        
        for (FileHandle fileHandle : getInternalFiles("bgm")) {
            assetManager.load(fileHandle.path(), Music.class);
        }
        
        for (FileHandle fileHandle : getInternalFiles("sfx")) {
            assetManager.load(fileHandle.path(), Sound.class);
        }
        
        for (var fileHandle : getInternalFiles("levels")) {
            assetManager.load(fileHandle.path(), SpineMap.class);
        }
    }
    
    private Array<FileHandle> getInternalFiles(String internalFolder) {
        final Array<FileHandle> assetFiles = new Array<>();
        //list files if running from IDE
        assetFiles.addAll(Gdx.files.internal(internalFolder).list());
        
        //list files if running from JAR
        if (assetFiles.size == 0) {
            ZipInputStream zip = null;
            try {
                CodeSource src = getClass().getProtectionDomain().getCodeSource();
                URL jar = src.getLocation();
                zip = new ZipInputStream(jar.openStream());
                while (true) {
                    ZipEntry e = zip.getNextEntry();
                    if (e == null) {
                        break;
                    }
                    final java.lang.String name = e.getName();
                    if (name.matches(internalFolder + "\\/.+")) {
                        com.badlogic.gdx.files.FileHandle fileHandle = Gdx.files.internal(name);
                        assetFiles.add(fileHandle);
                        //internal files don't report if it's a directory, remove parent directories
                        assetFiles.removeValue(fileHandle.parent(), false);
                    }
                }
            } catch (IOException ex) {
                Gdx.app.error(getClass().getName(), "Error reading assets from JAR", ex);
            } finally {
                try {
                    if (zip != null) {
                        zip.close();
                    }
                } catch (IOException ex) {
                    Gdx.app.error(getClass().getName(), "Error reading assets from JAR", ex);
                }
            }
        }
        
        return assetFiles;
    }

    @Override
    public void render() {
        
        if (screen != null) {
            if (screen instanceof JamScreen) {
                var jamScreen = (JamScreen) screen;
                long current = TimeUtils.millis();
                long elapsed = current - previous;
                previous = current;
                lag += elapsed;

                while (lag >= MS_PER_UPDATE) {
                    var delta = MS_PER_UPDATE / 1000.0f;
                    actionsManager.act(delta);
                    jamScreen.act(delta);
                    lag -= MS_PER_UPDATE;
                }
                
                jamScreen.draw(lag / MS_PER_UPDATE);
            } else {
                var delta = Gdx.graphics.getDeltaTime();
                actionsManager.act(delta);
                screen.render(delta);
            }
        }
    }

    @Override
    public void dispose() {
        
    }
    
    public long playSound(String name, float volume) {
        return playSound(name, volume, 1, 1);
    }
    
    public long playSound(String name, float volume, float pitch) {
        return playSound(name, volume, pitch, 1);
    }
    
    public long playSound(String name, float volume, float pitch, float pan) {
        return assetManager.get("sfx/" + name, Sound.class).play(volume, pitch, pan);
    }
}
