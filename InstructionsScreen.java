// InstructionsScreen.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class InstructionsScreen implements Screen {
    private JPanel panel;

    public InstructionsScreen(ActionListener onBack) {
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel("Instructions", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        JTextArea text = new JTextArea(
            "Welcome to Pop Quiz: Computer Science!\n\n"
            + "• You have 5 lives.\n"
            + "• Each grade increases the number of questions.\n"
            + "• Answer correctly to progress!\n"
            + "• Lose all lives, and it’s game over!\n\n"
            + "Good luck and have fun!"
        );
        text.setFont(new Font("Arial", Font.PLAIN, 16));
        text.setEditable(false);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setBackground(Color.WHITE);
        text.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JButton backBtn = new JButton("Back");
        backBtn.setFont(new Font("Arial", Font.PLAIN, 18));
        backBtn.addActionListener(onBack);

        JPanel bottom = new JPanel();
        bottom.add(backBtn);

        panel.add(title, BorderLayout.NORTH);
        panel.add(new JScrollPane(text), BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }
}
