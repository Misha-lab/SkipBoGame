package game.model;

import java.io.*;
import java.util.ArrayList;

public class GameThread extends Thread {
    private PrintWriter out;
    private BufferedReader in;

    private String currentRequest = "";

    private ClientList clientsInGame;
    //private ArrayList<Client> players;
    private int num;
    private Game game;
    private GameSettings gameSettings;
    public GameThread(GameSettings settings, int number) throws IOException {
        clientsInGame = new ClientList();
        //players = new ArrayList<>();
        num = number;
        gameSettings = settings;
        game = new Game(settings);
        System.out.println("Game " + number + " created!");

    }

    public void acceptToGame(ClientThread clientThread) {
        if(clientsInGame.size() < game.getSettings().getPlayersCount())
            clientsInGame.add(clientThread);
        else return;

        if (clientsInGame.size() == game.getSettings().getPlayersCount()) {
            startTheGame();
            clientThread.getWriter().println("@setGameView");
            clientThread.sendMessage("@setGameView");
        }
        else {
            clientThread.getWriter().println("@setLobbyView");
        }
    }

    public void startTheGame() {
        game.setGameStatus(Game.IN_GAME);
        ArrayList<Player> temp = new ArrayList<>();
        for (int i = 0; i < clientsInGame.size(); i++) {
            Player current = clientsInGame.getList().get(i).getPlayer();
            temp.add(current);
            current.generateCards(gameSettings.getMainCardsCount());
            System.out.println(current.getCurrentCards());
        }
        game.setPlayers(temp);

        for (int i = 0; i < clientsInGame.size(); i++) {
            ClientThread clientThread = clientsInGame.getList().get(i);
            clientThread.getWriter().println("@updateGameState#" + game.toString() + "#" + clientThread.getPlayer().allInfo());
        }

    }


    public ClientList getClientsInGame() {
        return clientsInGame;
    }

    public int getXMoveID() {
        return clientsInGame.getList().get(game.getXMove()).getID();
    }

    public String getCurrentRequest() {
        return currentRequest;
    }

    public void setCurrentRequest(String currentRequest) {
        this.currentRequest = currentRequest;
    }

    public int currentPlayersCount() {
        return clientsInGame.size();
    }

    public GameSettings getSettings() {
        return game.getSettings();
    }

    public Board getBoard() {
        return game.getBoard();
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public int getNumber() {
        return num;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(getNumber()).append("%").append(currentPlayersCount()).append("%").append(getSettings().getPlayersCount())
                .append("%").append(getSettings().getMainCardsCount()).append("%").append(getGame().getGameStatus());
        return result.toString();
    }

}
