package SB.view;

import SB.Client;
import SB.model.Game;
import SB.model.GameSettings;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ChooseModeView extends JPanel {
    private JLayeredPane layeredPane;
    private int selectedButton;

    private JLabel createGamePic;
    private JLabel createGamePicSelected;
    private JLabel joinGamePicSelected;
    private JLabel joinGamePic;

    public ChooseModeView(final Client client) {
        try {
            selectedButton = GameSettings.CREATE;
            AllPanels picture = new AllPanels(AllPanels.SETTINGS);
            layeredPane = new JLayeredPane();
            layeredPane.setPreferredSize(new Dimension(picture.getWidth(), picture.getHeight()));
            layeredPane.add(picture, Integer.valueOf(10));
            BufferedImage pic = ImageIO.read(new File("pictures/buttons/button0.png"));
            BufferedImage picSelected = ImageIO.read(new File("pictures/buttons/button1.png"));

            setPreferredSize(new Dimension(picture.getWidth(), picture.getHeight()));

            createGamePic = new JLabel(new ImageIcon(pic));
            joinGamePic = new JLabel(new ImageIcon(pic));
            createGamePic.setBounds(50, 200, pic.getWidth(), pic.getHeight());
            joinGamePic.setBounds(50, 220 + pic.getHeight(), pic.getWidth(), pic.getHeight());

            createGamePicSelected = new JLabel(new ImageIcon(picSelected));
            joinGamePicSelected = new JLabel(new ImageIcon(picSelected));
            createGamePicSelected.setBounds(50, 200, picSelected.getWidth(), picSelected.getHeight());
            joinGamePicSelected.setBounds(50, 220 + picSelected.getHeight(), picSelected.getWidth(), picSelected.getHeight());

            JLabel createGameLabel = new JLabel("CREATE NEW GAME");
            JLabel joinGameLabel = new JLabel("JOIN TO EXISTING GAME");
            createGameLabel.setBounds(50, 200, pic.getWidth(), pic.getHeight());
            joinGameLabel.setBounds(50, 220 + pic.getHeight(), pic.getWidth(), pic.getHeight());

            createGameLabel.setHorizontalAlignment(JLabel.CENTER);
            joinGameLabel.setHorizontalAlignment(JLabel.CENTER);
            Font font = new Font("Sherif", Font.BOLD, 20);

            createGameLabel.setFont(font);
            joinGameLabel.setFont(font);
            createGameLabel.setForeground(Color.BLACK);
            joinGameLabel.setForeground(Color.BLACK);

            layeredPane.add(createGameLabel, Integer.valueOf(100));
            layeredPane.add(joinGameLabel, Integer.valueOf(100));

            layeredPane.add(createGamePicSelected, Integer.valueOf(50));
            layeredPane.add(joinGamePic, Integer.valueOf(50));

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


            createGameLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    setSelected(GameSettings.CREATE);
                }
            });

            joinGameLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    setSelected(GameSettings.JOIN);
                }
            });

            KeyStroke upKeyStrokePressed = KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, true);
            Action upAction = new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    int nowMenu = selectedButton;
                    nowMenu--;
                    if (nowMenu == 1) {
                        nowMenu = 3;
                    }
                    setSelected(nowMenu);
                }
            };

            getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                    upKeyStrokePressed, "UP_PRESSED");
            getActionMap().put("UP_PRESSED", upAction);

            KeyStroke downKeyStrokePressed = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true);
            Action downAction = new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    int nowMenu = selectedButton;
                    nowMenu++;
                    if (nowMenu == 4) {
                        nowMenu = 2;
                    }
                    setSelected(nowMenu);
                }
            };

            getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                    downKeyStrokePressed, "DOWN_PRESSED");
            getActionMap().put("DOWN_PRESSED", downAction);

            KeyStroke enterKeyStrokePressed1 = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true);
            Action enterAction1 = new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    client.getContentPane().removeAll();
                    if (selectedButton == GameSettings.CREATE) {
                        GameSettingsView settingsView = new GameSettingsView(client, GameSettings.ONLINE_GAME);
                        client.getContentPane().add(settingsView);
                    } else {
                        client.getContentPane().add(new GameListView(client));
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
            if (value == GameSettings.CREATE) {
                layeredPane.remove(joinGamePicSelected);
                layeredPane.remove(createGamePic);
                layeredPane.add(joinGamePic, Integer.valueOf(50));
                layeredPane.add(createGamePicSelected, Integer.valueOf(50));
            } else if (value == GameSettings.JOIN) {
                layeredPane.remove(createGamePicSelected);
                layeredPane.remove(joinGamePic);
                layeredPane.add(createGamePic, Integer.valueOf(50));
                layeredPane.add(joinGamePicSelected, Integer.valueOf(50));
            }
        }
        selectedButton = value;
    }

    public int getSelectedButton() {
        return selectedButton;
    }
}
