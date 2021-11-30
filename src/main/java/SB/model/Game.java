package SB.model;

import java.util.ArrayList;

public class Game {
    public static final int NOT_STARTED = 0;
    public static final int IN_GAME = 1;
    public static final int FINISHED = 2;

    private final Board board;
    private final GameSettings settings;
    private int xMove;
    private int gameStatus;
    private ArrayList<Player> players;
    private ArrayList<Bot> bots;

    public Game(GameSettings settings) {
        this.settings = settings;
        this.players = new ArrayList<>();
        this.bots = new ArrayList<>();
        xMove = (int) (Math.random() * settings.getPlayersCount());
        gameStatus = NOT_STARTED;
        board = new Board();
    }

    public Game(String string) {
        this.players = new ArrayList<>();
        String[] parameters = string.split(":");
        xMove = Integer.parseInt(parameters[0]);
        gameStatus = Integer.parseInt(parameters[1]);
        settings = new GameSettings(parameters[2]);
        board = new Board(parameters[3]);

        for (int i = 4; i < parameters.length; i++) {
            players.add(new Player(parameters[i]));
        }

    }

    @Override
    public String toString() {
        String result = xMove + ":" + gameStatus + ":" + settings.toString() + ":" + board.toString() + ":";
        for (int i = 0; i < players.size(); i++) {
            if (i == players.size() - 1) {
                result += players.get(i).allInfo();
            }
            else result += players.get(i).allInfo() + ":";
        }
        return result;
    }

    public void setGameStatus(int newGameStatus) {
        gameStatus = newGameStatus;
    }

    public int updateGameStatus() {
        int winner = -1;
        if (gameStatus == IN_GAME) {
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).getMainCards().size() == 0) {
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
        return players.get(xMove).getId();
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

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public void setBots(ArrayList<Bot> bots) {
        this.bots = bots;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public ArrayList<Bot> getBots() {
        return bots;
    }

    public Player getPlayer(int i) {
        return players.get(i);
    }

    public Bot getBot(int i) {
        return bots.get(i);
    }

    public void setPlayer(int i, Player player) {
        players.set(i, player);
    }

    public void setBot(int i, Bot bot) {
        bots.set(i, bot);
    }

    public int getGameStatus() {
        return gameStatus;
    }


}
