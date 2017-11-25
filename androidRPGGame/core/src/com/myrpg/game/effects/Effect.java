package com.myrpg.game.effects;
import com.myrpg.game.Unit;
import com.myrpg.game.SkillsFactory;

import java.io.Serializable;

public abstract class Effect implements Serializable {
    protected Unit unit;
    protected int rounds;
    protected SkillsFactory skillsFactory;
    protected int change;

    public void start (Unit unit, int rounds, int change) {
        this.unit = unit;
        skillsFactory = unit.getSkills();
        this.rounds = rounds;
        this.change = change;
    }

    public void tick(){this.rounds--;}
    public boolean isEnded() {return rounds == 0;}
    public abstract void end();
}
