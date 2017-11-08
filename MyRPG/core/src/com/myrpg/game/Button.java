package com.myrpg.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class Button {
    private Texture texture;
    private Rectangle rect;
    private String action;

    Button(String action, Texture texture, Rectangle rect){
        this.texture = texture;
        this.rect = rect;
        this.action = action;
    }
    public boolean checkClick(){
        if(InputHandler.checkClickInRect(rect)){
            action();
            return true;
        }
        return false;
    }
    public void render(SpriteBatch batch){
        batch.draw(texture, rect.getX(), rect.getY());
    }

    public String getAction() {
        return action;
    }
    public abstract void action();
}
