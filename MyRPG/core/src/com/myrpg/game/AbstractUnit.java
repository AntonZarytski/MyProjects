package com.myrpg.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.myrpg.game.effects.Effect;

import java.util.ArrayList;

public abstract class AbstractUnit {
    protected GameScreen game;
    protected AbstractUnit target;
    protected Texture texture;
    protected Texture textureDeath;
    protected String name;
    protected Integer hp;
    protected String hpStr;
    protected int maxHp;
    protected HealthLine hl;
    protected BitmapFont font;
    protected int level;
    protected int exp;
    protected int needExp;
    protected int mana;
    protected int maxMana;
    protected Label hpLabel;
    protected boolean isAlive;
    protected ArrayList<Effect> effects;
    protected SkillsFactory skillsFactory;

    protected Vector2 position;
    protected Rectangle rect;
    protected boolean flip;

    protected float attackAction;
    protected float takeDamageAction;

    public void setAttackAction(float attackAction) {
        this.attackAction = attackAction;
    }

    public AbstractUnit getTarget() {
        return target;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public GameScreen getGame() {
        return game;
    }

    public int getLevel() { return level; }

    public Vector2 getPosition() { return position; }

    public Rectangle getRect() { return rect; }

    public boolean isAlive() { return isAlive; }

    public void addEffect(Effect effect){
        effects.add(effect);
    }

    public void setTarget(AbstractUnit target) {
        this.target = target;
    }

    public AbstractUnit(GameScreen game, Vector2 position, Texture texture, Texture textureDeath){
        skillsFactory = new SkillsFactory();
        this.isAlive=true;
        this.game = game;
        this.position = position;
        this.texture = texture;
        this.textureDeath = textureDeath;
        this.rect = new Rectangle(position.x, position.y, texture.getWidth(), texture.getHeight());
        this.effects = new ArrayList<Effect>();
    }

    public SkillsFactory getSkillsFactory() {
        return skillsFactory;
    }

    public Texture getTexture() {
        return texture;
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
        skillsFactory.levelUp(level);
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
            game.getInfoSystem().addMessage(couse + " -" + value, FlyingText.Colors.RED, this);
            takeDamageAction = 1.0f;
        }
        if (value >= 0) {
            game.getInfoSystem().addMessage(couse + " + " + value, FlyingText.Colors.GREEN, this);
        }
    }

    public void miss(){
        game.getInfoSystem().addMessage("MISS",FlyingText.Colors.WHITE, this);
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
        font = new BitmapFont();
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
        textureFront = new Texture("HealthLine.png");
        textureBack = new Texture("HealthLineBack.png");
        labelStyleWhite = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
    }
}

