package com.tutor.ai.ui;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class FormattingToolBar extends JToolBar {

    private final JTabbedPane painelAbas;
    private final Map<String, JTextPane> editores;

    public FormattingToolBar(JTabbedPane painelAbas, Map<String, JTextPane> editores) {
        this.painelAbas = painelAbas;
        this.editores = editores;
        
        setFloatable(false);
        setBackground(new Color(35, 35, 35));
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 2));

        // Botões de Injeção de Código
        criarBotaoAtalho("<html><b>for</b></html>", "for (int i = 0; i < 10; i++) {\n    \n}");
        criarBotaoAtalho("<html><b>if/else</b></html>", "if (condicao) {\n    \n} else {\n    \n}");
        criarBotaoAtalho("psvm", "public static void main(String[] args) {\n    \n}");
        criarBotaoAtalho("sout", "System.out.println(\"\");");
        criarBotaoAtalho("Scanner", "import java.util.Scanner;\nScanner teclado = new Scanner(System.in);");
    }

    private void criarBotaoAtalho(String rotulo, String snippet) {
        JButton botao = new JButton(rotulo);
        botao.setBackground(new Color(55, 55, 55));
        botao.setForeground(Color.WHITE);
        botao.setFocusable(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));

        botao.addActionListener(e -> {
            int index = painelAbas.getSelectedIndex();
            if (index == -1) return;
            String nomeArquivo = painelAbas.getTitleAt(index);
            JTextPane editorAtivo = editores.get(nomeArquivo);

            if (editorAtivo != null) {
                try {
                    int posicaoCursor = editorAtivo.getCaretPosition();
                    editorAtivo.getDocument().insertString(posicaoCursor, snippet, null);
                    editorAtivo.requestFocus();
                } catch (Exception ignored) {}
            }
        });
        add(botao);
    }
}
