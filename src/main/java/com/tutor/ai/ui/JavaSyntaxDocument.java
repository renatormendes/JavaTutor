package com.tutor.ai.ui;

import javax.swing.text.*;
import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaSyntaxDocument extends DefaultStyledDocument {

	private final SimpleAttributeSet palavraChaveAttr;
	private final SimpleAttributeSet textoComumAttr;
	private final SimpleAttributeSet stringAttr;
	private final SimpleAttributeSet comentarioAttr;

	// Expressão regular com comandos e palavras-chaves do Java
	private static final String PALAVRAS_CHAVES = "\\b(public|protected|private|class|interface|enum|extends|implements|import|package|static|final|void|int|double|float|long|boolean|char|byte|short|return|if|else|for|while|do|switch|case|break|continue|new|this|super|try|catch|finally|throw|throws|String|System|out|println|print|var)\\b";
    private static final Pattern PATTERN_KEYWORDS = Pattern.compile(PALAVRAS_CHAVES);
    private static final Pattern PATTERN_STRINGS = Pattern.compile("\"([^\"\\\\]|\\\\.)*\"");
    private static final Pattern PATTERN_COMMENTS = Pattern.compile("//[^\\n]*|/\\*.*?\\*/", Pattern.DOTALL);

    public JavaSyntaxDocument() {

    	palavraChaveAttr = new SimpleAttributeSet();
        StyleConstants.setForeground(palavraChaveAttr, new Color(249, 38, 114)); // Rosa Monokai
        StyleConstants.setBold(palavraChaveAttr, true);

        textoComumAttr = new SimpleAttributeSet();
        StyleConstants.setForeground(textoComumAttr, new Color(248, 248, 242)); // Branco Monokai

        stringAttr = new SimpleAttributeSet();
        StyleConstants.setForeground(stringAttr, new Color(230, 219, 116)); // Amarelo Monokai

        comentarioAttr = new SimpleAttributeSet();
        StyleConstants.setForeground(comentarioAttr, new Color(117, 113, 94)); // Cinza Monokai

    }

    @Override
    public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
    
        super.insertString(offset, str, a);
        atualizarEstilos();
    
    }

    @Override
    public void remove(int offset, int len) throws BadLocationException {
        
        super.remove(offset, len);
        atualizarEstilos();
    
    }

    private void atualizarEstilos() {

        String texto;

        try {

            texto = getText(0, getLength());

        } catch (BadLocationException e) {

            return;

        }

        // 1. Reseta tudo para texto comum (Branco Monokai)
        setCharacterAttributes(0, getLength(), textoComumAttr, true);

        // 2. Aplica cor nas palavras-chave (Rosa Monokai)
        Matcher matcher = PATTERN_KEYWORDS.matcher(texto);

        while (matcher.find()) {

            setCharacterAttributes(matcher.start(), matcher.end() - matcher.start(), palavraChaveAttr, false);
        
        }

        // 3. Aplica cor nas Strings (Amarelo Monokai)
        matcher = PATTERN_STRINGS.matcher(texto);

        while (matcher.find()) {

            setCharacterAttributes(matcher.start(), matcher.end() - matcher.start(), stringAttr, false);
        
        }

        // 4. Aplica cor nos comentários (Cinza Monokai) - Processado por último para sobrepor Strings/Keywords internas
        matcher = PATTERN_COMMENTS.matcher(texto);

        while (matcher.find()) {

            setCharacterAttributes(matcher.start(), matcher.end() - matcher.start(), comentarioAttr, false);
       
        }

    }

}
