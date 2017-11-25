package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Ball {
    private Texture texture;
    private Vector2 direction;
    private Vector2 position;
    float speed;
    int corn;

    public Ball(Texture texture, float x, float y, float speed) {
        this.texture = texture;
        this.position = new Vector2(x, y);
        this.direction = new Vector2(0, 1);
        this.speed=speed;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x-8, position.y-8);
    }
    public void update(float dt) {
        position.sub(direction.nor().scl(speed*dt));
        if(position.y>704) {
            direction.rotate(corn);
            position.sub(direction);
        }
        if(position.y<0) {
            direction.rotate(corn);
            position.sub(direction);
        }
        if(position.x>1264){
            direction.rotate(corn);
            position.sub(direction);
        }
        if(position.x<0){
            direction.rotate(corn);
            position.sub(direction);
        }
        if(position.y < Line.position.y+20 && position.x>Line.position.x&& position.x<Line.position.x+50){
            corn=90;
            direction.rotate(225);
            position.sub(direction);
        }
        if(position.y < Line.position.y+20 && position.x>Line.position.x+50 && position.x< Line.position.x+100){
            corn=270;
            direction.rotate(135);
            position.sub(direction);
        }
    }
}


