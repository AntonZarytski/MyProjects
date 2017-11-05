package com.myrpg.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public abstract class Person {
    protected Texture texture;
    protected Texture textureDeath;
    protected String name;
    protected Integer hp;
    protected String hpStr;
    protected final String miss= "miss!";
    protected boolean isMiss;
    protected boolean iscritAttack;
    protected int maxHp;
    protected HealthLine hl;
    protected BitmapFont font;
    protected Label hpLabel;
    protected boolean isAlive;

    protected int level;
        //Primary skills
    protected int strenght;
    protected int dexterity;
    protected int endurance;
    protected int spellpower;
    protected int knowlege;
        //secondary skills
    protected int defence;


    protected Vector2 position;
    protected Rectangle rect;
    protected boolean flip;
    protected float attackAction;
    protected float takeDamageAction;

    public Person (Vector2 position, Texture texture, Texture textureDeath){
        this.isAlive=true;
        this.position = position;
        this.texture = texture;
        this.textureDeath = textureDeath;
        this.rect = new Rectangle(position.x, position.y, texture.getWidth(), texture.getHeight());
    }

    public void setPosition(Vector2 position) {
        this.position = position;
        this.rect = new Rectangle(position.x, position.y, texture.getWidth(), texture.getHeight());

    }

    public Texture getTexture() {
        return texture;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Rectangle getRect() {
        return rect;
    }

    public void createHelathLine(){
        font = new BitmapFont();
        hl = new HealthLine();
        hpStr = hp.toString();
        hl.setPosition( new Vector2((position.x+texture.getWidth()*0.25f), (position.y+texture.getHeight()*1.05f)));
        hpLabel = new Label(hpStr, hl.labelStyleWhite);
    }
    public abstract void getTurn();

    public void takeDamage(int dmg){
        this.takeDamageAction = 1.0f;
        hp-=dmg;
    }
    public void meleeAttack(Person enemy){
        float critAttack=1;
        this.attackAction = 1.0f;
        float chanceToMiss = (enemy.dexterity + enemy.level) /100;
        float chanceToAttack = (this.dexterity+this.level )/100;
        if(chanceToAttack>chanceToMiss){
            if(Math.random() < chanceToMiss){
                isMiss=true;
                return;
            }
        }else {
            if(Math.random() < chanceToAttack){
                iscritAttack = true;
                critAttack=1.25f;
            }
        }
        int dmg = Math.round((this.strenght - enemy.defence)*critAttack);
        critAttack = 1;
        if(dmg<0){
            dmg=0;
        }
        enemy.takeDamage(dmg);
    }
    public void render(SpriteBatch batch){
        float dx = 10f*(float)Math.sin(1f - attackAction)*3.14f;
        if(!flip) dx*=-1;
        float scaleX = 100*hp*1.0f/maxHp;
        if(scaleX<1)scaleX=0;
        hpStr = hp.toString();
        if(isAlive) {
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

        if(isMiss && takeDamageAction>0) {
            hpLabel.setText(miss);
            hpLabel.setX(position.x+texture.getWidth()*1.25f);
            hpLabel.setY(position.y+texture.getHeight());
            hpLabel.draw(batch, 1f-takeDamageAction);
            if(takeDamageAction>0)
            isMiss=false;
        }
        batch.setColor(1f,1f,1f,1f);
    }
    public void update(float dt){
        if(takeDamageAction > 0){
            takeDamageAction-=dt;
        }
        if(attackAction > 0){
            attackAction-=dt;
        }
        if(hp<=0){
            isAlive = false;
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
}
