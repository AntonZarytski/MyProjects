package com.myrpg.game;

import java.io.Serializable;

public class SkillsFactory implements Cloneable, Serializable{
    //private int typeOfUnit;
    private int baseStrenght;
    private int baseDefence;
    private int baseEndurance;
    private int baseDexterity;
    private int baseSpellpower;
    private int baseKnowlege;
    private float levelK;

    private int strenght;
    private int defence;
    private int endurance;
    private int dexterity;
    private int spellpower;
    private int knowlege;

    private int strenghtPerLevel;
    private int defencePerLevel;
    private int endurancePerLevel;
    private int dexterityPerLevel;
    private int spellpowerPerLevel;
    private int knowlegePerLevel;

    @Override
    protected Object clone(){
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    // type = 0 - воин(акцент на выносливость и защиту)
    // type = 1 - дд(акцент на уроне и ловкости)
    // type = 2 - маг (акцент на силе магии и знании)
    public SkillsFactory(int typeOfUnit){
        // базовые навыки юнита 1го уровня
        if(typeOfUnit == 0){
            this.baseStrenght = 80;
            this.baseDefence = 5;
            this.baseEndurance = 5;
            this.baseDexterity =  3;
            this.baseSpellpower = 1;
            this.baseKnowlege = 1;
        }
        if(typeOfUnit == 1){
            this.baseStrenght = 6;
            this.baseDefence = 2;
            this.baseEndurance = 3;
            this.baseDexterity =  4;
            this.baseSpellpower = 3;
            this.baseKnowlege = 2;
        }
        if(typeOfUnit == 2){
            this.baseStrenght = 4;
            this.baseDefence = 1;
            this.baseEndurance = 2;
            this.baseDexterity =  1;
            this.baseSpellpower = 6;
            this.baseKnowlege = 6;
        }
        this.strenghtPerLevel = 3;
        this.defencePerLevel = 2;
        this.endurancePerLevel = 2;
        this.dexterityPerLevel = 2;
        this.spellpowerPerLevel = 3;
        this.knowlegePerLevel = 1;
    }
    public int getStrenght() {
        return strenght;
    }

    public int getDexterity() {
        return dexterity;
    }

    public int getDefence() { return defence; }

    public int getEndurance() {
        return endurance;
    }

    public int getSpellpower() {
        return spellpower;
    }

    public int getKnowlege() {
        return knowlege;
    }

    public void showInfo(){
        System.out.println("strenght = "+strenght);
        System.out.println("defence = "+defence);
        System.out.println("endurance = "+endurance);
        System.out.println("dexterity = "+dexterity);
        System.out.println("spellpower = "+spellpower);
        System.out.println("knowlege = "+knowlege);
        System.out.println();
    }

    public void addStrenght(int chnge){ strenght=+chnge; }
    public void addDefence(int chnge){
        defence=+chnge;
    }
    public void addEndurance(int chnge){ endurance=+chnge; }
    public void addDexterity(int chnge){
        dexterity=+chnge;
    }
    public void addSpellpower(int chnge){
        spellpower=+chnge;
    }
    public void addKnowlege(int changi){
        knowlege=+changi;
    }
    public void deductStrenght(int chnge){
        strenght=-chnge;
    }
    public void deductDefence(int chnge){
        defence=-chnge;
    }
    public void deductEndurance(int chnge){
        endurance=-chnge;
    }
    public void deductDexterity(int chnge){
        dexterity=-chnge;
    }
    public void deductSpellpower(int chnge){
        spellpower=-chnge;
    }
    public void deductKnowlege(int changi){
        knowlege=-changi;
    }

    // strenght = baseStrebght + (int)(level-1)*strenghtPerLevel;
    public void levelUp(int level){
            if(level<10)levelK=0.3f;
            if(level>10 && levelK < 20)levelK =0.2f;
            if(level>20 && levelK < 20)levelK =0.1f;
            strenght += Math.round(strenght*levelK+level);
            defence += Math.round(defence*levelK+level) ;
            endurance += Math.round(endurance*levelK+level);
            dexterity +=Math.round(dexterity*levelK+level);
            spellpower +=Math.round(spellpower*levelK+level);
            knowlege += Math.round(knowlege*levelK+level);
    }
    public void setLevel(int level) {
        strenght = baseStrenght + (int) ((level - 1) * strenghtPerLevel);
        dexterity = baseDexterity + (int) ((level - 1) * dexterityPerLevel);
        endurance = baseEndurance + (int) ((level - 1) * endurancePerLevel);
        defence = baseDefence + (int) ((level - 1) * defencePerLevel);
        spellpower = baseSpellpower + (int) ((level - 1) * spellpowerPerLevel);
        knowlege = baseKnowlege + (int)((level - 1) *knowlegePerLevel);
    }
}

