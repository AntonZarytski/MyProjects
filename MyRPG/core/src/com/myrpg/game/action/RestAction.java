package com.myrpg.game.action;

import com.badlogic.gdx.graphics.Texture;
import com.myrpg.game.AbstractUnit;

public class RestAction extends BaseAction {
    public RestAction() {
        btnTexture = new Texture("Heal.png");
    }
    @Override
    public boolean action(AbstractUnit me) {
        me.changeHP((int) (me.getMaxHp() * 0.15f), "Rest");
        return true;
    }
}
