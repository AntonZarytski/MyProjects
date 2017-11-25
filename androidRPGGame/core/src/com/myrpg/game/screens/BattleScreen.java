package com.myrpg.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.myrpg.game.Assets;
import com.myrpg.game.GameSesion;
import com.myrpg.game.Hero;
import com.myrpg.game.InfoSystem;
import com.myrpg.game.MyInputProcessor;
import com.myrpg.game.ScreenManager;
import com.myrpg.game.SpecialFXEmitter;
import com.myrpg.game.Unit;
import com.myrpg.game.UnitFactory;
import com.myrpg.game.action.BaseAction;
import java.util.ArrayList;
import java.util.List;

public class BattleScreen implements Screen {
    private static int gameLevel=1;
    private SpriteBatch batch;
    private Texture backgroundtexture;
   // private Background background;
    private List<Unit> units;
    private UnitFactory unitFactory;
    private int currentUnitIndex;
    private Unit currentUnit;
    private TextureRegion textureSelector;
    private InfoSystem infoSystem;
    private Hero player1;
    private Hero player2;
    private BitmapFont font;
    private Hero[] players;

    private MyInputProcessor mip;
    private Vector2[][] stayPoints;
    private float animationTime;
    private SpecialFXEmitter specialFXEmitter;

    private Stage stage;//сцена с элементами управления
    private Skin skin;// оформление кнопок

    public List<Unit> getUnits() {
        return units;
    }
    public SpecialFXEmitter getSpecialFXEmitter() {return specialFXEmitter;}
    public UnitFactory getUnitFactory() {return unitFactory;}
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
    public boolean isHeroTurn(){
        return currentUnit.getAutopilot() == null;
    }
    public static int getGameLevel() {return gameLevel;}
    public static void setGameLevel(int gameLevel) {BattleScreen.gameLevel = gameLevel;}

    public void createGUI(){
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Gagalin.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 1;
        parameter.shadowColor = Color.BLACK;
        parameter.shadowOffsetX = -2;
        parameter.shadowOffsetY = 2;
        parameter.color = Color.WHITE;
        font = generator.generateFont(parameter);
        generator.dispose();

        stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        skin = new Skin(Assets.getInstance().getAtlas());
        List<BaseAction> list = unitFactory.getActions();
        for (BaseAction action: list) {
            skin.addRegions(Assets.getInstance().getAtlas());
            Button.ButtonStyle buttonStyle = new Button.ButtonStyle();
            buttonStyle.up = skin.newDrawable(action.getTextureName());
            skin.add(action.getName(), buttonStyle);
        }

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("MenuBtn");
        textButtonStyle.font = font;
        skin.add("tbs", textButtonStyle);

        Button btnPause = new TextButton("PAUSE", skin, "tbs");
        btnPause.setPosition(1600, 900);
        stage.addActor(btnPause);
        btnPause.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameSesion.getInstance().saveSession();
                ScreenManager.getInstance().swithScreen(ScreenManager.ScreenTypes.MENU);
            }
        });

        for(Unit unit: units){
            if(!unit.isAI()){
                Group actionPanel = new Group();
                Image image = new Image(Assets.getInstance().getAtlas().findRegion("ActionPanel"));
                actionPanel.addActor(image);
                actionPanel.setPosition(10, 10, 5);
                actionPanel.setVisible(false);
                unit.setActionPanel(actionPanel);
                stage.addActor(actionPanel);

                int counter = 0;
                final Unit innerUnit = unit;
                for(BaseAction action: unit.getActions()){
                    final BaseAction ba = action;
                    Button btn = new Button(skin, action.getName());
                    btn.setPosition(30 + counter*100, 10);
                    btn.addListener(new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            if(!innerUnit.isAI()){
                                if(ba.action(innerUnit)){
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

    @Override
    public void show() {
        final int LEFT_STAYPOINT_X = 300;
        final int TOP_STAYPOINT_Y = 600;
        final int DISTANCE_BETWEEN_UNITS_X = 250;
        final int DISTANCE_BETWEEN_UNITS_Y = 250;
        final int DISTANCE_BETWEEN_TEAMS = 400;
        final int SHIFT_NEXT_UNIT = 0;
        stayPoints = new Vector2[4][3];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                int x = LEFT_STAYPOINT_X + i * DISTANCE_BETWEEN_UNITS_X+ j * SHIFT_NEXT_UNIT;
                if (i > 1) x += DISTANCE_BETWEEN_TEAMS;
                stayPoints[i][j] = new Vector2(x, TOP_STAYPOINT_Y - j * DISTANCE_BETWEEN_UNITS_Y);
            }
        }
        player1 = GameSesion.getInstance().getPlayer1();
      //  player2 = GameSesion.getInstance().getPlayer2();
        player2 = new Hero();
        unitFactory = new UnitFactory();

        player2.setArmy(unitFactory.createUnit(UnitFactory.UnitType.WARIOR, true, true, 1),
        null, null,
        unitFactory.createUnit(UnitFactory.UnitType.WIZARD, true, true, 2),
        unitFactory.createUnit(UnitFactory.UnitType.WIZARD, true, true, 3),
        unitFactory.createUnit(UnitFactory.UnitType.DD, true, true, 4)
        );
        units = new ArrayList<Unit>();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3 ; j++) {
                if (player1.getTeam()[i][j] != null) {
                    unitFactory.reloadUnit(player1.getTeam()[i][j]);
                    units.add(player1.getTeam()[i][j]);
                    player1.getTeam()[i][j].setToMap(this, i, j);
                }
            }
        }
        for (int i = 0; i < 2 ; i++) {
            for (int j = 0; j < 3 ; j++) {
                if (player2.getTeam()[i][j] != null) {
                 //   unitFactory.reloadUnit(player2.getTeam()[i][j]);
                    units.add(player2.getTeam()[i][j]);
                    player2.getTeam()[i][j].setToMap(this, i+2, j);
                }
            }
        }

        specialFXEmitter = new SpecialFXEmitter();
        mip = new MyInputProcessor();
        Gdx.input.setInputProcessor(mip);
        backgroundtexture = Assets.getInstance().getAssetManager().get("Field.jpg", Texture.class);

        infoSystem = new InfoSystem();
        textureSelector = Assets.getInstance().getAtlas().findRegion("Selector");

        currentUnitIndex=0;
        currentUnit = units.get(currentUnitIndex);
        createGUI();
        InputMultiplexer im= new InputMultiplexer(stage, mip);
        Gdx.input.setInputProcessor(im);//gdxная система ввода обробатывает запросы со stage и mip
        animationTime= 0.0f;
    }

    public void nextTurn() {
/*        if(!player2.HaveLiveUnits()){
            System.out.println(units.size());
            for (int i = 11; i >6 ; i--) {
                units.remove(i);
            }
            ScreenManager.getInstance().swithScreen(ScreenManager.ScreenTypes.LEVELEND);
        }*/
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
        batch.draw(backgroundtexture, 0,0);
        if(isHeroTurn()) {
            batch.setColor(0, 1, 0, 0.5f);
            batch.draw(textureSelector, currentUnit.getPosition().x-40, currentUnit.getPosition().y);
        }
        if (isHeroTurn() && currentUnit.getTarget() != null){
            batch.setColor(1,0,0,0.2f);
            if(currentUnit.getTarget().isMyTeamMate(currentUnit)){
                batch.draw(textureSelector, currentUnit.getTarget().getPosition().x-40, currentUnit.getTarget().getPosition().y  );
            }else {
                batch.draw(textureSelector, currentUnit.getTarget().getPosition().x+20, currentUnit.getTarget().getPosition().y );
            }
        }
        batch.setColor(1,1,1,1);
        for (int i = 0; i <units.size() ; i++) {
            units.get(i).render(batch);
        }
        infoSystem.render(batch);
        specialFXEmitter.render(batch);
        batch.end();
        stage.draw();
    }

    public void update(float dt) {
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
        specialFXEmitter.update(dt);

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
