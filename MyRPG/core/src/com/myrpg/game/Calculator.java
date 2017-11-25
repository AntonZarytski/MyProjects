package com.myrpg.game;

public class Calculator {
    public static int getMeleeDamage(AbstractUnit atacker, AbstractUnit target){
        int dmg = atacker.getSkillsFactory().getStrenght()-(int)(atacker.getSkillsFactory().getStrenght()*(+target.getSkillsFactory().getDefence()/100.0f));
        dmg = (int)(dmg*0.8f + (float)dmg * Math.random()*0.4f);
        System.out.println(dmg);
        if(dmg<1)dmg=1;
        return dmg;

    }
    public static boolean getTargetEvaded(AbstractUnit atacker, AbstractUnit target){
        float chanceToMiss = (target.getSkillsFactory().getDexterity() + target.getLevel()) /100f;
        if(Math.random() < chanceToMiss){
            return true;
        }else return false;
    }
    public static boolean getAtackerCrit(AbstractUnit atacker, AbstractUnit target){
        float chanceToAttack = (atacker.getSkillsFactory().getDexterity()+atacker.getLevel())/100f;
        if(chanceToAttack > Math.random()){
            return true;
        }else return false;
    }
}
