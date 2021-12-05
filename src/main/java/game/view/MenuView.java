package game.view;

import game.Client;
import game.model.GameSettings;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MenuView extends JPanel {
    public int selectedButton = 0;

    private JLayeredPane layeredPane;
    private JLabel botGamePic;
    private JLabel botGamePicSelected;
    private JLabel onlineGamePic;
    private JLabel onlineGamePicSelected;

    private static final int BOT_GAME = 0;
    private static final int ONLINE_GAME = 1;
    public MenuView(final Client client) {
        try {
            selectedButton = BOT_GAME;
            setLayout(new FlowLayout());

            AllPanels picture = new AllPanels(AllPanels.START);
            layeredPane = new JLayeredPane();
            layeredPane.setPreferredSize(new Dimension(picture.getWidth(), picture.getHeight()));
            layeredPane.add(picture, new Integer(10));
            BufferedImage pic = ImageIO.read(new File("pictures/buttons/button0.png"));
            BufferedImage picSelected = ImageIO.read(new File("pictures/buttons/button1.png"));

            setPreferredSize(new Dimension(picture.getWidth(), picture.getHeight()));

            botGamePic = new JLabel(new ImageIcon(pic));
            onlineGamePic = new JLabel(new ImageIcon(pic));
            botGamePic.setBounds(50, 200, pic.getWidth(), pic.getHeight());
            onlineGamePic.setBounds(50, 220 + pic.getHeight(), pic.getWidth(), pic.getHeight());

            botGamePicSelected = new JLabel(new ImageIcon(picSelected));
            onlineGamePicSelected = new JLabel(new ImageIcon(picSelected));
            botGamePicSelected.setBounds(50, 200, picSelected.getWidth(), picSelected.getHeight());
            onlineGamePicSelected.setBounds(50, 220 + picSelected.getHeight(), picSelected.getWidth(), picSelected.getHeight());

            JLabel botGameLabel = new JLabel("PLAY OFFLINE");
            JLabel onlineGameLabel = new JLabel("PLAY ONLINE");
            botGameLabel.setBounds(50, 200, pic.getWidth(), pic.getHeight());
            onlineGameLabel.setBounds(50, 220 + pic.getHeight(), pic.getWidth(), pic.getHeight());

            botGameLabel.setHorizontalAlignment(JLabel.CENTER);
            onlineGameLabel.setHorizontalAlignment(JLabel.CENTER);
            Font font = new Font("Sherif", Font.BOLD, 30);

            botGameLabel.setFont(font);
            onlineGameLabel.setFont(font);
            botGameLabel.setForeground(Color.BLACK);
            onlineGameLabel.setForeground(Color.BLACK);

            layeredPane.add(botGameLabel, new Integer(100));
            layeredPane.add(onlineGameLabel, new Integer(100));

            layeredPane.add(botGamePicSelected, new Integer(50));
            layeredPane.add(onlineGamePic, new Integer(50));

            BufferedImage exitButton = ImageIO.read(new File("pictures/buttons/off.png"));
            JLabel exit = new JLabel(new ImageIcon(exitButton));
            exit.setBounds(20, 20, exitButton.getWidth(), exitButton.getHeight());

            exit.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    System.exit(0);
                }
            });
            layeredPane.add(exit, Integer.valueOf(100));

            add(layeredPane);

            botGameLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    setSelected(BOT_GAME);
                }
            });

            onlineGameLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    setSelected(ONLINE_GAME);
                }
            });

            KeyStroke upKeyStrokePressed1 = KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, true);
            Action upAction1 = new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    int nowMenu = selectedButton;
                    nowMenu--;
                    if (nowMenu == -1) {
                        nowMenu = 1;
                    }
                    setSelected(nowMenu);
                }
            };

            getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                    upKeyStrokePressed1, "UP1_PRESSED");
            getActionMap().put("UP1_PRESSED", upAction1);

            KeyStroke downKeyStrokePressed1 = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true);
            Action downAction1 = new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    int nowMenu = selectedButton;
                    nowMenu++;
                    if (nowMenu == 2) {
                        nowMenu = 0;
                    }
                    setSelected(nowMenu);
                }
            };

            getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                    downKeyStrokePressed1, "DOWN1_PRESSED");
            getActionMap().put("DOWN1_PRESSED", downAction1);

            KeyStroke enterKeyStrokePressed1 = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true);
            Action enterAction1 = new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    client.getContentPane().removeAll();
                    if (selectedButton == GameSettings.BOT_GAME) {
                        GameSettingsView settingsView = new GameSettingsView(client, GameSettings.BOT_GAME);
                        client.getContentPane().add(settingsView);
                    } else if (selectedButton == GameSettings.ONLINE_GAME) {
                        client.connectToServer();
                        client.getContentPane().add(new ChooseModeView(client));
                    }
                    client.getContentPane().repaint();
                    client.revalidate();
                }
            };
            getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                    enterKeyStrokePressed1, "ENTER1_PRESSED");
            getActionMap().put("ENTER1_PRESSED", enterAction1);
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    }
    public void setSelected(int value) {
        if(selectedButton != value) {
            if (value == BOT_GAME) {
                layeredPane.remove(onlineGamePicSelected);
                layeredPane.remove(botGamePic);
                layeredPane.add(onlineGamePic, new Integer(50));
                layeredPane.add(botGamePicSelected, new Integer(50));
            } else if (value == ONLINE_GAME) {
                layeredPane.remove(botGamePicSelected);
                layeredPane.remove(onlineGamePic);
                layeredPane.add(botGamePic, new Integer(50));
                layeredPane.add(onlineGamePicSelected, new Integer(50));
            }
        }
        selectedButton = value;
    }
}
