package game.view;

import game.Client;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class LobbyView extends JPanel {

    private JLayeredPane layeredPane;
    private Client client;

    public LobbyView(Client client) {
        this.client = client;
        lobbyView(client);
    }

    public void lobbyView(Client client) {
        removeAll();
        layeredPane = new JLayeredPane();
        try {
            AllPanels picture = new AllPanels(AllPanels.LOBBY);
            layeredPane.setPreferredSize(new Dimension(picture.getWidth(), picture.getHeight()));
            setPreferredSize(new Dimension(picture.getWidth(), picture.getHeight()));
            layeredPane.add(picture, new Integer(10));

            BufferedImage skipBoPic = ImageIO.read(new File("pictures/texts/skipbo_text.png"));
            JLabel skipboLabel = new JLabel(new ImageIcon(skipBoPic));
            skipboLabel.setBounds(picture.getWidth() - skipBoPic.getWidth() + 100, 0, skipBoPic.getWidth(), skipBoPic.getHeight());
            JLabel label = new JLabel("Wait for other players");
            label.setBounds(50,0, 900, 200);
            label.setFont(new Font("Sherif", Font.BOLD, 50));
            label.setForeground(Color.black);

            layeredPane.add(skipboLabel, new Integer(50));
            layeredPane.add(label, new Integer(100));
            add(layeredPane);

            client.getContentPane().removeAll();
            client.getContentPane().add(this);
            revalidate();
            client.repaint();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
