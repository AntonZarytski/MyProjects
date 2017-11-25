package com.myrpg.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class InfoSystem {
    private FlyingText[] msgs;

    public InfoSystem(){
        this.msgs = new FlyingText[20];
        for (int i = 0; i < msgs.length; i++) {
            msgs[i] = new FlyingText();
        }
    }

    public void addMessage(String text, FlyingText.Colors color, float x, float y) {
        for (int i = 0; i < msgs.length; i++) {
            if (!msgs[i].isActive()) {
                msgs[i].setup(text,color, x, y);
                break;
            }
        }
    }
    public void addMessage(String text,FlyingText.Colors color, AbstractUnit abstractUnit){
        addMessage(text, color, abstractUnit.getPosition().x + abstractUnit.getTexture().getWidth()*0.8f, abstractUnit.getPosition().y + abstractUnit.getTexture().getHeight()*0.8f );
    }

    public  void render(SpriteBatch batch){
        for (int i = 0; i < msgs.length; i++) {
            if (msgs[i].isActive()) {
                msgs[i].render(batch);
            }
        }
    }
    public void update(float dt){
        for (int i = 0; i < msgs.length; i++) {
            if (msgs[i].isActive()) {
                msgs[i].update(dt);
            }
        }
    }
}
