package com.myrpg.game.action;

import com.myrpg.game.Unit;

public abstract class BaseAction {
    String name;
    String textureName;

    public BaseAction(String name, String textureName) {
        this.name = name;
        this.textureName = textureName;
    }


    public abstract boolean action(Unit me);

    public String getTextureName() {
        return textureName;
    }

    public String getName() {
        return name;
    }
}
