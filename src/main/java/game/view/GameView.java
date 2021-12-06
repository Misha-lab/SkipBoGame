package game.view;

import game.Client;
import game.model.Game;
import game.model.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GameView extends JPanel {
    private final Client client;
    private JLayeredPane layeredPane;

    public GameView(Client client) {
        this.client = client;
        gameView();
    }

    public void gameView() {
        removeAll();
        layeredPane = new JLayeredPane();
        int m = 0;
        for (; m < client.game.getPlayers().size(); m++) {
            if(client.getId() == client.game.getPlayers().get(m).getId()) {
                break;
            }
        }
        final Player current = client.game.getPlayer(m);
        try {
            final BufferedImage backPic = ImageIO.read(new File("pictures/backgrounds/backgroundGame.png"));
            JLabel backLabel = new JLabel(new ImageIcon(backPic));
            layeredPane.setPreferredSize(new Dimension(backPic.getWidth(), backPic.getHeight()));
            setPreferredSize(new Dimension(backPic.getWidth(), backPic.getHeight()));
            backLabel.setBounds(0, 0, backPic.getWidth(), backPic.getHeight());
            layeredPane.add(backLabel, Integer.valueOf(10));

            JLabel fieldLabel = new JLabel("FIELD:");
            fieldLabel.setBounds(450, backPic.getHeight() / 2 - 180, 300, 100);
            fieldLabel.setFont(new Font("Sherif", Font.BOLD, 40));
            fieldLabel.setForeground(Color.RED);
            layeredPane.add(fieldLabel, Integer.valueOf(20));

            final BufferedImage shapePic = ImageIO.read(new File("pictures/cards/0.png"));
            final JLabel[] field = new JLabel[4];
            for (int i = 0; i < 4; i++) {
                String format = ".png";
                if (client.game.getBoard().isSkipBoAt(i) && client.game.getBoard().getValue(i) != 0) {
                    format = "+.png";
                }
                BufferedImage fieldPic = ImageIO.read(new File("pictures/cards/" + client.game.getBoard().getValue(i) + format));
                field[i] = new JLabel(new ImageIcon(fieldPic));
                field[i].setBounds(450 + (fieldPic.getWidth() + 20) * i, backPic.getHeight() / 2 - 100, fieldPic.getWidth(), fieldPic.getHeight());
                layeredPane.add(field[i], Integer.valueOf(20));
            }

            JLabel storageLabel = new JLabel("YOUR STORAGE: ");
            storageLabel.setBounds(700, backPic.getHeight() - shapePic.getHeight() - 180, 500, 100);
            storageLabel.setFont(new Font("Sherif", Font.BOLD, 20));
            storageLabel.setForeground(Color.BLACK);
            layeredPane.add(storageLabel, Integer.valueOf(80));

            final JLabel[] storagePic = new JLabel[4];
            for (int i = 0; i < 4; i++) {
                final int ind = i;
                final BufferedImage cardPic = ImageIO.read(new File("pictures/cards/" + current.getStorage().get(i).lastElement() + ".png"));
                storagePic[i] = new JLabel(new ImageIcon(cardPic));
                storagePic[i].setBounds(700 + (cardPic.getWidth() + 20) * i, backPic.getHeight() - cardPic.getHeight() - 110, cardPic.getWidth(), cardPic.getHeight());

                storagePic[i].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        int size = current.getStorage().get(ind).size();
                        if (size > 1) {
                            final JPanel cards = new JPanel();
                            cards.setBounds(600, backPic.getHeight() - shapePic.getHeight() - 200, 700, 350);
                            int k = 0;
                            for (int j = size - 1; j >= 0; j--) {
                                if (current.getStorage().get(ind).get(j) != 0) {
                                    if (j % 5 == 0) {
                                        k++;
                                    }
                                    try {
                                        BufferedImage cardPic = ImageIO.read(new File("pictures/cards/" + current.getStorage().get(ind).get(j) + ".png"));
                                        JLabel card = new JLabel(new ImageIcon(cardPic));
                                        card.setBounds(cards.getX() + (cardPic.getWidth() + 20) * ((size - 1 - j) % 5), cards.getY() + (cardPic.getHeight() + 10) * k, cardPic.getWidth(), cardPic.getHeight());
                                        cards.add(card);
                                    } catch (IOException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            }

                            cards.addMouseListener(new MouseAdapter() {
                                @Override
                                public void mouseClicked(MouseEvent e) {
                                    layeredPane.remove(cards);
                                    repaint();
                                }
                            });

                            layeredPane.add(cards, Integer.valueOf(200));
                            repaint();
                        }
                    }
                });

                MouseInputAdapter toField = new MouseInputAdapter() {
                    private int startX = storagePic[ind].getX();
                    private int startY = storagePic[ind].getY();

                    public void mouseDragged(MouseEvent e) {
                        if (client.game.getXMoveID() == client.getId()) {
                            int x = e.getX() + storagePic[ind].getX();
                            int y = e.getY() + storagePic[ind].getY();
                            storagePic[ind].setBounds(x, y, cardPic.getWidth(), cardPic.getHeight());
                        }
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        boolean isMade;
                        for (int j = 0; j < 4; j++) {
                            boolean isContains = storagePic[ind].getX() >= field[j].getX()
                                    && storagePic[ind].getX() <= field[j].getX() + field[j].getWidth()
                                    && storagePic[ind].getY() >= field[j].getY()
                                    && storagePic[ind].getY() <= field[j].getY() + field[j].getHeight();

                            if (isContains) {
                                storagePic[ind].setBounds(field[j].getX(), field[j].getY(), cardPic.getWidth(), cardPic.getHeight());
								client.getWriter().println("@makeMove 2" + ind + "" + j + " 10");
                                break;
                            }
                        }
                    }
                };

                storagePic[i].addMouseListener(toField);
                storagePic[i].addMouseMotionListener(toField);

                layeredPane.add(storagePic[i], Integer.valueOf(80));

            }

            JLabel currentLabel = new JLabel("YOUR CURRENT CARDS: ");
            currentLabel.setBounds(100, backPic.getHeight() - shapePic.getHeight() - 180, 500, 100);
            currentLabel.setFont(new Font("Sherif", Font.BOLD, 20));
            currentLabel.setForeground(Color.BLUE);
            layeredPane.add(currentLabel, Integer.valueOf(100));

            for (int i = 0; i < current.getCurrentCards().size(); i++) {
                final int ind = i;
                final BufferedImage cardPic = ImageIO.read(new File("pictures/cards/" + current.getCurrentCards().get(i) + ".png"));
                final JLabel cardLabel = new JLabel(new ImageIcon(cardPic));
                cardLabel.setBounds(100 + (cardPic.getWidth() - 30) * i, backPic.getHeight() - cardPic.getHeight() - 110, cardPic.getWidth(), cardPic.getHeight());
                layeredPane.add(cardLabel, Integer.valueOf(100 + i * 10));

                MouseInputAdapter toField = new MouseInputAdapter() {
                    private int startX = cardLabel.getX();
                    private int startY = cardLabel.getY();

                    public void mouseDragged(MouseEvent e) {
                        if (client.game.getXMoveID() == client.getId()) {
                            int x = e.getX() + cardLabel.getX();
                            int y = e.getY() + cardLabel.getY();
                            cardLabel.setBounds(x, y, cardPic.getWidth(), cardPic.getHeight());
                        }
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        boolean isMade;
                        for (int j = 0; j < 4; j++) {
                            boolean isContainsField = cardLabel.getX() >= field[j].getX() && cardLabel.getX() <= field[j].getX() + field[j].getWidth() && cardLabel.getY() >= field[j].getY() && cardLabel.getY() <= field[j].getY() + field[j].getHeight();
                            if (isContainsField) {
                                cardLabel.setBounds(field[j].getX(), field[j].getY(), cardPic.getWidth(), cardPic.getHeight());
								client.getWriter().println("@makeMove 3" + ind + "" + j + " 10");
                                break;
                            }
                            boolean isContainsStorage = cardLabel.getX() >= storagePic[j].getX() && cardLabel.getX() <= storagePic[j].getX() + storagePic[j].getWidth() && cardLabel.getY() >= storagePic[j].getY() && cardLabel.getY() <= storagePic[j].getY() + storagePic[j].getHeight();
                            if (isContainsStorage) {
                                cardLabel.setBounds(storagePic[j].getX(), storagePic[j].getY(), cardPic.getWidth(), cardPic.getHeight());
								client.getWriter().println("@makeMove " + ind + "" + j + " 11");
                                break;
                            }
                        }
                    }
                };
                cardLabel.addMouseListener(toField);
                cardLabel.addMouseMotionListener(toField);
            }

            JLabel mainLabel = new JLabel("MAIN:");
            mainLabel.setBounds(1100, backPic.getHeight() / 2 - 180, 500, 100);
            mainLabel.setFont(new Font("Sherif", Font.BOLD, 30));
            mainLabel.setForeground(Color.DARK_GRAY);
            layeredPane.add(mainLabel, Integer.valueOf(80));

            JLabel mainCountLabel = new JLabel(current.getMainCards().size() + " remaining");
            mainCountLabel.setBounds(1050, backPic.getHeight() / 2 + 20, 500, 100);
            mainCountLabel.setFont(new Font("Sherif", Font.BOLD, 30));
            mainCountLabel.setForeground(Color.YELLOW);
            layeredPane.add(mainCountLabel, Integer.valueOf(80));

            final BufferedImage cardPic = ImageIO.read(new File("pictures/cards/" + current.getMainCards().get(0) + ".png"));
            final JLabel cardLabel = new JLabel(new ImageIcon(cardPic));
            cardLabel.setBounds(1100, backPic.getHeight() / 2 - 100, cardPic.getWidth(), cardPic.getHeight());

            MouseInputAdapter toField = new MouseInputAdapter() {
                private int startX = cardLabel.getX();
                private int startY = cardLabel.getY();

                public void mouseDragged(MouseEvent e) {
                    if (client.game.getXMoveID() == client.getId()) {
                        int x = e.getX() + cardLabel.getX();
                        int y = e.getY() + cardLabel.getY();
                        cardLabel.setBounds(x, y, cardPic.getWidth(), cardPic.getHeight());
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    boolean isMade;
                    for (int j = 0; j < 4; j++) {
                        boolean isContains = false;
                        if (cardLabel.getX() >= field[j].getX() && cardLabel.getX() <= field[j].getX() + field[j].getWidth() && cardLabel.getY() >= field[j].getY() && cardLabel.getY() <= field[j].getY() + field[j].getHeight()) {
                            isContains = true;
                        }
                        if (isContains) {
                            cardLabel.setBounds(field[j].getX(), field[j].getY(), cardPic.getWidth(), cardPic.getHeight());
							client.getWriter().println("@makeMove 1*" + j + " 10");
                            break;
                        }
                    }
                }
            };
            cardLabel.addMouseListener(toField);
            cardLabel.addMouseMotionListener(toField);

            layeredPane.add(cardLabel, Integer.valueOf(80));


            ArrayList<Player> players = client.game.getPlayers();
            int k = 0;
            int sub = 0;
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).getId() == client.getId()) {
                    sub++;
                    continue;
                }
                if ((i - sub) % 5 == 0 && (i - sub) != 0) {
                    k++;
                }
                BufferedImage mainCardPic = ImageIO.read(new File("pictures/cards/" + players.get(i).getMainCards().get(0) + ".png"));
                JLabel card = new JLabel(new ImageIcon(mainCardPic));
                card.setBounds(50 + (2 * mainCardPic.getWidth()) * ((i - sub) % 5), 50 + (mainCardPic.getHeight() + 20) * k, mainCardPic.getWidth(), mainCardPic.getHeight());

                JLabel playerNameLabel = new JLabel("Player " + players.get(i).getId());
                playerNameLabel.setBounds(160 + (2 * mainCardPic.getWidth()) * ((i - sub) % 5), 60 + (mainCardPic.getHeight() + 20) * k, 500, 100);
                playerNameLabel.setFont(new Font("Sherif", Font.BOLD, 25));
                playerNameLabel.setForeground(Color.BLACK);

                JLabel mainCardsCountLabel = new JLabel(players.get(i).getMainCards().size() + " remaining");
                mainCardsCountLabel.setBounds(160 + (2 * mainCardPic.getWidth()) * ((i - sub) % 5), 90 + (mainCardPic.getHeight() + 20) * k, 500, 100);
                mainCardsCountLabel.setFont(new Font("Sherif", Font.BOLD, 15));
                mainCardsCountLabel.setForeground(Color.BLUE);
                layeredPane.add(card, Integer.valueOf(100));
                layeredPane.add(playerNameLabel, Integer.valueOf(105));
                layeredPane.add(mainCardsCountLabel, Integer.valueOf(110));
            }

            if (client.game.getXMoveID() == client.getId()) {
                JLabel yourMoveLabel = new JLabel("YOUR MOVE!");
                yourMoveLabel.setBounds(50, 250, 350, 100);
                yourMoveLabel.setFont(new Font("Sherif", Font.BOLD, 40));
                yourMoveLabel.setForeground(Color.RED);
                layeredPane.add(yourMoveLabel, Integer.valueOf(50));
            } else {
                JLabel notYourMoveLabel = new JLabel("NOT YOUR MOVE!");
                notYourMoveLabel.setBounds(50, 250, 350, 100);
                notYourMoveLabel.setFont(new Font("Sherif", Font.BOLD, 35));
                notYourMoveLabel.setForeground(Color.RED);
                layeredPane.add(notYourMoveLabel, Integer.valueOf(50));
            }

            add(layeredPane);
            revalidate();


        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private BufferedImage resize(BufferedImage img, int newW, int newH) {
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage dimg = new BufferedImage(newW, newH, img.getType());
        Graphics2D g = dimg.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);
        g.dispose();
        return dimg;
    }
}