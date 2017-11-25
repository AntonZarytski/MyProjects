package com.myrpg.game.action;

import com.myrpg.game.Unit;

public class RestAction extends BaseAction {
    public RestAction() {
        super("REST", "HealButton");
    }
    @Override
    public boolean action(Unit me) {
        me.changeHP((int) (me.getMaxHp() * 0.15f), "Rest");
        return true;
    }
}
