// BackgroundPanel.java
import javax.swing.*;
import java.awt.*;

public class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(String imagePath) {
        backgroundImage = new ImageIcon(imagePath).getImage();
        setPreferredSize(new Dimension(1920, 1080));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int imgWidth = backgroundImage.getWidth(this);
        int imgHeight = backgroundImage.getHeight(this);

        // Center the image without scaling
        int x = (panelWidth - imgWidth) / 2;
        int y = (panelHeight - imgHeight) / 2;

        g.drawImage(backgroundImage, x, y, this);
    }
}
