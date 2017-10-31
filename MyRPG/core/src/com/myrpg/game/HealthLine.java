package com.myrpg.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class HealthLine {
    private Texture texture;
    private Vector2 position;

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public HealthLine(){
        texture = new Texture("HealthLine.png");
    }
    public void render(SpriteBatch batch){
        batch.draw(texture, position.x++, position.y);
    }
}
