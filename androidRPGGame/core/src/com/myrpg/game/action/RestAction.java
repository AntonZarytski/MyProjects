package com.myrpg.game.action;

import com.badlogic.gdx.graphics.Texture;
import com.myrpg.game.Assets;
import com.myrpg.game.Unit;

public class RestAction extends BaseAction {
    public RestAction() {
        super("REST", Assets.getInstance().getAssetManager().get("HealButton.png", Texture.class));
    }
    @Override
    public boolean action(Unit me) {
        me.changeHP((int) (me.getMaxHp() * 0.15f), "Rest");
        return true;
    }
}
