
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;


public class TitleScreen implements Screen {
    private JPanel panel;

    public TitleScreen(ActionListener onStart, ActionListener onInstructions, ActionListener onExit) {
        
        // Stop any previous audio and play title screen music
        AudioPlayer.stopAll();
        AudioPlayer.playSound("./audio/TitleScreen.wav", true);
        
        ImageIcon background = new ImageIcon("./assets/title-page.jpg");
        Image backgroundImage = background.getImage();

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };

        panel.setLayout(null);

        StyledPillButton startBtn = new StyledPillButton("START");
        StyledPillButton instructionsBtn = new StyledPillButton("INSTRUCTIONS");
        StyledPillButton exitBtn = new StyledPillButton("EXIT");

        startBtn.addActionListener(e -> {
            AudioPlayer.playSound("./audio/Click.wav", false);
            onStart.actionPerformed(e);
        });
        instructionsBtn.addActionListener(e -> {
            AudioPlayer.playSound("./audio/Click.wav", false);
            onInstructions.actionPerformed(e);
        });
        exitBtn.addActionListener(e -> {
            AudioPlayer.playSound("./audio/Click.wav", false);
            onExit.actionPerformed(e);
        });

        
        int buttonWidth = 350;
        int buttonHeight = 70;
  
        startBtn.setBounds(580, 520, buttonWidth, buttonHeight);
        instructionsBtn.setBounds(580, 600, buttonWidth, buttonHeight);
        exitBtn.setBounds(580, 680, buttonWidth, buttonHeight);

        panel.add(startBtn);
        panel.add(instructionsBtn);
        panel.add(exitBtn);
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }
}

