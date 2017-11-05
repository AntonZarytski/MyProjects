package com.myrpg.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

class FlyingText {
    private String text;
    private float time;
    private Vector2 position;
    private boolean active;
    private BitmapFont font;

    public FlyingText() {
        this.font = new BitmapFont(Gdx.files.internal("font.fnt"));
        this.position = new Vector2(0, 0);
        this.text = "";
        this.time = 0.0f;

    }

    public boolean isActive(){
        return active;
    }

    public void render (SpriteBatch batch){
        font.draw(batch, text, position.x, position.y);
    }

    public void setup( String text, float x, float y){
        this.active = true;
        this.position.set(x, y);
        this.text = text;
    }

    public void update(float dt){
        position.add(25*dt, 50*dt);
        time +=dt;
        if(time > 1.0f){
            time = 0.0f;
            active = false;
        }
    }
}