package com.myrpg.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;


public class Hero extends Person {

    public Hero(runGame game, Vector2 position){
        super(game, position, new Texture("Hero.png"), new Texture("HeroDeath.png"));
        this.maxHp = 100;
        this.hp=maxHp;
        this.name="Hero";
        this.level=1;
        this.strenght=10;
        this.dexterity=80;
        this.endurance=1;
        this.spellpower =1;
        this.defence = 1;
        this.knowlege = 5;
        this.flip = false;
        createHelathLine();
    }


    @Override
    public void getTurn() {

    }
}
