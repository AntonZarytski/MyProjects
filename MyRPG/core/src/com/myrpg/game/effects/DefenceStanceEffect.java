package com.myrpg.game.effects;

import com.myrpg.game.FlyingText;
import com.myrpg.game.GameScreen;
import com.myrpg.game.AbstractUnit;

public class DefenceStanceEffect extends Effect{

    public void start (GameScreen game, AbstractUnit unit, int rounds, int change){
        super.start(game, unit, rounds, change);
        skillsFactory.addDefence(change);
        infoSystem.addMessage("Deffence +" + change,  FlyingText.Colors.GREEN, unit);
    }

    public void end() {
        skillsFactory.deductDefence(change);
        infoSystem.addMessage("Deffence -"+  change,  FlyingText.Colors.GREEN, unit);

    }
}
