package com.myrpg.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.myrpg.game.action.BaseAction;
import com.myrpg.game.action.DefenceStanceAction;
import com.myrpg.game.action.MelleAtackAction;
import com.myrpg.game.action.RestAction;

import java.util.ArrayList;
import java.util.List;

public class GameScreen implements Screen {
    private SpriteBatch batch;
    private Background background;

    private Hero player;
    private List<AbstractUnit> units;
    private int currentUnit;
    private int selectedUnit;
    private Texture textureSelector;
    private List<Button> btnGUI;
    private InfoSystem infoSystem;
    private List<BaseAction> actions;
    private DefefenceFX defenceEffect;

    private float animationTime;

    public boolean canIMakeTurn(){
        return animationTime <= 0;
    }

    public InfoSystem getInfoSystem() {
        return infoSystem;
    }

    public GameScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    @Override
    public void show() {
        defenceEffect = new DefefenceFX();
        actions = new ArrayList<BaseAction>();
        actions.add(new MelleAtackAction());
        actions.add(new RestAction());
        actions.add(new DefenceStanceAction());
        background = new Background();
        infoSystem = new InfoSystem();
        textureSelector = new Texture("Selector.png");
        units = new ArrayList<AbstractUnit>();
        player = new Hero(this, new Vector2(200, 150),3,0);
        units.add(player);
        units.add(new Enemy(this, new Vector2(720, 15), player,2,2));
        units.add(new Enemy(this, new Vector2(580, 285), player,3,1));
        currentUnit=0;
        selectedUnit=0;
        prepareButtons();
        animationTime= 0.0f;
    }

    public boolean isHeroTurn(){
        return units.get(currentUnit)instanceof Hero;
    }

    public void prepareButtons(){
        btnGUI = new ArrayList<Button>();
        for (int i = 0; i <actions.size() ; i++) {
            final int w = i;
            Button btn = new Button( (actions.get(w).getBtnTexture()), new Rectangle(i*100, 20, 50, 50)) {
                @Override
                public void action() {
                    if (actions.get(w).action(player)) {
                        nextTurn();
                    }
                }
            };
            btnGUI.add(btn);
        }
    }

    public void nextTurn() {
        do {
            currentUnit++;
            if (currentUnit >= units.size()) {
                currentUnit = 0;
            }
        } while (!units.get(currentUnit).isAlive());
        units.get(currentUnit).getTurn();
        animationTime = 1.0f;
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.render(batch);
        for (int i = 0; i <units.size() ; i++) {
            if (currentUnit==i && units.get(i).isAlive && isHeroTurn()){
                batch.setColor(0,1,0,0.2f);
                batch.draw(textureSelector, units.get(i).getPosition().x-units.get(i).getTexture().getWidth()*0.2f, units.get(i).getPosition().y+units.get(i).getTexture().getHeight()*0.015f  );
            }
            if (selectedUnit==i && units.get(i).isAlive && isHeroTurn()){
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
        if (isHeroTurn()) { //  && InputHandler.getY() < 100
            for (int i = 0; i < btnGUI.size(); i++) {
                btnGUI.get(i).render(batch);
            }
        }
        infoSystem.render(batch);
        if(defenceEffect.isActive()){
            defenceEffect.render(batch);
        }
        batch.end();
    }

    public void update(float dt) {
        if (animationTime > 0.0f) {
            animationTime -= dt;
        }
        for (int i = 0; i < units.size(); i++) {
            units.get(i).update(dt);
            if (InputHandler.checkClickInRect(units.get(i).getRect()) && units.get(i).isAlive()) {
                selectedUnit = i;
                player.setTarget(units.get(selectedUnit));
            }
        }

        if (isHeroTurn() && canIMakeTurn()) {
            for (int i = 0; i < btnGUI.size(); i++) {
                btnGUI.get(i).checkClick();
            }
        }
        if (!isHeroTurn()) {
            if (((Enemy) units.get(currentUnit)).ai(dt, actions.get(0))) {
                defenceEffect.setup(200,200);
                nextTurn();
            }
        }
        infoSystem.update(dt);
        if(defenceEffect.isActive()){
            defenceEffect.update(dt);
        }
    }

    @Override
    public void resize(int width, int height) {

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

    }

}
