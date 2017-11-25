package com.myrpg.game.effects;

import com.myrpg.game.FlyingText;
import com.myrpg.game.Unit;

public class DefenceStanceEffect extends Effect{

    public void start (Unit unit, int rounds, int change){
        super.start(unit, rounds, change);
        skillsFactory.addDefence(change);
        unit.getBattleScreen().getInfoSystem().addMessage("Deffence +" + change,  FlyingText.Colors.GREEN, unit);
    }

    public void end() {
        skillsFactory.deductDefence(change);
        unit.getBattleScreen().getInfoSystem().addMessage("Deffence -"+  change,  FlyingText.Colors.GREEN, unit);

    }
}
