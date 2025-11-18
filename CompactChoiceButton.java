import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CompactChoiceButton extends JButton {
    private Color normalBg = new Color(255, 255, 255);
    private Color hoverBg = new Color(240, 240, 255);
    private Color borderColor = new Color(80, 80, 120);
    private String label;
    private boolean hover = false;

    public CompactChoiceButton(String label, String text) {
        super(text);
        this.label = label;
        
        setOpaque(true);
        setBorderPainted(true);
        setFocusPainted(false);
        setContentAreaFilled(true);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        setFont(new Font("Arial", Font.PLAIN, 16));
        setForeground(new Color(30, 30, 30));
        setBackground(normalBg);
        
        setHorizontalAlignment(SwingConstants.LEFT);
        setVerticalAlignment(SwingConstants.CENTER);
        
        // Add padding for the label
        setMargin(new Insets(12, 60, 12, 14));
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (isEnabled()) {
                    hover = true;
                    if (getBackground().equals(normalBg)) {
                        setBackground(hoverBg);
                    }
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hover = false;
                if (getBackground().equals(hoverBg)) {
                    setBackground(normalBg);
                }
            }
        });
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(380, 60);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw label box (A, B, C, D)
        int boxSize = 38;
        int boxX = 10;
        int boxY = (getHeight() - boxSize) / 2;
        
        // Determine label box color based on button state
        Color labelBg;
        if (!isEnabled()) {
            Color btnBg = getBackground();
            if (btnBg.equals(new Color(46, 204, 113))) {
                // Green for correct
                labelBg = new Color(34, 153, 84);
            } else if (btnBg.equals(new Color(231, 76, 60))) {
                // Red for wrong
                labelBg = new Color(192, 57, 43);
            } else {
                // Gray for not selected
                labelBg = new Color(160, 160, 160);
            }
        } else {
            labelBg = new Color(100, 120, 200);
        }
        
        g2.setColor(labelBg);
        g2.fillRoundRect(boxX, boxY, boxSize, boxSize, 6, 6);
        
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(boxX, boxY, boxSize, boxSize, 6, 6);

        // Draw label text
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.setColor(Color.WHITE);
        FontMetrics fm = g2.getFontMetrics();
        int labelWidth = fm.stringWidth(label);
        int labelHeight = fm.getAscent();
        g2.drawString(label, boxX + (boxSize - labelWidth) / 2, boxY + (boxSize + labelHeight) / 2 - 2);

        g2.dispose();
    }

    @Override
    public Insets getInsets() {
        return new Insets(12, 60, 12, 14);
    }
}
