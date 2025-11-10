
// InstructionsScreen.java
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionListener;

public class InstructionsScreen implements Screen {
    private JPanel panel;

    public InstructionsScreen(ActionListener onBack) {
        panel = new JPanel() {
        Image bgImage = new ImageIcon("./assets/instructions.jpg").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(null); 

        JButton backButton = new JButton("Back") {
            private final int arc = 25;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(new Color(220, 220, 220));
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(240, 240, 240));
                } else {
                    g2.setColor(Color.WHITE);
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);

                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);

                g2.dispose();
                super.paintComponent(g);
            }

           
        };

        backButton.setBounds(50, 50, 180, 70);
        backButton.setFont(new Font("Arial", Font.BOLD, 20));
        backButton.setForeground(Color.BLACK);
        backButton.setFocusPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        backButton.addActionListener(onBack);
        panel.add(backButton);
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }
}
