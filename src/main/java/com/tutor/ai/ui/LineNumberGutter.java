package com.tutor.ai.ui;

import javax.swing.*;
import javax.swing.text.Element;
import java.awt.*;

public class LineNumberGutter extends JComponent {
    private final JTextArea textArea;

    public LineNumberGutter(JTextArea textArea) {
        this.textArea = textArea;
        setBackground(new Color(34, 34, 34));
        setForeground(new Color(120, 120, 120));
        setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        textArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { repaint(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { repaint(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { repaint(); }
        });
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(35, textArea.getHeight());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(getForeground());
        g.setFont(getFont());

        Element root = textArea.getDocument().getDefaultRootElement();
        int lineCount = root.getElementCount();
        FontMetrics metrics = g.getFontMetrics();

        for (int i = 1; i <= lineCount; i++) {
            String number = String.valueOf(i);
            int y = (i * metrics.getHeight()) - metrics.getDescent() + 4;
            g.drawString(number, getWidth() - metrics.stringWidth(number) - 6, y);
        }
    }
}
