package com.myrpg.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class RunGame extends Game {
	SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();
		ScreenManager.getInstance().init(this, batch);
		ScreenManager.getInstance().swithScreen(ScreenManager.ScreenTypes.BATTLE);
	}

	@Override
	public void render () {
		float dt = Gdx.graphics.getDeltaTime();
		this.getScreen().render(dt);

	}

	@Override
	public void dispose () {
		batch.dispose();
	}
}
