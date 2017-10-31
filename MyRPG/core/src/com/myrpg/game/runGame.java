package com.myrpg.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class runGame extends ApplicationAdapter {
	public SpriteBatch batch;
	Background background;
	Hero hero;
	Enemy monster;
	Person currentUnit;
	HealthLine[] hl;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Background();
		hero = new Hero();
		hero.setPosition(new Vector2(200, 50));
		monster = new Enemy();
		monster.setPosition(new Vector2(700, 50));
		currentUnit = hero;
		hl = new HealthLine[99];
		for (int i = 0; i<hl.length; i++){
			hl[i] = new HealthLine();
		}
	}

	@Override
	public void render () {
		float dt = Gdx.graphics.getDeltaTime();
		update(dt);
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		background.render(batch);
		hero.render(batch);
		monster.render(batch);
		for (int i = 0; i<hl.length; i++){
			hl[i].render(batch);
		}
		batch.end();
	}

	public void update(float dt){
		hero.update(dt);
		monster.update(dt);
		if(currentUnit == hero){
			if (InputHandler.checkClickInRect(monster.rect)) {
				hero.meleeAttack(monster);
				currentUnit = monster;
			}
		}
		if(currentUnit == monster){
			if(InputHandler.checkClickInRect(hero.rect)){
				monster.meleeAttack(hero);
				currentUnit = hero;
			}
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
