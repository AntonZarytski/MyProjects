package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public class Line {
    private Texture texture;
    private float speed;
    static Vector2 position;
    Circle circle;

    public Line(Texture texture, float x, float y, float speed) {
        this.texture = texture;
        this.position = new Vector2(x, y);
        this.speed = speed;
        circle = new Circle(position, 50);

    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y );
    }

    public void update(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT))
            position.x -= speed*dt;
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            position.x += speed*dt;
        if(position.x<50)
            position.x=50;
        if(position.x>1280-50)
            position.x=1280-50;

    }
}

