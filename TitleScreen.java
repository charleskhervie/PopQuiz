// TitleScreen.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class TitleScreen implements Screen {
    private JPanel panel;

    public TitleScreen(ActionListener onStart, ActionListener onInstructions, ActionListener onExit) {
        panel = new GradientPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // --- Title ---
        JLabel popQuiz = new JLabel("POP QUIZ", SwingConstants.CENTER);
        popQuiz.setFont(new Font("SansSerif", Font.BOLD, 80));
        popQuiz.setForeground(Color.WHITE);
        popQuiz.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Computer Science", SwingConstants.CENTER);
        subtitle.setFont(new Font("SansSerif", Font.BOLD, 48));
        subtitle.setForeground(new Color(240, 250, 255));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- Buttons ---
        JButton startBtn = new JButton("Start");
        JButton instructionsBtn = new JButton("Instructions");
        JButton exitBtn = new JButton("Exit");

        JButton[] buttons = {startBtn, instructionsBtn, exitBtn};
        for (JButton b : buttons) {
            b.setFont(new Font("SansSerif", Font.BOLD, 22));
            b.setFocusPainted(false);
            b.setBackground(new Color(255, 255, 255, 230));
            b.setForeground(new Color(40, 100, 150));
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            b.setMaximumSize(new Dimension(220, 60));
            b.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            b.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    b.setBackground(new Color(255, 255, 255, 255));
                    b.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    b.setBackground(new Color(255, 255, 255, 230));
                    b.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            });
        }

        startBtn.addActionListener(onStart);
        instructionsBtn.addActionListener(onInstructions);
        exitBtn.addActionListener(onExit);

        // --- Layout ---
        panel.add(Box.createVerticalStrut(180)); // top spacing
        panel.add(popQuiz);
        panel.add(Box.createVerticalStrut(10));
        panel.add(subtitle);
        panel.add(Box.createVerticalStrut(80));

        panel.add(startBtn);
        panel.add(Box.createVerticalStrut(20));
        panel.add(instructionsBtn);
        panel.add(Box.createVerticalStrut(20));
        panel.add(exitBtn);
        panel.add(Box.createVerticalGlue());
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }

    // Gradient background panel (light to darker blue)
    static class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            Color top = new Color(135, 206, 250);   // sky blue
            Color bottom = new Color(70, 130, 180); // steel blue
            GradientPaint gp = new GradientPaint(0, 0, top, 0, getHeight(), bottom);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
