package com.myrpg.game;

import com.badlogic.gdx.Gdx;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GameSesion {
    private static final GameSesion ourInstance = new GameSesion();

    public static GameSesion getInstance() {
        return ourInstance;
    }

    private Hero player1;
    //private Hero player2;

    private GameSesion() {
    }

    public void startNewSession() {
        player1 = new Hero();
        makeStandartArmy(player1, false, false);
     //   player2 = new Hero();
     //   makeStandartArmy(player2, true, true);
    }

    public void loadSession(){
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream((Gdx.files.local("mydata.sav").file())));
            player1 = (Hero)ois.readObject();
         //   player2 = (Hero)ois.readObject();
                    ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void saveSession(){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Gdx.files.local("mydata.sav").file()));
            oos.writeObject(player1);
         //   oos.writeObject(player2);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Hero getPlayer1() {
        return player1;
    }
   /* public Hero getPlayer2() {
        return player2;
    }*/

    public void makeStandartArmy(Hero player, boolean isFlip, boolean isAI) {
        UnitFactory factory = new UnitFactory();
        player.setArmy(factory.createUnit(UnitFactory.UnitType.WARIOR, isFlip, isAI, 1),
                factory.createUnit(UnitFactory.UnitType.WARIOR, isFlip, isAI, 1),
                factory.createUnit(UnitFactory.UnitType.WARIOR, isFlip, isAI, 1),
                factory.createUnit(UnitFactory.UnitType.WIZARD, isFlip, isAI, 1),
                factory.createUnit(UnitFactory.UnitType.WIZARD, isFlip, isAI, 1),
                factory.createUnit(UnitFactory.UnitType.DD, isFlip, isAI, 1)
        );
    }
}
