import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    public GameFrame() {
        setTitle("Pop Quiz: Computer Science");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel dummy = new JPanel();
        add(dummy);
        pack();

        setSize(new Dimension(1920, 1080 + getInsets().top)); 
        setLocationRelativeTo(null); 
    }

    public void showScreen(Screen screen) {
        getContentPane().removeAll();
        getContentPane().add(screen.getPanel(), BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}