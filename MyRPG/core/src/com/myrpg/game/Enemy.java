package com.myrpg.game;

import com.badlogic.gdx.graphics.Texture;
public class Enemy extends Person {

    public Enemy(){
        this.texture = new Texture("Enemy.png");
        this.maxHp = 20;
        this.name="Enemy";
        this.level=1;
        this.strenght=1;
        this.dexterity=1;
        this.endurance=1;
        this.spellpower =0;
        this.defence = 0;
        this.flip = true;
    }


}
