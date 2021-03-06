package game;

import game.model.*;
import game.view.*;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;

public class Client extends JFrame {
    private Socket socket = new Socket();
    private PrintWriter out;
    private BufferedReader in;
    private BufferedReader inFromUser;
    private Player player;
    public int id;
    private String name;
    private final Client client = this;
    public String[] gameThreads;
    public int gamesCount = 0;
    public Game game;
    public boolean isUpdated = false;
	
	private ReaderThread readerThread;

    public String addr;
	public int port;

    public Client(String ip, int port) {
        super("SkipBo");
        addr = ip;
		this.port = port;
		
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        setBounds(0, 0, dimension.width, dimension.height);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setIconImage(new ImageIcon("pictures/icon.png").getImage());
        Container c = getContentPane();
        setLayout(new FlowLayout());
        MenuView menu = new MenuView(client);
        c.add(menu);

        c.setVisible(true);
        setVisible(true);
    }

    public void connectToServer() {
        inFromUser = new BufferedReader(new InputStreamReader(System.in));
        try {
            id = Server.playersCount();
            player = new Player(id);
            socket = new Socket(addr, port);

            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            readerThread = new ReaderThread(in, this);
            readerThread.start();
        } catch (ConnectException ce) {
            ce.printStackTrace();
            Container c = getContentPane();
            c.removeAll();
            MenuView menu = new MenuView(client);
            c.add(menu);
            c.repaint();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
	
	public void disconnectFromServer() {
		out.println("@quit");
		readerThread.interrupt();
		
		/*try {
			out.println("@quit");
			readerThread.interrupt();
			socket.close();
		} catch (IOException ex) {
            ex.printStackTrace();
        }*/
	}

    public PrintWriter getWriter() {
        return out;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setPlayer(Player newPlayer) {
        player = newPlayer;
    }
	
	public Socket getSocket() {
		return socket;
	}

    public static void main(String[] args) {
		String ip = "192.168.100.18";
		int port = 9876;
        if (args.length == 2) {
			ip = args[0];
			port = Integer.parseInt(args[1]);
		}
		new Client(ip, port);
    }

    private static class ReaderThread extends Thread {
        private final BufferedReader in;
        private final Client client;

        public ReaderThread(BufferedReader reader, Client client) {
            in = reader;
            this.client = client;
        }

        public void run() {
			boolean needExit = false;
            String messageFromServer;
            try {
                while (!needExit) {
                    messageFromServer = in.readLine();
                    if (messageFromServer.equals("")) {
                        continue;
                    } /*else if (messageFromServer.equals("@fail")) {
                        System.out.println("Nickname already exist!\n Enter another Nickname!");
                    }*/ else if (messageFromServer.startsWith("@id")) {
                        String temp = messageFromServer.substring(3);
                        client.id = Integer.parseInt(temp);
                        client.name = "Player" + client.id;
                    } else if (messageFromServer.startsWith("@updateGameState")) {
                        String[] parameters = messageFromServer.split("#");
                        client.game = new Game(parameters[1]);
                        client.player = new Player(parameters[2]);
                    } else if (messageFromServer.startsWith("@updateGameView")) {
                        for (int i = 0; i < client.getContentPane().getComponentCount(); i++) {  
                            ((GameView) client.getContentPane().getComponent(i)).gameView();
                            client.isUpdated = false;
                        }
                    } else if (messageFromServer.startsWith("@gameListToString")) {
                        String[] info = messageFromServer.split(":");
                        client.gamesCount = Integer.parseInt(info[0].substring(17));
                        client.gameThreads = new String[client.gamesCount];
                        for (int i = 1; i < info.length; i++) {
                            client.gameThreads[i - 1] = info[i];
                        }
                    } else if (messageFromServer.startsWith("@setGameView")) {
                        client.getContentPane().removeAll();
                        GameView gameView = new GameView(client);
                        client.getContentPane().add(gameView);
                        gameView.revalidate();
                        client.getContentPane().repaint();
                    } else if (messageFromServer.startsWith("@setLobbyView")) {
                        new LobbyView(client);
                    } else if (messageFromServer.startsWith("@setFinalViewAll")) {
                        client.getContentPane().removeAll();
                        FinalView finalView = new FinalView(client, client.game.getPlayers());
                        client.getContentPane().add(finalView);
                        client.getContentPane().repaint();
                        client.revalidate();
                    }
					else if(messageFromServer.startsWith("@quit")) {
						needExit = true;
						client.getSocket().close();
					}
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}