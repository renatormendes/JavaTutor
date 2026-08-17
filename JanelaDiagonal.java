import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import javax.swing.SwingUtilities;

// 1. Criamos uma classe que representa a nossa janela (JFrame)
public class JanelaDiagonal extends JFrame {

    public JanelaDiagonal() {
        // Define o título da janela
        setTitle("Diagonal 300x300");
        
        // Define o tamanho da janela (largura, altura) em pixels
        setSize(300, 300);
        
        // Finaliza o programa de vez quando o usuário fecha a janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // O SEGREDO DA CENTRALIZAÇÃO: passar 'null' centraliza na tela do computador
        setLocationRelativeTo(null);
        
        // Adiciona a nossa tela de desenho dentro da janela
        add(new PainelDesenho());
    }

    // 2. Criamos uma classe interna que será a nossa "tela de pintura" (JPanel)
    class PainelDesenho extends JPanel {
        
        // O método paintComponent é onde a mágica do desenho acontece
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); // Limpa a tela antes de desenhar
            
            // Define a cor do nosso pincel
            g.setColor(Color.BLUE);
            
            // Desenha a linha diagonal.
            // g.drawLine(x1, y1, x2, y2)
            // No Java, o ponto (0,0) é o canto superior esquerdo.
            // getWidth() e getHeight() pegam a largura e altura atuais da janela.
            g.drawLine(0, 0, getWidth(), getHeight());
        }
    }

    // 3. O método principal para rodar o programa
    public static void main(String[] args) {
        // É uma boa prática do Java rodar a interface gráfica em uma thread segura
        SwingUtilities.invokeLater(() -> {
            JanelaDiagonal janela = new JanelaDiagonal();
            janela.setVisible(true); // Torna a janela visível
        });
    }
}
