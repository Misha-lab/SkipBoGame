package SB.view;

import SB.Client;
import SB.model.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FinalView extends JPanel {
    private final Client client;
    private final ArrayList<Player> players;

    public FinalView(Client client, ArrayList<Player> players) {
        this.client = client;
        this.players = players;
        finalView();
    }

    public void finalView() {
        removeAll();
        JLayeredPane layeredPane = new JLayeredPane();
        try {
            BufferedImage backPic = ImageIO.read(new File("pictures/backgrounds/backgroundGame.png"));
            JLabel backLabel = new JLabel(new ImageIcon(backPic));
            layeredPane.setPreferredSize(new Dimension(backPic.getWidth(), backPic.getHeight()));
            setPreferredSize(new Dimension(backPic.getWidth(), backPic.getHeight()));
            backLabel.setBounds(0, 0, backPic.getWidth(), backPic.getHeight());
            layeredPane.add(backLabel, Integer.valueOf(10));

            BufferedImage skipBoPic = ImageIO.read(new File("pictures/texts/skipbo_text.png"));
            JLabel skipboLabel = new JLabel(new ImageIcon(skipBoPic));
            skipboLabel.setBounds(backPic.getWidth() - skipBoPic.getWidth() + 100, 0, skipBoPic.getWidth(), skipBoPic.getHeight());
            layeredPane.add(skipboLabel, Integer.valueOf(20));
            int size = client.game.getSettings().getPlayersCount();
            for (int i = 0; i < size; i++) {
                int minIdx = 0;
                int min = 100;
                for (int j = i; j < size; j++) {
                    if (players.get(j).getMainCards().size() < min) {
                        min = players.get(i).getMainCards().size();
                        minIdx = j;
                    }
                }
                Player player = players.get(i);
                players.set(i, players.get(minIdx));
                players.set(minIdx, player);
            }

            for (int i = 0; i < players.size(); i++) {
                System.out.println(players.get(i).getMainCards().size() + "");
            }

            JLabel result;
            if (players.get(0).getId() == client.getId()) {
                result = new JLabel("YOU ARE WINNER!!!:)");
            } else {
                result = new JLabel("YOU ARE LOSER:(");
            }
            result.setBounds(470, 20, 500, 100);
            result.setHorizontalAlignment(JLabel.CENTER);
            result.setFont(new Font("Sherif", Font.BOLD, 40));
            result.setForeground(Color.BLUE);
            layeredPane.add(result, Integer.valueOf(100));

            BufferedImage playerPic = ImageIO.read(new File("pictures/buttons/button0.png"));
            BufferedImage youPic = ImageIO.read(new File("pictures/buttons/button1.png"));
            for (int i = 0; i < size; i++) {
                JLabel icon;
                if (players.get(i).getId() == client.getId()) {
                    icon = new JLabel(new ImageIcon(youPic));
                } else {
                    icon = new JLabel(new ImageIcon(playerPic));
                }
                icon.setBounds(450, 150 + (playerPic.getHeight() + 20) * i, playerPic.getWidth(), playerPic.getHeight());
                layeredPane.add(icon, Integer.valueOf(20));

                Font font = new Font("Sherif", Font.BOLD, 25);

                JLabel playerName = new JLabel("Player" + players.get(i).getId());
                playerName.setBounds(470, 130 + (playerPic.getHeight() + 20) * i, playerPic.getWidth(), playerPic.getHeight());
                playerName.setHorizontalAlignment(JLabel.CENTER);
                playerName.setFont(font);
                playerName.setForeground(Color.BLACK);
                layeredPane.add(playerName, Integer.valueOf(100));

                if (i != 0) {
                    JLabel onlineGameLabel = new JLabel(players.get(i).getMainCards().size() + " cards remaining");
                    onlineGameLabel.setBounds(430, 160 + (playerPic.getHeight() + 20) * i, playerPic.getWidth(), playerPic.getHeight());
                    onlineGameLabel.setHorizontalAlignment(JLabel.RIGHT);
                    onlineGameLabel.setFont(font);
                    onlineGameLabel.setForeground(Color.RED);
                    layeredPane.add(onlineGameLabel, Integer.valueOf(100));
                }
            }

            BufferedImage backButtonPic = ImageIO.read(new File("pictures/buttons/backButton.png"));
            JLabel backButton = new JLabel(new ImageIcon(backButtonPic));
            backButton.setBounds(20, 20, backButtonPic.getWidth(), backButtonPic.getHeight());

            backButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    client.getContentPane().removeAll();
                    client.getContentPane().add(new MenuView(client));
                    client.getContentPane().revalidate();
					client.disconnectFromServer();
                }
            });
            layeredPane.add(backButton, Integer.valueOf(100));

            add(layeredPane);
            revalidate();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
