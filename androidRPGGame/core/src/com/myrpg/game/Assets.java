package com.myrpg.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Assets {
    private static final Assets ourInstance = new Assets();
    private AssetManager assetManager;//хранит ресурсы внутри себя и отдает их по ссылке
    private TextureAtlas atlas;

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public static Assets getInstance() {
        return ourInstance;
    }

    private Assets() {
        assetManager = new AssetManager();
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }
    public void loadAssets(ScreenManager.ScreenTypes type){
        switch (type){
            case MENU:
                assetManager.load("rpg.pack", TextureAtlas.class);
                assetManager.load("BackgroundMenu.jpg", Texture.class);
                assetManager.getProgress();
                assetManager.finishLoading();
                atlas = assetManager.get("rpg.pack", TextureAtlas.class);
                break;
            case BATTLE:
                assetManager.load("rpg.pack", TextureAtlas.class);
                assetManager.load("Field.jpg", Texture.class);
                assetManager.load("rpg.pack", TextureAtlas.class);
                assetManager.load("font.fnt", BitmapFont.class);
                assetManager.getProgress();
                assetManager.finishLoading();//ждет загрузки всех ресурсов(не кинет nullpointer)
                atlas = assetManager.get("rpg.pack", TextureAtlas.class);
                break;
            case LEVELEND:
                assetManager.load("rpg.pack", TextureAtlas.class);
                assetManager.load("BackgroundLevelEnd.jpg", Texture.class);
                assetManager.getProgress();
                assetManager.finishLoading();
                atlas = assetManager.get("rpg.pack", TextureAtlas.class);
        }
    }
    public void clear(){
        assetManager.clear();
    }
}
