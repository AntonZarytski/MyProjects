package com.myrpg.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;


public class InputHandler {
    public static int getX(){
        return Gdx.input.getX();
    }
    public static int getY(){
        return 720 - Gdx.input.getY();
    }
    public static boolean checkClickInRect(Rectangle rect){
        if(Gdx.input.justTouched()){
            if(rect.contains(getX(), getY())){
                return true;
            }
        }
        return false;
    }
}
