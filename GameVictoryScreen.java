
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionListener;

public class GameVictoryScreen implements Screen {
    private JPanel panel;

    public GameVictoryScreen(ActionListener onReturnToTitle) {
        // Stop any previous audio and play victory music
        AudioPlayer.stopAll();
        AudioPlayer.playSound("./audio/GameVictory.wav", true);
        
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
        backBtn.addActionListener(e -> {
            AudioPlayer.playSound("./audio/Click.wav", false);
            onReturnToTitle.actionPerformed(e);
        });

        backBtn.setFont(new Font("Impact", Font.BOLD, 22));

        backBtn.setBounds(600, 650, 250, 60);
        panel.add(backBtn);
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }
}
