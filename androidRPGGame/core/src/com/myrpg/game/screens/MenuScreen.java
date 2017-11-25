package com.myrpg.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
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
import com.myrpg.game.GameSesion;
import com.myrpg.game.ScreenManager;

/**
 * Created by Natallia && Anton on 18.11.2017.
 */

public class MenuScreen implements Screen {
    private Texture backgroundTexture;
    private TextureRegion buttonTexture;
    private BitmapFont font96;
    private BitmapFont font36;
    private Music music;
    private SpriteBatch batch;

    private Stage stage;
    private Skin skin;
    private float time;

    public MenuScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    @Override
    public void show() {
        backgroundTexture = Assets.getInstance().getAssetManager().get("BackgroundMenu.jpg", Texture.class);
        buttonTexture = Assets.getInstance().getAtlas().findRegion("MenuBtn");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Gagalin.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 96;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 1;
        parameter.shadowColor = Color.BLACK;
        parameter.shadowOffsetX = -3;
        parameter.shadowOffsetY = 3;
        parameter.color = Color.WHITE;
        font96 = generator.generateFont(parameter);
        parameter.size = 36;
       // font36 = font96;
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



        Button btnNewGame = new TextButton("START NEW GAME", skin, "tbs");
        Button btnContinueGame = new TextButton("CONTINUE GAME", skin, "tbs");
        Button btnExitGame = new TextButton("EXIT GAME", skin, "tbs");
        btnNewGame.setPosition(720-480, 600);
        btnContinueGame.setPosition(720-480, 480);
        btnExitGame.setPosition(720-480, 360);
        stage.addActor(btnNewGame);
        stage.addActor(btnContinueGame);
        stage.addActor(btnExitGame);
        btnNewGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameSesion.getInstance().startNewSession();
                ScreenManager.getInstance().swithScreen(ScreenManager.ScreenTypes.BATTLE);
            }
        });
        btnContinueGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameSesion.getInstance().loadSession();
                ScreenManager.getInstance().swithScreen(ScreenManager.ScreenTypes.BATTLE);
            }
        });
        btnExitGame.addListener(new ChangeListener() {
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
        font96.draw(batch, "No name game yet", 0, 800+20.f * (float)Math.sin(time), 1280, 1, false );
        batch.end();
        stage.draw();
    }

    public void update(float dt){
        time += dt;
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
//        music.dispose();
        backgroundTexture.dispose();
        font36.dispose();
        font96.dispose();
    }
}
