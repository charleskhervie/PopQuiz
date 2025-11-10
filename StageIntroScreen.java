
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionListener;

public class StageIntroScreen implements Screen {
    private JPanel panel;

    public StageIntroScreen(int grade, ActionListener onStartGrade) {
        // Stop any previous audio and play stage intro music
        AudioPlayer.stopAll();
        AudioPlayer.playSound("./audio/StageIntro.wav", true);
        
        String imagePath = "./assets/grade-" + grade + ".jpg";
        Image bgImage = new ImageIcon(imagePath).getImage();

        panel = new JPanel(null) { 
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null) {
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        panel.setBackground(Color.WHITE);

        RoundedButton startBtn = new RoundedButton("START GRADE " + grade, 36);
        startBtn.setBackground(new Color(255, 250, 240)); 
        startBtn.setForeground(new Color(0, 0, 139)); 
        startBtn.addActionListener(e -> {
            AudioPlayer.playSound("./audio/Click.wav", false);
            onStartGrade.actionPerformed(e);
        });
        startBtn.setBounds(650, 530, 220, 60);
        panel.add(startBtn);


        }

    @Override
    public JPanel getPanel() {
        return panel;
    }
}
