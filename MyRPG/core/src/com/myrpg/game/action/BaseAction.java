package com.myrpg.game.action;

import com.badlogic.gdx.graphics.Texture;
import com.myrpg.game.AbstractUnit;

public abstract class BaseAction {
    Texture btnTexture;

    public Texture getBtnTexture() {
        return btnTexture;
    }

    public abstract boolean action(AbstractUnit me);
}
