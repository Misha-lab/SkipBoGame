package game.view;

import game.Client;
import game.Server;
import game.model.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GameSettingsView extends JPanel {
    public Font font = new Font("Sherif", Font.BOLD, 50);

    public static class CardsCountListener extends MouseAdapter {
        private final int number;
        private final int cardsCount;
        JLabel cardsCountSelected;

        public CardsCountListener(JLabel cardsCountSelected, int i, int cardsCount) {
            this.cardsCount = cardsCount;
            this.cardsCountSelected = cardsCountSelected;
            number = i;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            cardsCountSelected.setBounds(120, 110 + (cardsCountSelected.getHeight() + 45) * number, cardsCountSelected.getWidth(), cardsCountSelected.getHeight());
        }
    }

    private GameSettings settings;
    private int gameType;
    int nowCardsCount = 7;

    public GameSettingsView(final Client client, final int gameType) {
        this.gameType = gameType;
        final int[] cardsCountValues = {7, 10, 15, 20, 30};
        try {
            AllPanels picture = new AllPanels(AllPanels.SETTINGS);
            JLayeredPane layeredPane = new JLayeredPane();
            layeredPane.setPreferredSize(new Dimension(picture.getWidth(), picture.getHeight()));
            layeredPane.add(picture, Integer.valueOf(10));
            BufferedImage pic = ImageIO.read(new File("pictures/buttons/cardsCount.png"));
            setPreferredSize(new Dimension(picture.getWidth(), picture.getHeight()));

            BufferedImage picSelected = ImageIO.read(new File("pictures/buttons/selectedCardsCount.png"));
            final JLabel cardsCountSelected = new JLabel(new ImageIcon(picSelected));
            cardsCountSelected.setBounds(120, 110, picSelected.getWidth(), picSelected.getHeight());
            layeredPane.add(cardsCountSelected, Integer.valueOf(120));

            BufferedImage playPicture = ImageIO.read(new File("pictures/buttons/play1.png"));
            JLabel playPic = new JLabel(new ImageIcon(playPicture));
            playPic.setBounds(400,530, playPicture.getWidth(), playPicture.getHeight());
            layeredPane.add(playPic, Integer.valueOf(150));

            JLabel text = new JLabel("SELECT COUNT OF CARDS:");
            text.setBounds(100, 30, 300, 80);
            text.setFont(new Font("Sherif", Font.BOLD, 20));
            layeredPane.add(text, Integer.valueOf(50));

            JLabel[] cardsCountPics = new JLabel[5];
            JLabel[] cardsCountLabels = new JLabel[5];
            for (int i = 0; i < 5; i++) {
                cardsCountPics[i] = new JLabel(new ImageIcon(pic));
                cardsCountPics[i].setBounds(100, 100 + (pic.getHeight() + 20) * i, pic.getWidth(), pic.getHeight());

                cardsCountLabels[i] = new JLabel(cardsCountValues[i] + "");
                cardsCountLabels[i].setBounds(100, 100 + (pic.getHeight() + 20) * i, pic.getWidth(), pic.getHeight());
                cardsCountLabels[i].setForeground(Color.BLACK);
                cardsCountLabels[i].setFont(font);
                cardsCountLabels[i].setHorizontalAlignment(JLabel.CENTER);

                cardsCountLabels[i].addMouseListener(new CardsCountListener(cardsCountSelected, i, nowCardsCount = cardsCountValues[i]));

                layeredPane.add(cardsCountPics[i], Integer.valueOf(50));
                layeredPane.add(cardsCountLabels[i], Integer.valueOf(100));

            }

            JLabel text1 = new JLabel("SELECT COUNT OF PLAYERS:");
            text1.setBounds(400, 30, 300, 80);
            text1.setFont(new Font("Sherif", Font.BOLD, 20));
            layeredPane.add(text1, Integer.valueOf(100));

            final JSlider slider = new JSlider(2, 6, 2);
            slider.setBounds(400, 100, 100, 400);
            slider.setMinorTickSpacing(1);
            slider.setBackground(new Color(0, 153, 73));
            slider.setMajorTickSpacing(1);
            slider.setPaintLabels(true);
            slider.setFont(font);
            slider.setOrientation(SwingConstants.VERTICAL);

            layeredPane.add(slider, Integer.valueOf(100));

            BufferedImage backButtonPic = ImageIO.read(new File("pictures/buttons/backButton.png"));
            JLabel backButton = new JLabel(new ImageIcon(backButtonPic));
            backButton.setBounds(20, 20, backButtonPic.getWidth(), backButtonPic.getHeight());

            backButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    client.getContentPane().removeAll();
                    if(gameType == GameSettings.BOT_GAME) {
                        client.getContentPane().add(new MenuView(client));
                    }
                    else {
                        client.getContentPane().add(new ChooseModeView(client));
                    }
                    client.getContentPane().revalidate();
                }
            });
            layeredPane.add(backButton, Integer.valueOf(100));

            add(layeredPane);

            playPic.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if(gameType == GameSettings.ONLINE_GAME) {
                        try {
                            int i = (cardsCountSelected.getY() - 110)/(cardsCountSelected.getHeight() + 45);
                            nowCardsCount = cardsCountValues[i];

                            client.getWriter().println("@createGame " + gameType + " " + nowCardsCount + " " + slider.getValue());
                            settings = new GameSettings(gameType, nowCardsCount, slider.getValue());
                            GameThread newGame = new GameThread(settings, Server.gamesCount());
                            newGame.start();
                            Server.addGame(newGame);
                            client.setPlayer(new Player(client.getId()));
                        }
                        catch(IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                    else if (gameType == GameSettings.BOT_GAME) {
                        int i = (cardsCountSelected.getY() - 110)/(cardsCountSelected.getHeight() + 45);
                        nowCardsCount = cardsCountValues[i];
                        settings = new GameSettings(gameType, nowCardsCount, slider.getValue());
                        Game game = new Game(settings);
                        game.setGameStatus(Game.IN_GAME);


                        ArrayList<Player> temp = new ArrayList<>();
                        client.id = 0;
                        for (int j = 0; j < settings.getPlayersCount(); j++) {
                            Player bot = new Player(j);
                            temp.add(bot);
                            bot.generateCards(settings.getMainCardsCount());
                            System.out.println(bot.getCurrentCards());
                        }
                        game.setPlayers(temp);
                        client.game = game;

                        client.getContentPane().removeAll();
                        BotGameView botGameView = new BotGameView(client);
                        client.getContentPane().add(botGameView);
                        client.getContentPane().repaint();
                        client.revalidate();
                    }
                    System.out.println("PLAY!");
                }
            });

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        revalidate();
    }
}