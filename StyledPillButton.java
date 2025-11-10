


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
public class StyledPillButton extends JButton {
        private static final int ARC = 36;
        private Color baseBg = new Color(255, 250, 240); // slightly warm white
        private Color hoverBg = new Color(255, 240, 200); // warm, noticeable hover
        private Color pressedBg = new Color(200, 180, 150); // darker, tactile press

        private Color borderColor = new Color(30, 40, 70); // subtle dark border
        private boolean hover = false;
        private boolean pressed = false;

    public StyledPillButton(String text) {
        super(text);
        setOpaque(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Typography: use a chunky display style; fallback to SansSerif bold
        setFont(new Font("Impact", Font.PLAIN, 36).deriveFont(Font.BOLD, 36f));
        setForeground(new Color(20, 40, 90)); // deep blue text

        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);

        // mouse behavior
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hover = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hover = false;
                pressed = false;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                pressed = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                pressed = false;
                repaint();
            }
        });

        // keyboard accessibility: show hover/press when focused/space pressed
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER) {
                    pressed = true;
                    repaint();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER) {
                    pressed = false;
                    repaint();
                }
            }
        });
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(300, 64);
    }

    @Override
    protected void paintComponent(Graphics g) {
        int width = getWidth();
        int height = getHeight();

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // shadow (drawn behind the button)
        int shadowOffsetY = hover ? 8 : 10;
        int shadowAlpha = hover ? 80 : 70;
        if (pressed) {
            shadowOffsetY = 4;
            shadowAlpha = 50;
        }
        Color shadow = new Color(0, 0, 0, shadowAlpha);
        g2.setColor(shadow);
        // slightly larger rounded rect for blur-like shadow
        g2.fillRoundRect(6, shadowOffsetY, width - 12, height - 4, ARC, ARC);

        // background
        if (pressed) {
            g2.setColor(pressedBg);
        } else if (hover) {
            g2.setColor(hoverBg);
        } else {
            g2.setColor(baseBg);
        }
        // move button down a couple px when pressed for tactile feel
        int yOffset = pressed ? 3 : 0;
        g2.fillRoundRect(0, yOffset, width - 0, height - yOffset - 1, ARC, ARC);

        // subtle border
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(2f));
        g2.drawRoundRect(0, yOffset, width - 1, height - yOffset - 1, ARC, ARC);

        // draw text with a small darker shadow to simulate inset text shadow like in image
        String text = getText();
        Font font = getFont();
        g2.setFont(font);
        FontMetrics fm = g2.getFontMetrics(font);
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getAscent();

        int tx = (width - textWidth) / 2;
        int ty = (height + textHeight) / 2 - 6 - yOffset;

        // text shadow
        g2.setColor(new Color(10, 25, 60, 120)); // deep blue shadow
        g2.drawString(text, tx + 2, ty + 3);

        // main text
        g2.setColor(getForeground());
        g2.drawString(text, tx, ty);

        g2.dispose();
    }

    // keep border shape for mouse events
    @Override
    public boolean contains(int x, int y) {
        // ensure clicks outside rounded corners are ignored - approximate using ellipse corners
        int w = getWidth();
        int h = getHeight();
        Shape r = new RoundRectangle2D.Float(0, 0, w, h, ARC, ARC);
        return r.contains(x, y);
    }
}

