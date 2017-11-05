package com.myrpg.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;


public class runGame extends ApplicationAdapter {
	public SpriteBatch batch;
	private Background background;
	private Hero player;
	private Enemy monster;
	private Enemy monster2;
	private List<Person> units;
	private int currentUnit;
	private int selectedUnit;
	private Texture textureSelector;
	private List<Button> btnGUI;
	private FlyingText[] msgs;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Background();
		textureSelector = new Texture("Selector.png");
		player = new Hero(this, new Vector2(200, 150));
		monster = new Enemy(this, new Vector2(720, 15), player);
		monster2 = new Enemy(this, new Vector2(580, 285), player);
		units = new ArrayList<Person>();
		units.add(player);
		units.add(monster);
		units.add(monster2);
		currentUnit=0;
		selectedUnit=0;
		btnGUI = new ArrayList<Button>();
		btnGUI.add(new Button("attack", new Texture("Button.png"), new Rectangle(200, 20, 50, 50)));
		msgs = new FlyingText[50];
		for (int i = 0; i < msgs.length; i++) {
			msgs[i] = new FlyingText();
		}
	}

	public void addMessage(String text, float x, float y) {
		for (int i = 0; i < msgs.length; i++) {
			if (!msgs[i].isActive()) {
				msgs[i].setup(text, x, y);
				break;
			}
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
		for (int i = 0; i <units.size() ; i++) {
			if (isHeroTurn()&& currentUnit==i && units.get(i).isAlive){
				batch.setColor(0,1,0,0.2f);
					batch.draw(textureSelector, units.get(i).getPosition().x-units.get(i).getTexture().getWidth()*0.2f, units.get(i).getPosition().y+units.get(i).getTexture().getHeight()*0.015f  );
			}
			if (selectedUnit==i && units.get(i).isAlive){
				batch.setColor(1,0,0,0.2f);
				if(units.get(i)instanceof Hero){
					batch.draw(textureSelector, units.get(i).getPosition().x-units.get(i).getTexture().getWidth()*0.2f, units.get(i).getPosition().y+units.get(i).getTexture().getHeight()*0.015f  );
				}else {
					batch.draw(textureSelector, units.get(i).getPosition().x+units.get(i).getTexture().getWidth()*0.1f, units.get(i).getPosition().y+units.get(i).getTexture().getHeight()*0.015f  );
				}
			}
			batch.setColor(1,1,1,1);
			units.get(i).render(batch);
		}
		for (int i = 0; i <btnGUI.size() ; i++) {
			btnGUI.get(i).render(batch);
		}
		for (int i = 0; i < msgs.length; i++) {
			if (msgs[i].isActive()) {
				msgs[i].render(batch);
			}
		}
		batch.end();
	}

	public void update(float dt) {
		for (int i = 0; i < units.size(); i++) {
			units.get(i).update(dt);
			if (InputHandler.checkClickInRect(units.get(i).getRect())) {
				selectedUnit = i;
			}
		}
		if (isHeroTurn()) {
			for (int i = 0; i < btnGUI.size(); i++) {
				if (btnGUI.get(i).checkClick()) {
					String action = btnGUI.get(i).getAction();
					if (action.equals("attack")) {
						if (units.get(selectedUnit) instanceof Enemy) {
							player.meleeAttack(units.get(selectedUnit));
							nextTurn();
							selectedUnit=0;
						}
					}
				}
			}
		}
		if (!isHeroTurn()&& player.isAlive) {
			if(((Enemy) units.get(currentUnit)).ai(dt)){
				nextTurn();
			}
		}
		for (int i = 0; i < msgs.length; i++) {
			if (msgs[i].isActive()) {
				msgs[i].update(dt);
			}
		}
		}

	public boolean isHeroTurn(){
		return units.get(currentUnit)instanceof Hero;
	}
	public void nextTurn(){
		do {
			currentUnit++;
			if (currentUnit >= units.size()) {
				currentUnit = 0;
			}
		}while (!units.get(currentUnit).isAlive);
		units.get(currentUnit).getTurn();
	}
	@Override
	public void dispose () {
		batch.dispose();
	}
}
