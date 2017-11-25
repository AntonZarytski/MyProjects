package com.myrpg.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class DefefenceFX {
    private Vector2 position;
    private float time;
    private float speed;
    private float maxTime;
    private int maxFrames;
    private Texture texture;
    private TextureRegion[] regions;

    public boolean isActive() {
        return time > 0.0f;
    }

    public DefefenceFX() {
        position = new Vector2(0, 0);
        maxFrames = 10;
        speed = 0.05f;
        time = -1.0f;
        maxTime = maxFrames * speed;
        texture = new Texture("EnemyDeffenceSprite.png");
        TextureRegion[][] tr = new TextureRegion(texture).split(197, 300);
        regions = new TextureRegion[maxFrames];
        int counter = 0;
        for (int i = 0; i < tr.length; i++) {
            for (int j = 0; j < tr[0].length; j++) {
                regions[counter] = tr[i][j];
                counter++;
            }
        }
    }

    public void setup(float x, float y) {
        position.set(x, y);
        time = 0.001f;
    }

    public void render(SpriteBatch batch) {
        if (isActive()) {
            batch.draw(regions[(int) (time / speed)], position.x - 99, position.y - 150);
        }
    }

    public void update(float dt) {
        if (isActive()) {
            time += dt;
            if (time > maxTime) {
                time = -1.0f;
            }
        }
    }
}
