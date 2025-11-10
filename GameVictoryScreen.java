
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionListener;

public class GameVictoryScreen implements Screen {
    private JPanel panel;

    public GameVictoryScreen(ActionListener onReturnToTitle) {
        Image bgImage = new ImageIcon("./assets/victory-screen.jpg").getImage();

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

        RoundedButton backBtn = new RoundedButton("Return to Title", 40);
        backBtn.addActionListener(onReturnToTitle);

        backBtn.setFont(new Font("Impact", Font.BOLD, 22));

        backBtn.setBounds(600, 650, 250, 60);
        panel.add(backBtn);
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }
}
