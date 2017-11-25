package com.myrpg.game;

import com.myrpg.game.action.BaseAction;
import com.myrpg.game.action.DefenceStanceAction;
import com.myrpg.game.action.FireballAction;
import com.myrpg.game.action.MelleAtackAction;
import com.myrpg.game.action.RestAction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UnitFactory implements Serializable{
    public enum UnitType implements Serializable{
        WARIOR, DD, WIZARD
    }

    private Map<UnitType, Unit> data;
    private List<Autopilot> aiBank;
    private List<BaseAction> actions;

    public List<BaseAction> getActions() {
        return actions;
    }

    public UnitFactory() {
        this.createActions();
        this.aiBank = new ArrayList<Autopilot>();
        this.aiBank.add(new Autopilot() {
            @Override
            public boolean turn(Unit me) {
                if(!me.getBattleScreen().canIMakeTurn()){
                    return false;
                }
                Unit target = null;
                int counter = 0;
                do{
                    target = me.getBattleScreen().getUnits().get((int)(Math.random()*me.getBattleScreen().getUnits().size()));
                    counter++;
                    if(counter > 1000)break;
                }while (me.isMyTeamMate(target) || !target.isAlive());
                me.setTarget(target);
                me.getActions().get(0).action(me);// только атака на врага
                return true;
            }
        });
        this.createUnitPatterns();
    }
    public void createActions(){
        this.actions = new ArrayList<BaseAction>();
        this.actions.add(new MelleAtackAction());
        this.actions.add(new DefenceStanceAction());
        this.actions.add(new RestAction());
        this.actions.add(new FireballAction());
    }

    public void createUnitPatterns(){
        data = new HashMap<UnitType, Unit>();
        SkillsFactory wariorSkills = new SkillsFactory(0);
        Unit warior = new Unit(UnitType.WARIOR, Assets.getInstance().getAtlas().findRegion("WarriorAnim"),
                Assets.getInstance().getAtlas().findRegion("WarriorDeath"),
                wariorSkills);
        warior.getActions().add(actions.get(0));
        warior.getActions().add(actions.get(1));
        warior.getActions().add(actions.get(2));
        data.put(UnitType.WARIOR, warior);

        SkillsFactory ddSkills = new SkillsFactory(1);
        Unit dd = new Unit(UnitType.DD, Assets.getInstance().getAtlas().findRegion("DD"),
                Assets.getInstance().getAtlas().findRegion("DDDeath"),
                ddSkills);
        dd.getActions().add(actions.get(0));
        dd.getActions().add(actions.get(1));
        dd.getActions().add(actions.get(2));
        data.put(UnitType.DD, dd);

        SkillsFactory wizardSkills = new SkillsFactory(2);
        Unit wizard = new Unit(UnitType.WIZARD, Assets.getInstance().getAtlas().findRegion("WizardAnim"),
                Assets.getInstance().getAtlas().findRegion("WizardDeath"),
                wizardSkills);
        wizard.getActions().add(actions.get(3));
        wizard.getActions().add(actions.get(1));
        wizard.getActions().add(actions.get(2));
        data.put(UnitType.WIZARD, wizard);
    }

    public void reloadUnit(Unit unit){
        Unit unitPattern = data.get(unit.getType());
        unit.reload(unitPattern.getTexture(), unitPattern.getTextureDeath(), unitPattern.getActions());
        if(unit.isAI()){
            unit.setAutopilot(aiBank.get(0));
        }
    }

    public Unit createUnit(UnitType unitType, boolean flip, boolean ai, int level){
        Unit unitPattern = data.get(unitType);
        Unit unit = new Unit(unitType, unitPattern.getTexture(), unitPattern.getTextureDeath(), (SkillsFactory)unitPattern.getSkills().clone());
        unit.setActions(unitPattern.getActions());
        if(ai)unit.setAutopilot(aiBank.get(0));
        unit.setFlip(flip);
        unit.setlevel(level);
        return unit;
    }
   /* public void createUnitAndAddToBattle(UnitType unitType, BattleScreen battleScreen,Hero hero, boolean isHuman, float x, float y){
        Unit unit = createUnit(unitType, isHuman);
        unit.setToMap(battleScreen, x, y);
        unit.setHero(hero);
        hero.getTeam().add(unit);
        if(!isHuman)unit.setFlip(true);
        battleScreen.getUnits().add(unit);
    }
    public List<Unit> generateUnits(int unitCountOfTeam, BattleScreen battleScreen, Hero hero){
        for (int j = 2; j <=3 ; j++) {
            for (int k = 0; k <=2 ; k++) {
                    int rand = Math.round((float)Math.random()*2);
                    if(rand == 0){
                        createUnitAndAddToBattle(UnitType.WIZARD, battleScreen, hero, false, j, k);
                    }
                    if(rand == 1){
                        createUnitAndAddToBattle(UnitType.WARIOR, battleScreen, hero, false, j, k);
                    }
                    if(rand == 2){
                        createUnitAndAddToBattle(UnitType.DD, battleScreen, hero, false, j, k);
                    }
            }
        }
        units = battleScreen.getUnits();
//        for (int i = 5 + unitCountOfTeam; i >6 ; i--) {
//            units.remove(i);
//        }
        return battleScreen.getUnits();
    }*/
}
