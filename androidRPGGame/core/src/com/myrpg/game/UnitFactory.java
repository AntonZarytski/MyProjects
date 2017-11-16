package com.myrpg.game;

import com.badlogic.gdx.graphics.Texture;
import com.myrpg.game.action.BaseAction;
import com.myrpg.game.action.DefenceStanceAction;
import com.myrpg.game.action.MelleAtackAction;
import com.myrpg.game.action.RestAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Natallia && Anton on 15.11.2017.
 */

public class UnitFactory {
    public enum UnitType{
        WARIOR, DD, WIZARD
    }

    private BattleScreen battleScreen;
    private Map<UnitType, Unit> data;
    private List<Autopilot> aiBank;
    private List<BaseAction> actions;

    public List<BaseAction> getActions() {
        return actions;
    }

    public UnitFactory(BattleScreen battleScreen) {
        this.battleScreen = battleScreen;
        this.createActions();
        this.aiBank = new ArrayList<Autopilot>();
        this.aiBank.add(new Autopilot() {
            @Override
            public boolean turn(Unit me) {
                if(!me.getBattleScreen().canIMakeTurn()){
                    return false;
                }
                Unit target = null;
                do{
                    target = me.getBattleScreen().getUnits().get((int)(Math.random()*me.getBattleScreen().getUnits().size()));
                }while (target.isAI());
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
    }

    public void createUnitPatterns(){
        data = new HashMap<UnitType, Unit>();
        SkillsFactory wariorSkills = new SkillsFactory(0,1);
        Unit warior = new Unit(Assets.getInstance().getAssetManager().get("Warrior.png", Texture.class),
                Assets.getInstance().getAssetManager().get("WarriorDeath.png", Texture.class),
                wariorSkills);
        warior.getActions().add(actions.get(0));
        warior.getActions().add(actions.get(1));
        warior.getActions().add(actions.get(2));
        data.put(UnitType.WARIOR, warior);

        SkillsFactory ddSkills = new SkillsFactory(1,1);
        Unit dd = new Unit(Assets.getInstance().getAssetManager().get("DD.png", Texture.class),
                Assets.getInstance().getAssetManager().get("DDDeath.png", Texture.class),
                ddSkills);
        dd.getActions().add(actions.get(0));
        dd.getActions().add(actions.get(1));
        dd.getActions().add(actions.get(2));
        data.put(UnitType.DD, dd);

        SkillsFactory wizardSkills = new SkillsFactory(2,1);
        Unit wizard = new Unit(Assets.getInstance().getAssetManager().get("Wizard.png", Texture.class),
                Assets.getInstance().getAssetManager().get("WizardDeath.png", Texture.class),
                wizardSkills);
        wizard.getActions().add(actions.get(0));
        wizard.getActions().add(actions.get(1));
        wizard.getActions().add(actions.get(2));
        data.put(UnitType.WIZARD, wizard);
    }

    private Unit createUnit(UnitType unitType, boolean isHuman){
        Unit unitPattern = data.get(unitType);
        Unit unit = new Unit(unitPattern.getTexture(), unitPattern.getTextureDeath(), (SkillsFactory)unitPattern.getSkills().clone());
        unit.setActions(unitPattern.getActions());
        if(!isHuman)unit.setAutopilot(aiBank.get(0));
        return unit;
    }
    public void createUnitAndAddToBattle(UnitType unitType, BattleScreen battleScreen, boolean isHuman, int x, int y){
        Unit unit = createUnit(unitType, isHuman);
        unit.setToMap(battleScreen, x, y);
        if(!isHuman)unit.setFlip(true);
        battleScreen.getUnits().add(unit);
    }
}
