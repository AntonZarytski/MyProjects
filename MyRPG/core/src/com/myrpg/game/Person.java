package com.myrpg.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Person {
    protected HealthLine[] hl;
    protected Texture texture;
    protected String name;
    protected int hp;
    protected int maxHp;

    protected int level;
        //Primary skills
    protected int strenght;
    protected int dexterity;
    protected int endurance;
    protected int spellpower;
        //secondary skills
    protected int defence;


    protected Vector2 position;
    protected Rectangle rect;
    protected boolean flip;
    protected float attackAction;
    protected float takeDamageAction;

    public void setPosition(Vector2 position) {
        this.position = position;
        this.rect = new Rectangle(position.x, position.y, texture.getWidth(), texture.getHeight());
    }

    public Rectangle getRect() {
        return rect;
    }
    public void createHealthLine(){
        for (int i = 0; i <hl.length ; i++) {
            hl[i] = new HealthLine((int)position.x+20+i, (int)position.y+520);
        }
    }
    public void renderHealthLine(SpriteBatch batch){
        for (int i = 0; i <hl.length ; i++) {
            hl[i].render(batch);
        }
    }

    public void takeDamage(int dmg){
        this.takeDamageAction = 1.0f;
        hp-=dmg;
    }
    public void meleeAttack(Person enemy){
        int dmg = this.strenght - enemy.defence;
        if(dmg<0){
            dmg=0;
        }
        this.attackAction = 1.0f;
        enemy.takeDamage(dmg);
    }
    public void render(SpriteBatch batch){
        if(takeDamageAction>0) {
            batch.setColor(1f, 1f - takeDamageAction, 1f - takeDamageAction, 1f);
        }
        float dx = 10f*(float)Math.sin(1f - attackAction)*3.14f;
        if(!flip) dx*=-1;
        batch.draw(texture, position.x + dx, position.y, 0,0, texture.getWidth(), texture.getHeight(), 1,1,0,0,0, texture.getWidth(), texture.getHeight(), flip, false);
        batch.setColor(1f,1f,1f,1f);
    }
    public void update(float dt){
        if(takeDamageAction > 0){
            takeDamageAction-=dt;
        }
        if(attackAction > 0){
            attackAction-=dt;
        }
    }
}
