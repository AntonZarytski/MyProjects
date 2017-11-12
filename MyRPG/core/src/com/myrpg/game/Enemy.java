package com.myrpg.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.myrpg.game.action.BaseAction;


public class Enemy extends Person {
    public Enemy(GameScreen game, Vector2 position, Hero target){
        super(game, position, new Texture("Enemy.png"), new Texture("EnemyDeath.png"));
        this.skillz = new Skillz(5, 1,2,1,0,0 );
        this.target = target;
        this.maxHp = skillz.getEndurance()*10;
        this.hp=maxHp;
        this.name="Enemy";
        this.level=1;
        this.flip = true;
        createHelathLine();
    }

    public boolean ai(float dt, BaseAction mainAction){
        if(!game.canIMakeTurn()) return false;
      /*  if(1.0f*this.hp/this.maxHp*100<40*Math.random() && Math.random()>0.33f){
            changeHP(15, "Rest");
            return true;
        }
        if(Math.random()>0.9f){
            deffenceStance(1);
            return true;
        }
        meleeAttack(target);*/

        return mainAction.action(this);
    }

}
