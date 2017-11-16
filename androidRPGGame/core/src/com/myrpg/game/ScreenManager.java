package com.myrpg.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 хранилище всех глобальных настроек экранов
 */

public class ScreenManager {
    private RunGame game;
    private Viewport viewport;
    private BattleScreen battleScreen;
    public enum ScreenTypes{
        MENU, BATTLE
    }
    private static final ScreenManager ourInstance = new ScreenManager();

    public static ScreenManager getInstance() {
        return ourInstance;
    }

    public Viewport getViewport() {
        return viewport;
    }

    private ScreenManager() {
    }
    public void init(RunGame game, SpriteBatch batch){
        this.game = game;
        this.battleScreen = new BattleScreen(batch);
        // отвечает за пропорциональную отрисовку экрана
        this.viewport = new FitViewport(1920, 1080);
        this.viewport.update(1920, 1080, true);
        this.viewport.apply();
    }
    public void onResize(int width, int height){
        viewport.update(width, height, true);
        viewport.apply();
    }
    public void swithScreen(ScreenTypes type){
        Screen screen = game.getScreen();
        Assets.getInstance().clear();
        if(screen != null){
            screen.dispose();
        }
        switch (type){
            case BATTLE:
                Assets.getInstance().loadAssets(ScreenTypes.BATTLE);
                game.setScreen(battleScreen);
                break;
        }
    }
    public void dispose(){
        Assets.getInstance().clear();
        Assets.getInstance().getAssetManager().dispose();
    }

}
