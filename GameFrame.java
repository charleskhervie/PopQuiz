import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    public GameFrame() {
        setTitle("Pop Quiz: Computer Science");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Create a panel just to get the proper size
        JPanel dummy = new JPanel();
        add(dummy);
        pack();

        // Set the content area to exactly 1920x1080
        setSize(new Dimension(1920, 1080 + getInsets().top)); // add title bar height
        setLocationRelativeTo(null); // center on screen
    }

    public void showScreen(Screen screen) {
        getContentPane().removeAll();
        getContentPane().add(screen.getPanel(), BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}