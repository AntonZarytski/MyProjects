package com.myrpg.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class FlyingText {
    public enum  Colors{
        RED(1.0f, 0.0f, 0.0f), GREEN(0.0f, 0.7f, 0.0f), WHITE(1.0f, 1.0f, 1.0f);
        float r;
        float g;
        float b;
        Colors(float r, float g, float b){
            this.r = r;
            this.g = g;
            this.b = b;
        }
    }
    private String text;
    private float time;
    private Vector2 position;
    private boolean active;
    private BitmapFont font;
    private Colors color;

    public FlyingText() {
        font = new BitmapFont(Gdx.files.internal("font.fnt"));
        this.position = new Vector2(0, 0);
        this.text = "";
        this.time = 0.0f;
        this.color = Colors.WHITE;

    }

    public boolean isActive(){
        return active;
    }

    public void render (SpriteBatch batch){
        font.setColor(color.r, color.b, color.b, 1.0f-time/2.0f);
        font.draw(batch, text, position.x, position.y);
        font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public void setup( String text, Colors color, float x, float y){
        this.active = true;
        this.position.set(x, y);
        this.text = text;
        this.color = color;
    }

    public void update(float dt){
        position.add(25*dt, 50*dt);
        time +=dt;
        if(time > 2.0f){
            time = 0.0f;
            active = false;
        }
    }
}