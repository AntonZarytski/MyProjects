package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGdxGame extends ApplicationAdapter {

	static SpriteBatch batch;
	Line line;
	Ball ball;

	
	@Override
	public void create () {
		batch = new SpriteBatch();
		line = new Line(new Texture("Line.png"), 640, 0, 600);
		ball = new Ball(new Texture("Ball.png"), 640, 300, 600);

	}
	@Override
	public void render () {
		float dt = Gdx.graphics.getDeltaTime();
		update(dt);
		Gdx.gl.glClearColor(1, 0, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		line.render(batch);
		ball.render(batch);
		batch.end();
	}

	public void update(float dt){
		line.update(dt);
		ball.update(dt);

	}

	@Override
	public void dispose () {
		batch.dispose();
	}
}
