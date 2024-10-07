package GameManager;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.JTextField;

import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.text.JTextComponent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RoundedTextField extends JTextField {
    private int arcWidth;
    private int arcHeight;

    public RoundedTextField(int columns, int arcWidth, int arcHeight) {
        super(columns);
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setUI(new PlaceholderUI("정답을 이곳에 입력하세요!!", true));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arcWidth, arcHeight);
        super.paintComponent(g2);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getForeground());
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arcWidth, arcHeight);
        g2.dispose();
    }
}

class PlaceholderUI extends BasicTextFieldUI implements ActionListener {
    private String placeholder;
    private boolean showPlaceholder;

    public PlaceholderUI(String placeholder, boolean showPlaceholder) {
        this.placeholder = placeholder;
        this.showPlaceholder = showPlaceholder;
    }

    @Override
    protected void paintSafely(Graphics g) {
        super.paintSafely(g);
        JTextComponent comp = getComponent();
        if (showPlaceholder && comp.getText().isEmpty()) {
            g.setColor(Color.LIGHT_GRAY);
            g.setFont(comp.getFont().deriveFont(Font.ITALIC));
            int textHeight = comp.getHeight() - (comp.getHeight() - g.getFontMetrics().getHeight()) / 2-5;
            g.drawString(placeholder, (comp.getWidth() - g.getFontMetrics().stringWidth(placeholder)) / 2, textHeight);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JTextComponent comp = getComponent();
        if (!comp.getText().isEmpty()) {
            showPlaceholder = false;
        }
    }
}