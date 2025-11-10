// GameVictoryScreen.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GameVictoryScreen implements Screen {
    private JPanel panel;

    public GameVictoryScreen(ActionListener onReturnToTitle) {
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel("🏆 Certificate of Achievement 🏆", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 32));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(80, 0, 10, 0));

        JLabel message = new JLabel("You completed all grades in Computer Science!", SwingConstants.CENTER);
        message.setFont(new Font("Arial", Font.PLAIN, 20));
        message.setAlignmentX(Component.CENTER_ALIGNMENT);
        message.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        JButton backBtn = new JButton("Return to Title");
        backBtn.setFont(new Font("Arial", Font.PLAIN, 18));
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        backBtn.addActionListener(onReturnToTitle);

        panel.add(Box.createVerticalGlue());
        panel.add(title);
        panel.add(message);
        panel.add(backBtn);
        panel.add(Box.createVerticalGlue());
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }
}
