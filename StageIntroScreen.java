// StageIntroScreen.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class StageIntroScreen implements Screen {
    private JPanel panel;

    public StageIntroScreen(int grade, ActionListener onStartGrade) {
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel("Welcome to Grade " + grade, SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setBorder(BorderFactory.createEmptyBorder(100, 0, 20, 0));

        JLabel subtitle = new JLabel(
            "Answer beginner CS questions to move up a level!",
            SwingConstants.CENTER
        );
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 18));
        subtitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        JButton startBtn = new JButton("Start Grade " + grade);
        startBtn.setFont(new Font("Arial", Font.PLAIN, 20));
        startBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        startBtn.addActionListener(onStartGrade);

        panel.add(title);
        panel.add(subtitle);
        panel.add(startBtn);
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }
}
