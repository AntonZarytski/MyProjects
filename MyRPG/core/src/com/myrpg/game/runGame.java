package com.myrpg.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class runGame extends Game {
	GameScreen gameScreen;
	SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();
		gameScreen = new GameScreen(batch);
		setScreen(gameScreen);
	}

	@Override
	public void render () {
		float dt = Gdx.graphics.getDeltaTime();
		this.getScreen().render(dt);

	}

	@Override
	public void dispose () {
		batch.dispose();
		gameScreen.dispose();
	}
}
