
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;


public class TitleScreen implements Screen {
    private JPanel panel;

    public TitleScreen(ActionListener onStart, ActionListener onInstructions, ActionListener onExit) {
        
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

        startBtn.addActionListener(onStart);
        instructionsBtn.addActionListener(onInstructions);
        exitBtn.addActionListener(onExit);

        
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

