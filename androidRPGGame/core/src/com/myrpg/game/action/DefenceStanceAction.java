package com.myrpg.game.action;

import com.badlogic.gdx.graphics.Texture;
import com.myrpg.game.Unit;
import com.myrpg.game.effects.DefenceStanceEffect;
import com.myrpg.game.Assets;

public class DefenceStanceAction extends BaseAction {
    public DefenceStanceAction() {
        super("DEFENCE",Assets.getInstance().getAssetManager().get("DefenceButton.png", Texture.class));
    }

    @Override
    public boolean action(Unit me) {
        DefenceStanceEffect dse = new DefenceStanceEffect();
        dse.start(me.getBattleScreen(), me, 1, 5);
        me.addEffect(dse);
        return true;
    }
}
