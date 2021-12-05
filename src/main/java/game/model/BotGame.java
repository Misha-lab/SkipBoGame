package game.model;

import java.util.ArrayList;

public class BotGame {
    public static final int NOT_STARTED = 0;
    public static final int IN_GAME = 1;
    public static final int FINISHED = 2;

    private final Board board;
    private final GameSettings settings;
    private int xMove;
    private int gameStatus;
    private ArrayList<Bot> bots;


    public BotGame(GameSettings settings) {
        this.settings = settings;
        this.bots = new ArrayList<>();
        xMove = (int) (Math.random() * settings.getPlayersCount());
        gameStatus = NOT_STARTED;
        board = new Board();
    }

    public void setGameStatus(int newGameStatus) {
        gameStatus = newGameStatus;
    }

    public int updateGameStatus() {
        int winner = -1;
        if (gameStatus == IN_GAME) {
            for (int i = 0; i < bots.size(); i++) {
                if (bots.get(i).getMainCards().size() == 0) {
                    winner = i;
                    gameStatus = FINISHED;
                    break;
                }
            }
        }
        return winner;
    }



    public int getXMove() {
        return xMove;
    }

    public int getXMoveID() {
        return bots.get(xMove).getId();
    }

    public void setXMove(int xMove) {
        this.xMove = xMove;
    }

    public GameSettings getSettings() {
        return settings;
    }

    public Board getBoard() {
        return board;
    }

    public void setPlayers(ArrayList<Bot> players) {
        this.bots = players;
    }

    public ArrayList<Bot> getPlayers() {
        return bots;
    }

    public Bot getBot(int i) {
        return bots.get(i);
    }

    public void setPlayer(int i, Bot bot) {
        bots.set(i, bot);
    }

    public int getGameStatus() {
        return gameStatus;
    }
}