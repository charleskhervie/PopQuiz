// StageVictoryScreen.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class StageVictoryScreen implements Screen {
    private JPanel panel;

    public StageVictoryScreen(int grade, ActionListener onNextGrade, ActionListener onExit) {
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel("Grade " + grade + " Complete!", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(100, 0, 20, 0));

        JLabel message = new JLabel("Congratulations! You're moving up to Grade " + (grade + 1) + "!", SwingConstants.CENTER);
        message.setFont(new Font("Arial", Font.PLAIN, 18));
        message.setAlignmentX(Component.CENTER_ALIGNMENT);
        message.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));

        JButton nextBtn = new JButton("Start Grade " + (grade + 1));
        nextBtn.setFont(new Font("Arial", Font.PLAIN, 20));
        nextBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        nextBtn.addActionListener(onNextGrade);

        JButton exitBtn = new JButton("Exit");
        exitBtn.setFont(new Font("Arial", Font.PLAIN, 18));
        exitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitBtn.addActionListener(onExit);

        panel.add(title);
        panel.add(message);
        panel.add(nextBtn);
        panel.add(Box.createVerticalStrut(10));
        panel.add(exitBtn);
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }
}
