package com.myrpg.game.effects;

import com.myrpg.game.FlyingText;
import com.myrpg.game.screens.BattleScreen;
import com.myrpg.game.Unit;

public class RegenerationEffect extends Effect {
    public void start (Unit unit, int rounds, int change){
        super.start(unit, rounds, change);
        unit.getBattleScreen().getInfoSystem().addMessage("Regeneration + " + change+ " per round", FlyingText.Colors.GREEN, unit);
    }
    public void tick(){
        super.tick();
        unit.changeHP(unit.getMaxHp()*change, "Regeneration");
    }
    public void end() {
        unit.getBattleScreen().getInfoSystem().addMessage("Regeneration end", FlyingText.Colors.GREEN, unit);
    }
}
