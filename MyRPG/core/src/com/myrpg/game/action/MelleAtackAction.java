package com.myrpg.game.action;

import com.badlogic.gdx.graphics.Texture;
import com.myrpg.game.Calculator;
import com.myrpg.game.AbstractUnit;

public class MelleAtackAction extends BaseAction {
    public MelleAtackAction() {
        btnTexture = new Texture("Button.png");
    }

    @Override
    public boolean action(AbstractUnit me) {
        if(me.getTarget() == null) return false;
        if(me.getTarget().getClass().equals(me.getClass())) return false;
        float critAttack = 1;
        me.setAttackAction(1.0f);
        if (!Calculator.getTargetEvaded(me, me.getTarget())) {
            if (Calculator.getAtackerCrit(me, me.getTarget())) {
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
