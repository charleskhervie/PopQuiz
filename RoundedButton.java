
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RoundedButton extends JButton {
    private int radius;
    private Color normalBg;
    private Color hoverBg;
    private Color pressedBg;

    public RoundedButton(String text, int radius) {
        super(text);
        this.radius = radius;

        normalBg = new Color(255, 250, 240);
        hoverBg = new Color(230, 230, 255);
        pressedBg = new Color(200, 200, 255);

        setBackground(normalBg);
        setForeground(new Color(0, 0, 139));
        setFont(new Font("Impact", Font.BOLD, 20));
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setMargin(new Insets(10, 20, 10, 20));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { setBackground(hoverBg); }
            @Override
            public void mouseExited(MouseEvent e)  { setBackground(normalBg); }
            @Override
            public void mousePressed(MouseEvent e) { setBackground(pressedBg); }
            @Override
            public void mouseReleased(MouseEvent e){ setBackground(hoverBg); }
        });
    }

    @Override
    public Insets getInsets() { return new Insets(10, 20, 10, 20); }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        super.paintComponent(g2);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getForeground());
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        g2.dispose();
    }
}
