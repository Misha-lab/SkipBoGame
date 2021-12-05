package game.view;

import game.Client;
import game.model.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

public class GameListView extends JPanel {
    public Font font = new Font("Sherif", Font.BOLD, 22);
    private JLayeredPane layeredPane;
    private Client client;

    public GameListView(Client client) {
        this.client = client;
        update();
    }

    public void update() {
        try {
            removeAll();
            client.getWriter().println("@gameListToString");
            AllPanels picture = new AllPanels(AllPanels.SETTINGS);
            layeredPane = new JLayeredPane();
            layeredPane.setPreferredSize(new Dimension(picture.getWidth(), picture.getHeight()));
            layeredPane.add(picture, new Integer(10));
            setPreferredSize(new Dimension(picture.getWidth(), picture.getHeight()));
            BufferedImage pic = ImageIO.read(new File("pictures/buttons/cardsCount.png"));

            int i = 0;
            JLabel[] gamePics = new JLabel[client.gamesCount];
            JLabel[] infoLabels = new JLabel[client.gamesCount];
            JLabel[] statusLabel= new JLabel[client.gamesCount];

            for (; i < client.gamesCount; i++) {
                gamePics[i] = new JLabel(new ImageIcon(pic));
                gamePics[i].setBounds(50, 150 + (pic.getHeight() + 20) * i, pic.getWidth(), pic.getHeight());
                String temp = "";

                String[] parameters = client.gameThreads[i].split("%");
                final int gameNumber = Integer.parseInt(parameters[0]);
                int curPlayersCount = Integer.parseInt(parameters[1]);
                int fullPlayersCount = Integer.parseInt(parameters[2]);
                int cardsCount = Integer.parseInt(parameters[3]);
                int gameStatus = Integer.parseInt(parameters[4]);

                if(gameStatus == Game.NOT_STARTED) {
                    temp = "GAME NOT STARTED";
                }
                else if(gameStatus == Game.IN_GAME) {
                    temp = "GAME IN PROCESS";
                }
                else if(gameStatus == Game.FINISHED) {
                    temp = "GAME IS FINISHED";
                }

                infoLabels[i] = new JLabel(curPlayersCount+"/"+fullPlayersCount+ " ----- " + cardsCount + " cards ");
                infoLabels[i].setBounds(50, 150 + (pic.getHeight() + 20) * i, pic.getWidth(), pic.getHeight());
                if((curPlayersCount == fullPlayersCount) || gameStatus != Game.NOT_STARTED) {
                    infoLabels[i].setForeground(Color.RED);
                }
                else {
                    infoLabels[i].setForeground(new Color(0, 153, 73));
                }
                infoLabels[i].setFont(font);
                infoLabels[i].setHorizontalAlignment(JLabel.CENTER);
                infoLabels[i].setVerticalAlignment(JLabel.TOP);

                statusLabel[i] = new JLabel(temp);
                statusLabel[i].setBounds(50, 180 + (pic.getHeight() + 20) * i, pic.getWidth(), pic.getHeight() - 20);
                statusLabel[i].setForeground(Color.BLACK);
                statusLabel[i].setFont(font);
                statusLabel[i].setHorizontalAlignment(JLabel.CENTER);

                gamePics[i].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        client.getWriter().println("@joinToGame " + gameNumber);
                        //client.setPlayer(new Player(client.getId()));
                        //gameThread.acceptToGame(client);
                    }
                });

                layeredPane.add(gamePics[i], new Integer(50));
                layeredPane.add(infoLabels[i], new Integer(100));
                layeredPane.add(statusLabel[i], new Integer(120));
            }

            BufferedImage updatePic = ImageIO.read(new File("pictures/buttons/update.png"));
            JLabel update = new JLabel(new ImageIcon(updatePic));
            update.setBounds(50, 150 + (pic.getHeight() + 20) * i, updatePic.getWidth(), updatePic.getHeight());

            update.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    update();
                    //System.out.println(gameList.size());
                }
            });

            layeredPane.add(update, new Integer(150));

            BufferedImage backButtonPic = ImageIO.read(new File("pictures/buttons/backButton.png"));
            JLabel backButton = new JLabel(new ImageIcon(backButtonPic));
            backButton.setBounds(50, 20, backButtonPic.getWidth(), backButtonPic.getHeight());

            backButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    client.getContentPane().removeAll();
                    client.getContentPane().add(new ChooseModeView(client));
                    client.getContentPane().revalidate();
                }
            });
            layeredPane.add(backButton, Integer.valueOf(100));

            add(layeredPane);
            revalidate();
            client.getContentPane().repaint();

        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    }
}
