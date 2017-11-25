package com.myrpg.game.action;

import com.myrpg.game.Calculator;
import com.myrpg.game.Unit;

public class MelleAtackAction extends BaseAction {
    public MelleAtackAction() {
        super("ATTACK", "AttackButton");
    }

    @Override
    public boolean action(Unit me) {
        if(me.getTarget() == null) return false;
        if(me.isMyTeamMate(me.getTarget())) return false;
        float critAttack = 1;
        me.setAttackAction(1.0f);
        me.setCurrentAnimation(Unit.AnimationType.ATTACK);
        if (!Calculator.isTargetEvaded(me, me.getTarget())) {
            if (Calculator.isAtackerCrit(me, me.getTarget())) {
                critAttack = 1.5f;
            }
            int dmg = (int) (Calculator.getMeleeDamage(me, me.getTarget()) * critAttack);
            if (critAttack == 1.5f) {
              me.getTarget().changeHP(-dmg, "CRIT!");
            }else {
                me.getTarget().changeHP(-dmg, "");
            }
        }else me.getTarget().miss();
        return true;
    }
}
