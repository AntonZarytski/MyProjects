package com.myrpg.game;

public class SkillsFactory {
    //private int typeOfUnit;
    private int strenght;
    private int defence;
    private int endurance;
    private int dexterity;
    private int spellpower;
    private int knowlege;
    private float levelK;
    // type = 0 - воин(акцент на выносливость и защиту)
    // type = 1 - дд(акцент на уроне и ловкости)
    // type = 2 - маг (акцент на силе магии и знании)
    public SkillsFactory(int typeOfUnit, int level){
        if(typeOfUnit == 0){
            this.strenght = 5;
            this.defence = 5;
            this.endurance = 5;
            this.dexterity =  3;
            this.spellpower = 1;
            this.knowlege = 1;
        }
        if(typeOfUnit == 1){
            this.strenght = 6;
            this.defence = 2;
            this.endurance = 3;
            this.dexterity = 4;
            this.spellpower = 3;
            this.knowlege = 2;
        }
        if(typeOfUnit == 2){
            this.strenght = 4;
            this.defence = 1;
            this.endurance = 2;
            this.dexterity = 1;
            this.spellpower = 6;
            this.knowlege = 6;
        }
        for (int i = 1; i <level ; i++) {
            this.levelUp(i);
        }
        System.out.println("strenght = "+strenght);
        System.out.println("defence = "+defence);
        System.out.println("endurance = "+endurance);
        System.out.println("dexterity = "+dexterity);
        System.out.println("spellpower = "+spellpower);
        System.out.println("knowlege = "+knowlege);
        System.out.println();
    }
    public SkillsFactory(){ }
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
}

