package com.myrpg.game.effects;

import com.myrpg.game.FlyingText;
import com.myrpg.game.BattleScreen;
import com.myrpg.game.Unit;

public class RegenerationEffect extends Effect {
    public void start (BattleScreen game, Unit unit, int rounds, int change){
        super.start(game, unit, rounds, change);
        infoSystem.addMessage("Regeneration + " + change+ " per round", FlyingText.Colors.GREEN, unit);
    }
    public void tick(){
        super.tick();
        unit.changeHP(unit.getMaxHp()*change, "Regeneration");
    }
    public void end() {
        infoSystem.addMessage("Regeneration end", FlyingText.Colors.GREEN, unit);
    }
}
