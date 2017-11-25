package com.myrpg.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;


public class Hero extends AbstractUnit {

    public Hero(GameScreen game, Vector2 position, int level, int typeOfUnit){
        super(game, position, new Texture("Hero.png"), new Texture("HeroDeath.png"));
        this.level = level;
        this.skillsFactory = new SkillsFactory(typeOfUnit,level);
        this.maxHp = skillsFactory.getEndurance()*10;
        this.maxMana = skillsFactory.getKnowlege()*10;
        this.hp=maxHp;
        this.mana = maxMana;
        this.name="Hero";
        this.flip = false;
        createHelathLine();
    }

}
