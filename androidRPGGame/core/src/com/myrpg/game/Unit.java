package com.myrpg.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.myrpg.game.action.BaseAction;
import com.myrpg.game.effects.Effect;

import java.util.ArrayList;
import java.util.List;

public class Unit {
    private BattleScreen battleScreen;
    private Unit target;
    private Texture texture;
    private Texture textureDeath;

    private BitmapFont font;

    private int level;
    private int exp;
    private int needExp;
    private int mana;
    private int maxMana;
    private String name;
    private Integer hp;
    private int maxHp;
    private HealthLine hl;
    private String hpStr;
    private Label hpLabel;
    private boolean isAlive;
    private List<Effect> effects;
    private List<BaseAction> actions;
    private SkillsFactory skills;
    private float attackAction;

    private Vector2 position;
    private Rectangle rect;
    private boolean flip;
    private Autopilot autopilot;
    private Group actionPanel;
    private float takeDamageAction;

    public void setAutopilot(Autopilot autopilot) {
        this.autopilot = autopilot;
    }
    public boolean isAI(){
     return autopilot != null;
    }

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

    public BattleScreen getBattleScreen() {
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

    public Unit(Texture texture, Texture textureDeath, SkillsFactory skills){
        this.texture = texture;
        this.textureDeath = textureDeath;
        this.position = new Vector2(0,0);
        this.skills = skills;
        this.maxHp = skills.getEndurance() * 10;
        this.maxMana = skills.getKnowlege() * 10;
        this.mana = maxMana;
        this.hp = this.getMaxHp();
        createHelathLine();
        this.effects = new ArrayList<Effect>();
        this.actions = new ArrayList<BaseAction>();
        this.isAlive=true;
    }
    public void recalculateSkills(){
        this.maxHp = skills.getEndurance() * 10;
        this.maxMana = skills.getKnowlege() * 10;
        this.mana = maxMana;
        this.hp = this.getMaxHp();
    }
    public void setToMap(BattleScreen battleScreen, int cellX, int cellY){
        this.battleScreen = battleScreen;
        this.position.set(battleScreen.getStayPoints()[cellX][cellY]);
        this.rect = new Rectangle(position.x, position.y, texture.getWidth(), texture.getHeight());
        this.recalculateSkills();
    }
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

    public Texture getTexture() {
        return texture;
    }

    public Texture getTextureDeath() {
        return textureDeath;
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
    public void levelUp(int level){
        this.level++;
        skills.levelUp(level);
        needExp =(int)(needExp*(1.2f));
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

    public void setActionPanel(Group actionPanel) {
        this.actionPanel = actionPanel;
    }

    public void miss(){
        battleScreen.getInfoSystem().addMessage("MISS",FlyingText.Colors.WHITE, this);
    }

    public void render(SpriteBatch batch){
        float dx = 10f*(float)Math.sin(1f - attackAction)*3.14f;
        if(!flip) dx*=-1;
        float scaleX = 100*hp*1.0f/maxHp;
        hpStr = hp.toString();
        if(isAlive) {
            //рисуем хелзбар у живых персонажей
            batch.draw(hl.textureBack, position.x+texture.getWidth()*0.25f-2+dx, position.y+texture.getHeight()*1.05f-2);
            batch.draw(hl.textureFront,
                    position.x+texture.getWidth()*0.25f+dx,
                    position.y+texture.getHeight()*1.05f,
                    0,
                    0,
                    1,
                    20,
                    scaleX,
                    1,
                    0,
                    0,
                    0,
                    20,
                    100,
                    false,
                    false);
            hpLabel.setX(position.x+texture.getWidth()*0.25f+40+dx);
            hpLabel.setY(position.y+texture.getHeight()*1.05f);
            hpLabel.setText(hpStr);
            hpLabel.draw(batch, 1);
            if (takeDamageAction > 0) {
                batch.setColor(1f, 1f - takeDamageAction, 1f - takeDamageAction, 1f);
            }
            //рисуем живых персонажей
            batch.draw(texture,
                    position.x + dx,
                    position.y, 0,
                    0,
                    texture.getWidth(),
                    texture.getHeight(),
                    1,
                    1,
                    0,
                    0,
                    0,
                    texture.getWidth(),
                    texture.getHeight(),
                    flip,
                    false);
        }else {
            //рисуем трупы
            batch.draw(textureDeath,
                    position.x ,
                    position.y, 0,
                    0,
                    textureDeath.getWidth(),
                    textureDeath.getHeight(),
                    1,
                    1,
                    0,
                    0,
                    0,
                    textureDeath.getWidth(),
                    textureDeath.getHeight(),
                    flip,
                    false);
        }
        batch.setColor(1f,1f,1f,1f);
    }
    public void update(float dt){
        if (takeDamageAction > 0) {
            takeDamageAction -= dt;
        }
        if (attackAction > 0) {
            attackAction -= dt;
        }
    }

    public void createHelathLine(){
        font = Assets.getInstance().getAssetManager().get("font.fnt", BitmapFont.class);
        hl = new HealthLine();
        hpStr = hp.toString();
        hl.setPosition( new Vector2((position.x+texture.getWidth()*0.25f), (position.y+texture.getHeight()*1.05f)));
        hpLabel = new Label(hpStr, hl.labelStyleWhite);
    }

}
class HealthLine {
    Texture textureFront;
    Texture textureBack;
    Vector2 position;
    Label.LabelStyle labelStyleWhite;

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public HealthLine(){
        textureFront = Assets.getInstance().getAssetManager().get("HealthLine.png", Texture.class);
        textureBack = Assets.getInstance().getAssetManager().get("HealthLineBack.png", Texture.class);
        labelStyleWhite = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
    }
}

