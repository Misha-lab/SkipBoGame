package SB;

import SB.model.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Server {
    private static final ClientList clients = new ClientList();
    private static final GameList games = new GameList();

    private static int count = 0;

    public static void main(String[] args) throws UnknownHostException {
		Logger logger = new Logger();
		
        int portNumber = 9876;
        if(args.length == 1)
            portNumber = Integer.parseInt(args[0]);

        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(portNumber);
            System.out.println("Server started");
            while(true) {
                Socket clientSocket = serverSocket.accept();
                ClientThread clientThread = new ClientThread(clientSocket, clients, games, count++);
                logger.toLog("Client " + clientThread.getUsername() + " connected to the server.");
				clientThread.start();
                clients.add(clientThread);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int playersCount() {
        return count;
    }
    public static void addGame(GameThread gameThread) {
        games.add(gameThread);
    }

    public static GameList getGameList() {
        return games;
    }

    public static int gamesCount() {
        return games.size();
    }

    public static ClientThread getClientThread(String name) {
        return clients.get(name);
    }
}