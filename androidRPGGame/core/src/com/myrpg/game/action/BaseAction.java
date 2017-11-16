package com.myrpg.game.action;

import com.badlogic.gdx.graphics.Texture;
import com.myrpg.game.Unit;

public abstract class BaseAction {
    String name;
    Texture btnTexture;

    public BaseAction(String name, Texture btnTexture) {
        this.name = name;
        this.btnTexture = btnTexture;
    }

    public Texture getBtnTexture() {
        return btnTexture;
    }

    public abstract boolean action(Unit me);

    public String getName() {
        return name;
    }
}
