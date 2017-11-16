package com.myrpg.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Created by Natallia && Anton on 14.11.2017.
 */

public class Assets {
    private static final Assets ourInstance = new Assets();
    private AssetManager assetManager;//хранит ресурсы внутри себя и отдает их по ссылке

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
            case BATTLE:
                assetManager.load("Field.jpg", Texture.class);
                assetManager.load("AttackButton.png",Texture.class);
                assetManager.load("DefenceButton.png", Texture.class);
                assetManager.load("HealButton.png", Texture.class);
                assetManager.load("RegenerationButton.png", Texture.class);
                assetManager.load("Warrior.png", Texture.class);
                assetManager.load("WarriorDeath.png", Texture.class);
                assetManager.load("DD.png", Texture.class);
                assetManager.load("DDDeath.png", Texture.class);
                assetManager.load("Wizard.png", Texture.class);
                assetManager.load("WizardDeath.png", Texture.class);
                assetManager.load("Selector.png", Texture.class);
                assetManager.load("HealthLine.png", Texture.class);
                assetManager.load("HealthLineBack.png", Texture.class);
                assetManager.load("font.fnt", BitmapFont.class);
                assetManager.load("ActionPanel.png", Texture.class);
                assetManager.finishLoading();//ждет загрузки всех ресурсов(не кинет nullpointer)
                break;
        }
    }
    public void clear(){
        assetManager.clear();
    }
}
