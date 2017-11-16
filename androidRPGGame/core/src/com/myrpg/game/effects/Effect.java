package com.myrpg.game.effects;

import com.myrpg.game.BattleScreen;
import com.myrpg.game.InfoSystem;
import com.myrpg.game.Unit;
import com.myrpg.game.SkillsFactory;

public abstract class Effect {
    private BattleScreen game;
    protected InfoSystem infoSystem;
    protected Unit unit;
    protected int rounds;
    protected SkillsFactory skillsFactory;
    protected int change;

    public void start (BattleScreen game, Unit unit, int rounds, int change) {
        this.infoSystem = game.getInfoSystem();
        this.unit = unit;
        skillsFactory =unit.getSkills();
        this.rounds = rounds;
        this.change = change;
    }

    public void tick(){this.rounds--;}
    public boolean isEnded() {return rounds == 0;}
    public abstract void end();
}
