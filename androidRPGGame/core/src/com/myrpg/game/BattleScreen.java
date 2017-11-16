package com.myrpg.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.myrpg.game.action.BaseAction;
import com.myrpg.game.action.DefenceStanceAction;
import com.myrpg.game.action.MelleAtackAction;
import com.myrpg.game.action.RestAction;

import java.util.ArrayList;
import java.util.List;

public class BattleScreen implements Screen {
    private SpriteBatch batch;

    private Background background;
    private List<Unit> units;
    private UnitFactory unitFactory;
    private int currentUnitIndex;
    private Unit currentUnit;
    private Texture textureSelector;
    private InfoSystem infoSystem;

    private MyInputProcessor mip;
    private Vector2[][] stayPoints;
    private float animationTime;

    private Stage stage;//сцена с элементами управления
    private Skin skin;// оформление кнопок

    public List<Unit> getUnits() {
        return units;
    }

    public void createGUI(){
        stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        skin = new Skin();
        List<BaseAction> list = unitFactory.getActions();
        for (BaseAction action: list) {
            skin.add(action.getName(), action.getBtnTexture());
            Button.ButtonStyle buttonStyle = new Button.ButtonStyle();
            buttonStyle.up = skin.newDrawable(action.getName(), Color.WHITE);
            skin.add(action.getName(), buttonStyle);
        }
        for(Unit unit: units){
            if(!unit.isAI()){
                Group actionPanel = new Group();
                Image image = new Image(Assets.getInstance().getAssetManager().get("ActionPanel.png", Texture.class));
                actionPanel.addActor(image);
                actionPanel.setPosition(10, 10, 5);
                actionPanel.setVisible(false);
                unit.setActionPanel(actionPanel);
                stage.addActor(actionPanel);

                int counter = 0;
                for(BaseAction action: unit.getActions()){
                    final BaseAction ba = action;
                    Button btn = new Button(skin, action.getName());
                    btn.setPosition(30 + counter*100, 10);
                    btn.addListener(new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            if(!currentUnit.isAI()){
                                if(ba.action(currentUnit)){
                                    nextTurn();
                                }
                            }
                        }
                    });
                    actionPanel.addActor(btn);
                    counter++;
                }
            }
        }
    }

    public boolean canIMakeTurn(){
        return animationTime <= 0;
    }

    public InfoSystem getInfoSystem() {
        return infoSystem;
    }

    public BattleScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    public Vector2[][] getStayPoints() {
        return stayPoints;
    }

    @Override
    public void show() {
        stayPoints = new Vector2[4][3];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                int x = 200 + i * 250 + j * 100;
                if (i > 1) x += 250;
                stayPoints[i][j] = new Vector2(x, 600 - j * 250);
            }
        }
        unitFactory = new UnitFactory(this);
        units = new ArrayList<Unit>();
        unitFactory.createUnitAndAddToBattle(UnitFactory.UnitType.WIZARD, this, true, 0,0);
/*        unitFactory.createUnitAndAddToBattle(UnitFactory.UnitType.WARIOR, this, true, 0,1);
        unitFactory.createUnitAndAddToBattle(UnitFactory.UnitType.WARIOR, this, true, 0,2);
        unitFactory.createUnitAndAddToBattle(UnitFactory.UnitType.DD, this, true, 1,0);
        unitFactory.createUnitAndAddToBattle(UnitFactory.UnitType.DD, this, true, 1,1);
        unitFactory.createUnitAndAddToBattle(UnitFactory.UnitType.DD, this, true, 1,2);*/

        unitFactory.createUnitAndAddToBattle(UnitFactory.UnitType.DD, this, false, 2,0);
        unitFactory.createUnitAndAddToBattle(UnitFactory.UnitType.WIZARD, this, false, 2,1);
        unitFactory.createUnitAndAddToBattle(UnitFactory.UnitType.WARIOR, this, false, 2,2);
        unitFactory.createUnitAndAddToBattle(UnitFactory.UnitType.DD, this, false, 3,0);
        unitFactory.createUnitAndAddToBattle(UnitFactory.UnitType.WIZARD, this, false, 3,1);
        unitFactory.createUnitAndAddToBattle(UnitFactory.UnitType.WARIOR, this, false, 3,2);
        mip = new MyInputProcessor();
        Gdx.input.setInputProcessor(mip);
        background = new Background();
        infoSystem = new InfoSystem();
        textureSelector = Assets.getInstance().getAssetManager().get("Selector.png", Texture.class);

        currentUnitIndex=0;
        currentUnit = units.get(currentUnitIndex);
        createGUI();
        InputMultiplexer im= new InputMultiplexer(mip, stage);
        Gdx.input.setInputProcessor(im);//gdxная система ввода обробатывает запросы со stage и mip
        animationTime= 0.0f;
    }

    public boolean isHeroTurn(){
        return currentUnit.getAutopilot() == null;
    }

    public void nextTurn() {
        // скрываем панель после пропуска хода
        for (int i = 0; i < units.size() ; i++) {
            if(units.get(i).getActionPanel() != null){
                units.get(i).getActionPanel().setVisible(false);
            }
        }
        do {
            currentUnitIndex++;
            if (currentUnitIndex >= units.size()) {
                currentUnitIndex = 0;
            }
        } while (!units.get(currentUnitIndex).isAlive());
        currentUnit = units.get(currentUnitIndex);
        currentUnit.getTurn();
        animationTime = 1.0f;
        // отоюражаем панель для игрока не ии к которому перешол ход
        if(currentUnit.getActionPanel() != null){
            currentUnit.getActionPanel().setVisible(true);
        }
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.render(batch);
        if(isHeroTurn()) {
            batch.setColor(0, 0, 1, 0.5f);
            batch.draw(textureSelector, currentUnit.getPosition().x - currentUnit.getTexture().getWidth() * 0.2f, currentUnit.getPosition().y + currentUnit.getTexture().getHeight() * 0.015f);
        }
        if (isHeroTurn() && currentUnit.getTarget() != null){
            batch.setColor(1,0,0,0.2f);
            if(currentUnit.getTarget().isAI()){
                batch.draw(textureSelector, currentUnit.getTarget().getPosition().x-currentUnit.getTarget().getTexture().getWidth()*0.2f, currentUnit.getTarget().getPosition().y+currentUnit.getTarget().getTexture().getHeight()*0.015f  );
            }else {
                batch.draw(textureSelector, currentUnit.getTarget().getPosition().x+currentUnit.getTarget().getTexture().getWidth()*0.1f, currentUnit.getTarget().getPosition().y+currentUnit.getTarget().getTexture().getHeight()*0.015f  );
            }
        }
        batch.setColor(1,1,1,1);
        for (int i = 0; i <units.size() ; i++) {
            units.get(i).render(batch);
        }
        infoSystem.render(batch);
     /*   if(defenceEffect.isActive()){
            defenceEffect.render(batch);
        }*/
        batch.end();
        stage.draw();
    }

    public void update(float dt) {
        if(Gdx.input.isTouched()) {
            System.out.println(Gdx.input.getX() + " " + Gdx.input.getY());
        }
        if(isHeroTurn() && canIMakeTurn()){
            stage.act(dt);
            if(currentUnit.getActionPanel() != null) {
                currentUnit.getActionPanel().setVisible(true);
            }
        }
        if (animationTime > 0.0f) {
            animationTime -= dt;
        }
        for (int i = 0; i < units.size(); i++) {
            units.get(i).update(dt);
            if (mip.isTouchedInArea(units.get(i).getRect()) && units.get(i).isAlive()) {
                currentUnit.setTarget(units.get(i));
            }
        }
        if (!isHeroTurn()) {
            if (currentUnit.getAutopilot().turn(currentUnit)) {
                nextTurn();
            }
        }
        infoSystem.update(dt);


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

    }

}
