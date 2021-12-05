package game.model;

import game.Server;
import game.io.Logger;

import java.io.*;
import java.net.Socket;

public class ClientThread extends Thread {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String userName;
    private final ClientList clients;
    private GameList games;
    private GameThread currentGameThread;
    private int id;
    private Player player;

    private String currentRequest = "";
    private int times = 0;


    public ClientThread(Socket s, ClientList clientList, GameList gameList, int id) throws IOException {
        clients = clientList;
        games = gameList;
        clients.add(this);
        socket = s;
        this.id = id;
        player = new Player(id);
        userName = "Player" + id;
        System.out.println("client " + userName + " connected!");
        out = new PrintWriter(s.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(s.getInputStream()));

        out.println("@id" + id);
    }

    public void run() {
		Logger logger = new Logger();
		boolean needExit = false;
        try {
            String message;
            while(!needExit) {
                message = in.readLine();
                if(message.equals("")) {
                    continue;
                }
                else if(message.startsWith("@updateGameView")) {
                    sendMessage(message);
                }
                else if(message.startsWith("@createGame")) {
                    String temp = message.substring(12);
                    String[] parameters = temp.split(" ");
                    if(parameters.length > 2) {
                        createNewGame(Integer.parseInt(parameters[0]), Integer.parseInt(parameters[1]), Integer.parseInt(parameters[2]));
                    }
                }
                else if(message.startsWith("@gameListToString")) {
                    gameListToString();
                }
                else if(message.startsWith("@joinToGame")) {
                    int num = Integer.parseInt(message.substring(12));
                    joinToGame(num);
                }
                else if(message.startsWith("@updateGameState")) {
                    out.println("@updateGameState#" + currentGameThread.getGame().toString() + "#" + player.allInfo());
                }
                else if(message.startsWith("@serverGameState")) {
                    String[] parameters = message.split("#");
                    currentGameThread.setGame(new Game(parameters[1]));
                    setPlayer(new Player(parameters[2]));
                    sendMessage("@updateGameState#" + currentGameThread.getGame().toString() + "#" + player.allInfo());
                }
                else if(message.startsWith("@setFinalViewAll")) {
                    sendMessage(message);
                    player = null;
                    currentGameThread.interrupt();
                    games.remove(currentGameThread);
                    currentGameThread = null;
                }
				else if(message.startsWith("@quit")) {
					needExit = true;
					socket.close();
					logger.toLog("Client " + userName + " disconnected from the server.");
				}
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        PrintWriter out;
        synchronized (currentGameThread.getClientsInGame()) {
            for (ClientThread client : currentGameThread.getClientsInGame().getList()) {
                if (client == this) {
                    continue;
                }
                out = client.getWriter();
                out.println(message);
            }
        }
    }

    public void createNewGame(int gameType, int cardsCount, int playersCount) {
        GameSettings settings = new GameSettings(gameType, cardsCount, playersCount);
        try {
			int number = Server.gamesCount();
            GameThread newGame = new GameThread(settings, number);
            newGame.start();
            games.add(newGame);
			
			Logger logger = new Logger();
			logger.toLog("Game " + number + " created by " + userName);
			
            joinToGame(games.getList().get(games.size() - 1).getNumber());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void joinToGame(int num) {
        for (int i = 0; i < games.size(); i++) {
            if(games.getList().get(i).getNumber() == num) {
                currentGameThread = games.getList().get(i);
                games.getList().get(i).acceptToGame(this);
				
				Logger logger = new Logger();
				logger.toLog("Client " + userName + " join to game #" + num + ".");
                break;
            }
        }
    }

    public void gameListToString() {
        StringBuilder result = new StringBuilder("@gameListToString" + games.size() + ":");
        int i = 0;
        for (; i < games.size() - 1; i++) {
            result.append(games.getList().get(i).toString()).append(":");
        }
        if(games.size() > 0) {
            result.append(games.getList().get(i).toString());
        }
        out.println(result.toString());
    }

    public PrintWriter getWriter() {
        return out;
    }

    public String getUsername() {
        return userName;
    }

    public int getID() {
        return id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

}
