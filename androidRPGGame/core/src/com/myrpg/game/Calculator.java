package com.myrpg.game;

public class Calculator {
    public static int getMeleeDamage(Unit atacker, Unit target){
        int dmg = atacker.getSkills().getStrenght()-(int)(atacker.getSkills().getStrenght()*(+target.getSkills().getDefence()/100.0f));
        dmg = (int)(dmg*0.8f + (float)dmg * Math.random()*0.4f);
        if(dmg<1)dmg=1;
        return dmg;

    }

    public static int getFireballDamage(Unit atacker, Unit target){
        int dmg = atacker.getSkills().getSpellpower();
        dmg = (int)(dmg*0.9f + (float)dmg * Math.random()*0.5f);
        if(dmg<1)dmg=1;
        return dmg;
    }
    public static boolean isTargetEvaded(Unit atacker, Unit target){
        float chanceToMiss = (target.getSkills().getDexterity() + target.getLevel()) /100f;
        if(Math.random() < chanceToMiss){
            return true;
        }else return false;
    }
    public static boolean isAtackerCrit(Unit atacker, Unit target){
        float chanceToAttack = (atacker.getSkills().getDexterity()+atacker.getLevel())/100f;
        if(chanceToAttack > Math.random()){
            return true;
        }else return false;
    }
}
