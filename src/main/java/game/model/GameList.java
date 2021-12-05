package game.model;

import java.awt.image.AreaAveragingScaleFilter;
import java.io.Serializable;
import java.util.ArrayList;

public class GameList implements Serializable {
    private ArrayList<GameThread> currentGames;
    public GameList() {
        currentGames = new ArrayList<>();
    }

    public GameList(ArrayList<GameThread> games) {
        currentGames = games;
    }
   /* public synchronized boolean isOnline(String name) {
        for(GameThread gameThread: currentGames) {
            if(gameThread.getUsername().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public synchronized GameThread get(String name) {
        for(GameThread game : currentGames) {
            if(game.getUsername().equals(name)) {
                return game;
            }
        }
        return null;
    }*/

    public int size() { return currentGames.size(); }

    public synchronized void add(GameThread user) {
        currentGames.add(user);
    }

    public synchronized void remove(GameThread user) {
        currentGames.remove(user);
    }

    public ArrayList<GameThread> getList() {
        return currentGames;
    }
}
