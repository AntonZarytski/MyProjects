package com.myrpg.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;


public class Hero extends Person {

    public Hero(GameScreen game, Vector2 position){
        super(game, position, new Texture("Hero.png"), new Texture("HeroDeath.png"));
        this.skillz = new Skillz(7, 3,30,3,1,1 );
        this.maxHp = skillz.getEndurance()*10;
        this.hp=maxHp;
        this.name="Hero";
        this.level=1;
        this.flip = false;
        createHelathLine();
    }

}
