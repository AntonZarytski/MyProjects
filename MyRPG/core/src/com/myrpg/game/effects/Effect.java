package com.myrpg.game.effects;

import com.myrpg.game.GameScreen;
import com.myrpg.game.InfoSystem;
import com.myrpg.game.AbstractUnit;
import com.myrpg.game.SkillsFactory;

public abstract class Effect {
    private GameScreen game;
    protected InfoSystem infoSystem;
    protected AbstractUnit unit;
    protected int rounds;
    protected SkillsFactory skillsFactory;
    protected int change;

    public void start (GameScreen game, AbstractUnit unit, int rounds, int change) {
        this.infoSystem = game.getInfoSystem();
        this.unit = unit;
        skillsFactory =unit.getSkillsFactory();
        this.rounds = rounds;
        this.change = change;
    }

    public void tick(){this.rounds--;}
    public boolean isEnded() {return rounds == 0;}
    public abstract void end();
}
