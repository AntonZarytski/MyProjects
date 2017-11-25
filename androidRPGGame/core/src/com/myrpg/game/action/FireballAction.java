package com.myrpg.game.action;

import com.myrpg.game.Calculator;
import com.myrpg.game.Unit;

public class FireballAction extends BaseAction {
    public FireballAction() {
        super("FIREBALL", "FireballButton");
    }

    @Override
    public boolean action(Unit me) {
        if (me.getTarget() == null) return false;
        if (me.isMyTeamMate(me.getTarget())) return false;
        me.setAttackAction(1.0f);
        me.setCurrentAnimation(Unit.AnimationType.ATTACK);
            int dmg = Calculator.getFireballDamage(me, me.getTarget());
            me.getTarget().changeHP(-dmg, "");
            me.getBattleScreen().getSpecialFXEmitter().setup(me, me.getTarget(), 1.0f, 2f, 2f, 0.0f, true);
            me.getBattleScreen().getSpecialFXEmitter().setup(me.getTarget(), me.getTarget(), 1.0f, 1f, 20f, 1.0f, true);
            return true;
        }
    }
