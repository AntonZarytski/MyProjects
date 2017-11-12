package com.myrpg.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class Button {
    private Texture texture;
    private Rectangle rect;

    Button(Texture texture, Rectangle rect){
        this.texture = texture;
        this.rect = rect;
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
    public abstract void action();
}
