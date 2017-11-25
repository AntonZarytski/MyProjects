package com.myrpg.game.action;

import com.badlogic.gdx.graphics.Texture;
import com.myrpg.game.AbstractUnit;
import com.myrpg.game.effects.DefenceStanceEffect;

public class DefenceStanceAction extends BaseAction {
    public DefenceStanceAction() {
        btnTexture = new Texture("Deffence.png");
    }

    @Override
    public boolean action(AbstractUnit me) {
        DefenceStanceEffect dse = new DefenceStanceEffect();
        dse.start(me.getGame(), me, 1, 5);
        me.addEffect(dse);
        return true;
    }
}
