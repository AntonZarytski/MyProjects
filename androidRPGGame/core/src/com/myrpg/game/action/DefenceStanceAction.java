package com.myrpg.game.action;

import com.myrpg.game.Unit;
import com.myrpg.game.effects.DefenceStanceEffect;

public class DefenceStanceAction extends BaseAction {
    public DefenceStanceAction() {
        super("DEFENCE","DefenceButton");

    }

    @Override
    public boolean action(Unit me) {
        DefenceStanceEffect dse = new DefenceStanceEffect();
        dse.start(me, 1, 5);
        me.addEffect(dse);
        return true;
    }
}
