package com.myrpg.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.myrpg.game.action.BaseAction;
import com.myrpg.game.effects.Effect;
import com.myrpg.game.screens.BattleScreen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Unit implements Serializable{
    public enum AnimationType{
        STAING(0), ATTACK(1);

        int number;

        AnimationType(int number){
            this.number = number;
        }
    }
    transient private BattleScreen battleScreen;
    transient private TextureRegion texture;
    transient private TextureRegion textureDeath;
    private List<Effect> effects;
    transient private List<BaseAction> actions;
    private SkillsFactory skills;
    private Vector2 position;
    private Rectangle rect;
    private Autopilot autopilot;
    transient private Group actionPanel;
    transient private Label hpLabel;
    transient private Unit target;
    private Hero hero;
    private int level;
    private int exp;
    private int needExp;
    private int mana;
    private int maxMana;
    private Integer hp;
    private int maxHp;
    private HealthLine hl;
    private String hpStr;
    private boolean isAlive;
    private float attackAction;
    private boolean flip;
    private float takeDamageAction;

    private UnitFactory.UnitType type;
    transient private TextureRegion[][] frames;
    private AnimationType currentAnimation;
    private float animationTime;
    private float animationSpeed;
    private int maxFrame;
    private int maxAnimationType;
    private int animationFrame;
    private final int WIDTH = 200;
    private final int HEIGHT = 300;

    public void setCurrentAnimation(AnimationType currentAnimation) {
        this.currentAnimation = currentAnimation;
    }

    public UnitFactory.UnitType getType() {
        return type;
    }

    public void reload(TextureRegion texture,TextureRegion textureDeath, List<BaseAction> actions){
        this.texture = texture;
        this.textureDeath = textureDeath;
        this.frames = this.texture.split(WIDTH, HEIGHT);
        this.actions = actions;
        createHelathLine();
        this.effects.clear();
        this.animationSpeed = 0.2f;
        this.frames = this.texture.split(WIDTH, HEIGHT);
        this.maxFrame = this.frames[0].length;
        this.maxAnimationType = this.frames.length - 1;
        this.currentAnimation = AnimationType.STAING;
        if(this.isAI()){
            flip = true;//заглушка т.к за человека тоже может играть аи
        }
    }

    public int getWIDTH() {return WIDTH;}
    public int getHEIGHT() {return HEIGHT;}
    public void setAutopilot(Autopilot autopilot) {
        this.autopilot = autopilot;
    }
    public boolean isAI(){
     return autopilot != null;
    }
    public boolean isMyTeamMate(Unit another){return this.hero == another.hero;}
    public List<BaseAction> getActions() {
        return actions;
    }
    public void setAttackAction(float attackAction) {
        this.attackAction = attackAction;
    }
    public Unit getTarget() {
        return target;
    }
    public int getMaxHp() {
        return maxHp;
    }
    public com.myrpg.game.screens.BattleScreen getBattleScreen() {
        return battleScreen;
    }
    public int getLevel() { return level; }
    public Vector2 getPosition() { return position; }
    public Rectangle getRect() { return rect; }
    public boolean isAlive() { return isAlive; }
    public void addEffect(Effect effect){
        effects.add(effect);
    }
    public void setTarget(Unit target) {
        this.target = target;
    }
    public void setHero(Hero hero) {this.hero = hero;}
    public SkillsFactory getSkills() {
        return skills;
    }
    public void setActions(List<BaseAction> actions) {
        this.actions = actions;
    }
    public void setFlip(boolean flip) {
        this.flip = flip;
    }
    public Autopilot getAutopilot() {
        return autopilot;
    }
    public Group getActionPanel() {return actionPanel;}
    public TextureRegion getTexture() {
        return texture;
    }
    public TextureRegion getTextureDeath() {
        return textureDeath;
    }
    public void setActionPanel(Group actionPanel) {
        this.actionPanel = actionPanel;
    }

    public Unit(UnitFactory.UnitType type, TextureRegion texture, TextureRegion textureDeath, SkillsFactory skills){
        this.type = type;
        this.texture = texture;
        this.textureDeath = textureDeath;
        this.position = new Vector2(0,0);
        this.skills = skills;
        this.exp = 0;
        this.needExp = 300;
        this.maxHp = skills.getEndurance() * 10;
        this.maxMana = skills.getKnowlege() * 10;
        this.mana = maxMana;
        this.hp = this.getMaxHp();
        createHelathLine();
        this.effects = new ArrayList<Effect>();
        this.actions = new ArrayList<BaseAction>();
        this.isAlive=true;
        this.animationSpeed = 0.2f;
        this.frames = this.texture.split(WIDTH, HEIGHT);
        this.maxFrame = this.frames[0].length;
        this.maxAnimationType = this.frames.length - 1;
        this.currentAnimation = AnimationType.STAING;
    }
    public void recalculateSkills(){
        this.maxHp = skills.getEndurance() * 10;
        this.maxMana = skills.getKnowlege() * 10;
        this.mana = maxMana;
        this.hp = this.maxHp;
    }
    public void setToMap(BattleScreen battleScreen, int cellX, int cellY){
        this.battleScreen = battleScreen;
        this.position.set(battleScreen.getStayPoints()[cellX][cellY]);
        this.rect = new Rectangle(position.x, position.y, texture.getRegionWidth(), texture.getRegionHeight());
    }

    public void getTurn() {
        for (int i = effects.size() - 1; i >= 0; i--) {
            effects.get(i).tick();
            if (effects.get(i).isEnded()) {
                effects.get(i).end();
                effects.remove(i);
            }
        }
    }
    public void levelUp(){
        this.level++;
        this.skills.setLevel(level);
        needExp =(int)(needExp*(1.2f));
        this.recalculateSkills();
    }
    public void setlevel(int level){
        this.level = level;
        this.skills.setLevel(level);
        this.recalculateSkills();
    }
    public void changeHP (int value, String couse) {
        if (hp + value > maxHp) {
            value = maxHp - hp;
        }
        hp += value;
        if(hp < 0){
            hp = 0;
            isAlive = false;
        }
        if (value < 0) {
            battleScreen.getInfoSystem().addMessage(couse + " -" + value, FlyingText.Colors.RED, this);
            takeDamageAction = 1.0f;
        }
        if (value >= 0) {
            battleScreen.getInfoSystem().addMessage(couse + " + " + value, FlyingText.Colors.GREEN, this);
        }
    }
    public void miss(){
        battleScreen.getInfoSystem().addMessage("MISS",FlyingText.Colors.WHITE, this);
    }
    public void render(SpriteBatch batch){
        float dx = 10f*(float)Math.sin(1f - attackAction)*3.14f;
        if(!flip) dx*=-1;
        float scaleX = 100*hp*1.0f/maxHp;
        hpStr = hp.toString();
        int n = currentAnimation.number;
        if(n > maxAnimationType){
            n = 0;
        }
        if(isAlive) {
            if(flip){
                frames[n][animationFrame].flip(true, false);
            }
            //рисуем хелзбар у живых персонажей
            batch.draw(hl.textureBack, position.x+frames[n][animationFrame].getRegionWidth()*0.25f-2+dx, position.y+frames[n][animationFrame].getRegionHeight()*1.05f-2);
            batch.draw(hl.textureFront,
                    position.x+frames[n][animationFrame].getRegionWidth()*0.25f+dx,
                    position.y+frames[n][animationFrame].getRegionHeight()*1.05f,
                    0,
                    0,
                    1,
                    20,
                    scaleX,
                    1,
                    0
                    );
            hpLabel.setX(position.x+frames[n][animationFrame].getRegionWidth()*0.25f+40+dx);
            hpLabel.setY(position.y+frames[n][animationFrame].getRegionHeight()*1.05f);
            hpLabel.setText(hpStr);
            hpLabel.draw(batch, 1);
            if (takeDamageAction > 0) {
                batch.setColor(1f, 1f - takeDamageAction, 1f - takeDamageAction, 1f);
            }
            //рисуем живых персонажей
            batch.draw(frames[n][animationFrame],
                    position.x + dx,
                    position.y,
                    frames[n][animationFrame].getRegionWidth()/2,
                    frames[n][animationFrame].getRegionHeight()/2,
                    WIDTH,
                    HEIGHT,
                    1,
                    1,
                    0
                    );
        }else {
            //рисуем трупы
            batch.draw(textureDeath,
                    position.x,
                    position.y,
                    frames[n][animationFrame].getRegionWidth()/2,
                    frames[n][animationFrame].getRegionHeight()/2,
                    frames[n][animationFrame].getRegionHeight()*0.8f,
                    frames[n][animationFrame].getRegionWidth()*0.8f,
                    1,
                    1,
                    0);
        }
        if(flip){
            frames[n][animationFrame].flip(true, false);
        }
        batch.setColor(1f,1f,1f,1f);
    }
    public void update(float dt){
        if(exp >= needExp)this.levelUp();
        animationTime += dt;
        animationFrame = (int) (animationTime / animationSpeed);
        animationFrame = animationFrame % maxFrame;
        if (takeDamageAction > 0) {
            takeDamageAction -= dt;
            if(takeDamageAction <= 0){
                currentAnimation = AnimationType.STAING;
            }
        }
        if (attackAction > 0) {
            attackAction -= dt;
            if(attackAction <= 0 ){
                currentAnimation = AnimationType.STAING;
            }
        }
    }
    public void createHelathLine(){
     //   font = Assets.getInstance().getAssetManager().get("font.fnt", BitmapFont.class);
        hl = new HealthLine();
        hpStr = hp.toString();
        hl.setPosition( new Vector2((position.x+texture.getRegionWidth()*0.25f), (position.y+texture.getRegionHeight()*1.05f)));
        hpLabel = new Label(hpStr, hl.labelStyleWhite);
    }
}

class HealthLine implements Serializable{
    transient TextureRegion textureFront;
    transient TextureRegion textureBack;
    Vector2 position;
    transient Label.LabelStyle labelStyleWhite;

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public HealthLine(){
        textureFront = Assets.getInstance().getAtlas().findRegion("HealthLine");
        textureBack = Assets.getInstance().getAtlas().findRegion("HealthLineBack");
        labelStyleWhite = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
    }
}

