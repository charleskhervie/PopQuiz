// GameOverScreen.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GameOverScreen implements Screen {
    private JPanel panel;

    public GameOverScreen(ActionListener onReturnToTitle) {
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        JLabel emoji = new JLabel("💻", SwingConstants.CENTER);
        emoji.setFont(new Font("Arial", Font.PLAIN, 80));
        emoji.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Game Over", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel message = new JLabel("All lives lost!", SwingConstants.CENTER);
        message.setFont(new Font("Arial", Font.PLAIN, 20));
        message.setAlignmentX(Component.CENTER_ALIGNMENT);
        message.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0));

        JButton backBtn = new JButton("Return to Title");
        backBtn.setFont(new Font("Arial", Font.PLAIN, 20));
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        backBtn.addActionListener(onReturnToTitle);

        panel.add(Box.createVerticalGlue());
        panel.add(emoji);
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
