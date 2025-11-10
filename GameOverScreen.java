
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionListener;

public class GameOverScreen implements Screen {
    private JPanel panel;

    public GameOverScreen(ActionListener onReturnToTitle) {
        // Load background image
        Image bgImage = new ImageIcon("./assets/game-over.jpg").getImage();

        panel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null) {
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        panel.setOpaque(true);

        // Rounded button (custom style)
        RoundedButton backBtn = new RoundedButton("Return to Title", 40);
        backBtn.addActionListener(onReturnToTitle);

        // 🔹 Manually position the button (adjust x, y to your liking)
        backBtn.setBounds(600, 650, 250, 60);

        panel.add(backBtn);
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }
}
