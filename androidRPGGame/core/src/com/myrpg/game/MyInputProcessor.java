package com.myrpg.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import org.w3c.dom.css.Rect;

/**
 * Created by Natallia && Anton on 14.11.2017.
 */

public class MyInputProcessor implements InputProcessor {
    private Vector2 cursor;
    private boolean isClicked;

    public MyInputProcessor() {
        this.cursor = new Vector2(0,0);
    }
    public boolean isTouched (){
        return isClicked;
    }

    public float getX(){
        return cursor.x;
    }
    public float getY(){
        return cursor.y;
    }

    public boolean isTouchedInArea(Rectangle rect){
        if(!isTouched())return false;
        return rect.contains(cursor);
    }
    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        cursor.set(screenX, screenY);
        //экранные координаты соотносит с viewport
        ScreenManager.getInstance().getViewport().unproject(cursor);
        isClicked = true;
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        isClicked = false;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
