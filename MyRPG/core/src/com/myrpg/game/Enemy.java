package com.myrpg.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;




public class Enemy extends Person {
private Hero target;
    public Enemy(GameScreen game, Vector2 position, Hero target){
        super(game, position, new Texture("Enemy.png"), new Texture("EnemyDeath.png"));
        this.target = target;
        this.maxHp = 100;
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

    public boolean ai(float dt){
        if(!game.canIMakeTurn()) return false;
        if(1.0f*this.hp/this.maxHp*100<40*Math.random() && Math.random()>0.33f){
            heal(15);
            return true;
        }
        if(Math.random()>0.9f){
            deffenceStance(1);
            return true;
        }
        meleeAttack(target);
        return true;
    }

}
