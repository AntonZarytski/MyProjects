package com.myrpg.game;


import java.io.Serializable;

public class Hero implements Serializable{
    private Unit[][] team;
    private boolean haveLiveUnits;

    public Hero() {
        team = new Unit[2][3];
        haveLiveUnits = true;
    }
    public void setArmy(Unit... units) {
        int counter = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                team[j][i] = units[counter];
                if(team[j][i] != null) {
                    team[j][i].setHero(this);
                }
                counter++;

            }
        }
    }

    public Unit[][] getTeam() {
        return team;
    }
    /*private List<Unit> team;
    private boolean haveLiveUnits;

    public Hero() {
        team = new ArrayList<Unit>();
        haveLiveUnits = true;
    }
    public void setArmy(Unit... units){
        this.team.addAll(Arrays.asList(units));
        for (int i = 0; i <team.size() ; i++) {
            team.get(i).setHero(this);
        }
    }
    public List<Unit> getTeam() {
        return team;
    }

    public boolean HaveLiveUnits() {
        for (int i = 0; i <team.size() ; i++) {
            if(team.get(i).isAlive()){
                haveLiveUnits = true;
                return haveLiveUnits;
            }
        }
        haveLiveUnits = false;
        return haveLiveUnits;
    }*/

}
