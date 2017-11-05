package com.myrpg.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;


public class Enemy extends Person {
private Hero target;
private float sleepTimer;
    public Enemy(runGame game, Vector2 position, Hero target){
        super(game, position, new Texture("Enemy.png"), new Texture("EnemyDeath.png"));
        this.target = target;
        this.maxHp = 50;
        this.hp=maxHp;
        this.name="Enemy";
        this.level=1;
        this.strenght=3;
        this.dexterity=2;
        this.endurance=1;
        this.spellpower =0;
        this.defence = 1;
        this.flip = true;
        createHelathLine();
    }

    @Override
    public void getTurn() {
        sleepTimer = 1.0f;
    }

    public boolean ai(float dt){
        if(sleepTimer>0.0f){
            sleepTimer-=dt;
            return false;
        }
        meleeAttack(target);
        return true;
    }

}
