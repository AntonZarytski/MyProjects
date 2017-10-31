package com.myrpg.game;

import com.badlogic.gdx.graphics.Texture;


public class Hero extends Person {

    public Hero(){
        this.texture = new Texture("Hero.png");
        this.maxHp = 100;
        this.name="Hero";
        this.level=1;
        this.strenght=10;
        this.dexterity=10;
        this.endurance=10;
        this.spellpower =10;
        this.defence = 5;
        this.flip = false;
    }



}
