package SB.view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class AllPanels extends JPanel {
    public static final int START = 0;
    public static final int SETTINGS = 1;
    public static final int LOBBY = 2;
    private Image backgroundImage;
    public AllPanels(int value) throws IOException {
        if(value == 0) {
            backgroundImage = ImageIO.read(new File("pictures/backgrounds/startBackground.png"));
            setBounds(new Rectangle(backgroundImage.getWidth(this), backgroundImage.getHeight(this)));
            repaint();
        }
        else if(value == 1) {
            backgroundImage = ImageIO.read(new File("pictures/backgrounds/background1.png"));
            setBounds(new Rectangle(backgroundImage.getWidth(this), backgroundImage.getHeight(this)));
            repaint();
        }
        else if(value == 2) {
            backgroundImage = ImageIO.read(new File("pictures/backgrounds/backgroundLobby.jpg"));
            setBounds(new Rectangle(backgroundImage.getWidth(this), backgroundImage.getHeight(this)));
            repaint();
        }

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, this);
    }
}