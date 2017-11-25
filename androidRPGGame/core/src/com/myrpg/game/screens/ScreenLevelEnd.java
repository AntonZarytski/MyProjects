package com.myrpg.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.myrpg.game.Assets;
import com.myrpg.game.ScreenManager;


public class ScreenLevelEnd implements Screen {
    private Texture backgroundTexture;
    private TextureRegion buttonTexture;
    private BitmapFont font36;
    private SpriteBatch batch;

    private Stage stage;
    private Skin skin;

    public ScreenLevelEnd (SpriteBatch batch) {
        this.batch = batch;
    }

    @Override
    public void show() {
        backgroundTexture = Assets.getInstance().getAssetManager().get("BackgroundLevelEnd.jpg", Texture.class);
        buttonTexture = Assets.getInstance().getAtlas().findRegion("MenuBtn");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Gagalin.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 36;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 1;
        parameter.shadowColor = Color.BLACK;
        parameter.shadowOffsetX = -3;
        parameter.shadowOffsetY = 3;
        parameter.color = Color.WHITE;
        font36 = generator.generateFont(parameter);
        generator.dispose();
        stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        Gdx.input.setInputProcessor(stage);
        skin = new Skin();

        skin.addRegions(Assets.getInstance().getAtlas());
        skin.add("font36", font36);
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("MenuBtn");
        textButtonStyle.font = font36;
        skin.add("tbs", textButtonStyle);

        Button retryLevelBtn = new TextButton("Retry level", skin, "tbs");
        Button nextLevelBtn = new TextButton("Next level", skin, "tbs");
        Button exit = new TextButton("Exit", skin, "tbs");
        nextLevelBtn.setPosition(700, 480);
        retryLevelBtn.setPosition(700, 350);
        exit.setPosition(700, 100);
        stage.addActor(nextLevelBtn);
        stage.addActor(retryLevelBtn);
        stage.addActor(exit);
        nextLevelBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                BattleScreen.setGameLevel(BattleScreen.getGameLevel()+1);
                ScreenManager.getInstance().swithScreen(ScreenManager.ScreenTypes.BATTLE);
            }
        });

        retryLevelBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().swithScreen(ScreenManager.ScreenTypes.BATTLE);
            }
        });
        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

    }

    @Override
    public void render(float delta) {
        update(delta);
        batch.begin();
        batch.draw(backgroundTexture, 0,0 );
        batch.end();
        stage.draw();
    }

    public void update(float dt){
        stage.act(dt);
    }

    @Override
    public void resize(int width, int height) {
        ScreenManager.getInstance().onResize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();
        font36.dispose();
    }
}
