package game.model;

import java.io.Serializable;

public class GameSettings implements Serializable {
    public static final int BOT_GAME = 0;
    public static final int ONLINE_GAME = 1;
    public static final int CREATE = 2;
    public static final int JOIN = 3;

    private int gameType;

    private int mainCardsCount = 7;
    private int playersCount = 2;

    public GameSettings(int gameType, int mainCardsCount, int playersCount) {
        this.gameType = gameType;
        this.mainCardsCount = mainCardsCount;
        this.playersCount = playersCount;
    }

    public GameSettings(String string) {
        String[] parameters = string.split(" ");
        gameType = Integer.parseInt(parameters[0]);
        mainCardsCount = Integer.parseInt(parameters[1]);
        playersCount = Integer.parseInt(parameters[2]);
    }

    @Override
    public String toString() {
        return gameType + " " + mainCardsCount + " " + playersCount;
    }

    public int getGameType() {
        return gameType;
    }

    public int getMainCardsCount() {
        return mainCardsCount;
    }

    public int getPlayersCount() {
        return playersCount;
    }
}
