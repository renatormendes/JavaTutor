package com.tutor.ai.ui;

import com.tutor.ai.model.Mensagem;
import com.tutor.ai.service.ChatFacade;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainWindow extends JFrame {

    private JList<String> listaArquivos;
    private DefaultListModel<String> modeloLista;
    private JTextArea areaChat;
    //private JTextField campoMensagem;
    private JTextArea campoMensagem;
    private JTabbedPane painelAbasCodigo;
    private JScrollPane scrollChat;

    private JTextPane areaConsoleOutput;
    private JTextField campoConsoleInput;
    private OutputStream processoOutputStream;
    
    // Mapeamento agora gerencia JTextPane para suportar o realce de cores
    private final Map<String, JTextPane> editoresAbertos = new HashMap<>();
    private final ChatFacade chatFacade;
    private boolean iaPronta = false;
    private String arquivoUltimaAbaSelecionada = null;

    public MainWindow() {
        this.chatFacade = new ChatFacade();
        try {
            com.tutor.ai.config.AgentConfig.getChatClient();
            this.iaPronta = true;
        } catch (Exception e) {
            System.err.println("Aviso: Inicializando sem IA devido a chave ausente. " + e.getMessage());
        }

        configurarTemaEscuro();
        inicializarComponentes();
        carregarHistoricoBanco();
    }

    private void configurarTemaEscuro() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            UIManager.put("control", new Color(45, 45, 45));
            UIManager.put("nimbusBase", new Color(18, 18, 18));
            UIManager.put("nimbusFocus", new Color(0, 122, 204));
            UIManager.put("nimbusLightBackground", new Color(30, 30, 30));
            UIManager.put("nimbusSelectionBackground", new Color(0, 122, 204));
            UIManager.put("text", new Color(212, 212, 212));
        } catch (Exception ignored) {}
    }

    private void inicializarComponentes() {
        setTitle("Java AI Tutor v6 - Abas Inteligentes, Cores & FullScreen");
        setSize(1200, 750);
        
        // MODIFICAÇÃO: Inicia a janela 100% maximizada no Linux Lite 8
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Color corEditores = new Color(24, 24, 24);
        Color corTexto = new Color(212, 212, 212);
        Font fonteMonospace = new Font("Monospaced", Font.PLAIN, 13);

        // --- LATERAL ESQUERDA: EXPLORADOR + CHAT ---
        JPanel painelEsquerdo = new JPanel(new BorderLayout(5, 5));
        painelEsquerdo.setPreferredSize(new Dimension(380, 750));
        painelEsquerdo.setBorder(new EmptyBorder(10, 10, 10, 5));

        modeloLista = new DefaultListModel<>();
        listaArquivos = new JList<>(modeloLista);
        listaArquivos.setBackground(corEditores);
        listaArquivos.setForeground(corTexto);
        JScrollPane scrollArquivos = new JScrollPane(listaArquivos);
        scrollArquivos.setPreferredSize(new Dimension(380, 120));

        JButton botaoNovoArquivo = new JButton("🆕 Novo Arquivo .java");
        botaoNovoArquivo.setBackground(new Color(0, 122, 204));
        botaoNovoArquivo.setForeground(Color.WHITE);
        
        JPanel painelSuperiorLateral = new JPanel(new BorderLayout(5, 5));
        painelSuperiorLateral.add(new JLabel("📁 Arquivos do Projeto"), BorderLayout.NORTH);
        painelSuperiorLateral.add(scrollArquivos, BorderLayout.CENTER);
        painelSuperiorLateral.add(botaoNovoArquivo, BorderLayout.SOUTH);

        String msgBoasVindas = iaPronta 
            ? "Tutor: Olá! Chave GEMINI_API_KEY detectada no seu terminal. Modelo 3.5-Flash Ativo!\n\n"
            : "Tutor: Olá! [Aviso: GEMINI_API_KEY não detectada]. Rode 'source ~/.bashrc'.\n\n";

        areaChat = new JTextArea(msgBoasVindas);
        areaChat.setBackground(corEditores);
        areaChat.setForeground(corTexto);
        areaChat.setEditable(false);
        areaChat.setLineWrap(true);
        areaChat.setWrapStyleWord(true);
        
        scrollChat = new JScrollPane(areaChat);
        scrollChat.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        // --- CHAT AGORA ACEITA MÚLTIPLAS LINHAS ---
        campoMensagem = new JTextArea(3, 20); // 3 linhas de altura inicial
        campoMensagem.setBackground(new Color(60, 60, 60));
        campoMensagem.setForeground(Color.WHITE);
        campoMensagem.setCaretColor(Color.WHITE);
        campoMensagem.setLineWrap(true);
        campoMensagem.setWrapStyleWord(true);
        JScrollPane scrollInputChat = new JScrollPane(campoMensagem);
        
        JButton botaoEnviar = new JButton("Enviar");
        botaoEnviar.setBackground(new Color(0, 122, 204));
        botaoEnviar.setForeground(Color.WHITE);

        // --- NOVOS BOTÕES: EXPORTAR E LIMPAR CHAT ---
        JButton botaoPdf = new JButton("📄 PDF");
        botaoPdf.setBackground(new Color(108, 117, 125));
        botaoPdf.setForeground(Color.WHITE);
        
        JButton botaoLimpar = new JButton("🗑️ Limpar");
        botaoLimpar.setBackground(new Color(220, 53, 69));
        botaoLimpar.setForeground(Color.WHITE);

        // Grid auxiliar para os botões do chat
        JPanel painelBotoesDireita = new JPanel(new GridLayout(3, 1, 0, 2));
        painelBotoesDireita.add(botaoEnviar);
        painelBotoesDireita.add(botaoPdf);
        painelBotoesDireita.add(botaoLimpar);

        JPanel painelEnvioChat = new JPanel(new BorderLayout(5, 5));
        painelEnvioChat.add(campoMensagem, BorderLayout.CENTER);
        painelEnvioChat.add(botaoEnviar, BorderLayout.EAST);

        JPanel painelChatCompleto = new JPanel(new BorderLayout(5, 5));
        painelChatCompleto.add(new JLabel("🤖 Chat de Mentoria"), BorderLayout.NORTH);
        painelChatCompleto.add(scrollChat, BorderLayout.CENTER);
        painelChatCompleto.add(painelEnvioChat, BorderLayout.SOUTH);

        JSplitPane divisoriaEsquerda = new JSplitPane(JSplitPane.VERTICAL_SPLIT, painelSuperiorLateral, painelChatCompleto);
        divisoriaEsquerda.setDividerLocation(180);
        painelEsquerdo.add(divisoriaEsquerda, BorderLayout.CENTER);

        // --- CENTRAL / DIREITA: EDITOR DE CÓDIGO ---
        JPanel painelDireito = new JPanel(new BorderLayout(5, 5));
        painelDireito.setBorder(new EmptyBorder(10, 5, 10, 10));

        JPanel painelAcoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));

        // Cria a nossa barra de atalhos externa passando as abas e os editores como referência
        var barraFormtacaoRápida = new com.tutor.ai.ui.FormattingToolBar(painelAbasCodigo, editoresAbertos);

        JPanel painelSuperiorDireitoCompleto = new JPanel(new GridLayout(2, 1, 0, 2));
        painelSuperiorDireitoCompleto.add(painelAcoes);
        painelSuperiorDireitoCompleto.add(barraFormtacaoRápida); // Acopla a calha de botões Java

        painelDireito.add(painelSuperiorDireitoCompleto, BorderLayout.NORTH); // Altera o norte do layout

        JButton botaoSalvar = new JButton("💾 Salvar Aba");
        JButton botaoExecutar = new JButton("▶ Executar Código");
        botaoSalvar.setBackground(new Color(40, 167, 69));
        botaoSalvar.setForeground(Color.WHITE);
        botaoExecutar.setBackground(new Color(224, 168, 0));
        botaoExecutar.setForeground(Color.BLACK);
        painelAcoes.add(botaoSalvar);
        painelAcoes.add(botaoExecutar);

        painelAbasCodigo = new JTabbedPane();
        painelDireito.add(painelAcoes, BorderLayout.NORTH);
        painelDireito.add(painelAbasCodigo, BorderLayout.CENTER);

        // --- CONFIGURAÇÃO DO NOVO CONSOLE INTERATIVO (INFERIOR) ---
        JPanel painelConsole = new JPanel(new BorderLayout(5, 5));
        painelConsole.setPreferredSize(new Dimension(820, 180)); // Altura do terminal
        
        areaConsoleOutput = new JTextPane();
        areaConsoleOutput.setBackground(new Color(15, 15, 15)); // Preto terminal
        areaConsoleOutput.setForeground(new Color(0, 255, 0));  // Verde matriz padrão
        areaConsoleOutput.setFont(fonteMonospace);
        areaConsoleOutput.setEditable(false);
        JScrollPane scrollConsole = new JScrollPane(areaConsoleOutput);

        campoConsoleInput = new JTextField();
        campoConsoleInput.setBackground(new Color(30, 30, 30));
        campoConsoleInput.setForeground(Color.WHITE);
        campoConsoleInput.setCaretColor(Color.WHITE);
        campoConsoleInput.setFont(fonteMonospace);
        campoConsoleInput.setEnabled(false); // Só ativa quando o programa do aluno rodar

        JPanel painelInputAux = new JPanel(new BorderLayout());
        JLabel labelPrompt = new JLabel("  >  ");
        labelPrompt.setForeground(Color.GREEN);
        painelInputAux.add(labelPrompt, BorderLayout.WEST);
        painelInputAux.add(campoConsoleInput, BorderLayout.CENTER);

        painelConsole.add(new JLabel("💻 Console Interativo de Execução (Entrada/Saída de Dados)"), BorderLayout.NORTH);
        painelConsole.add(scrollConsole, BorderLayout.CENTER);
        painelConsole.add(painelInputAux, BorderLayout.SOUTH);

        // Divisória vertical: Código em cima, Console embaixo
        JSplitPane divisoriaDireitaModerna = new JSplitPane(JSplitPane.VERTICAL_SPLIT, painelAbasCodigo, painelConsole);
        divisoriaDireitaModerna.setDividerLocation(450); // Ajuste de proporção espacial
        divisoriaDireitaModerna.setResizeWeight(0.7);

        painelDireito.add(painelAcoes, BorderLayout.NORTH);
        painelDireito.add(divisoriaDireitaModerna, BorderLayout.CENTER); // Injeta o layout dividido


        JSplitPane painelDivididoPrincipal = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, painelEsquerdo, painelDireito);
        painelDivididoPrincipal.setDividerLocation(380);
        add(painelDivididoPrincipal, BorderLayout.CENTER);

        // --- ATIVAR O MÓDULO DE SALVAMENTO AUTOMÁTICO AO ALTERNAR ABAS ---
        painelAbasCodigo.addChangeListener(e -> {

            // Executa o salvamento automático da aba anterior antes de focar na nova
            if (arquivoUltimaAbaSelecionada != null && editoresAbertos.containsKey(arquivoUltimaAbaSelecionada)) {

                salvarArquivoPorNome(arquivoUltimaAbaSelecionada);

            }

            int index = painelAbasCodigo.getSelectedIndex();

            if (index != -1) {

                arquivoUltimaAbaSelecionada = painelAbasCodigo.getTitleAt(index);

            } else {

                arquivoUltimaAbaSelecionada = null;

            }

        });

        // Evento de Exportação para PDF
        botaoPdf.addActionListener(e -> {
            List<Mensagem> historico = chatFacade.obterHistoricoCompleto();
            String arquivoGerado = com.tutor.ai.service.PdfExportService.exportarHistoricoParaPdf(historico);
            if (arquivoGerado != null) {
                JOptionPane.showMessageDialog(this, "Histórico exportado com sucesso!\nArquivo: " + arquivoGerado);
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao gerar relatório em PDF.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Evento de Limpar Chat (Salva antes em PDF por segurança)
        botaoLimpar.addActionListener(e -> {
            int confirmacao = JOptionPane.showConfirmDialog(this, 
                "Deseja limpar o chat? Um backup em PDF do histórico atual será gerado automaticamente.", 
                "Limpar Histórico", JOptionPane.YES_NO_OPTION);
                
            if (confirmacao == JOptionPane.YES_OPTION) {
                List<Mensagem> historico = chatFacade.obterHistoricoCompleto();
                String backup = com.tutor.ai.service.PdfExportService.exportarHistoricoParaPdf(historico);
                
                // Vira a chave limpando fisicamente os registros da tabela SQLite
                com.tutor.ai.database.ChatDAO chatDAO = new com.tutor.ai.database.ChatDAO();
                for (Object obj : historico) {
                    Mensagem msg = (Mensagem) obj;
                    chatDAO.deletar(msg.getId());
                }
                
                areaChat.setText("Tutor: Histórico limpo! Backup de segurança gerado em: " + backup + "\n\n");
                rolarChatParaFinal();
            }
        });

        // Eventos e Listeners
        botaoEnviar.addActionListener(e -> executarEnvioMensagemComIA());
        
        // --- ATALHO CTRL + ENTER PARA ENVIAR MENSAGEM DO JTEXTAREA ---
        campoMensagem.getInputMap(JComponent.WHEN_FOCUSED).put(
            KeyStroke.getKeyStroke("control ENTER"), "enviarComIa"
        );
        campoMensagem.getActionMap().put("enviarComIa", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                executarEnvioMensagemComIA();
            }
        });
        
        botaoSalvar.addActionListener(e -> salvarArquivoAbaAtiva());
        botaoExecutar.addActionListener(e -> executarCodigoJava());
        botaoNovoArquivo.addActionListener(e -> criarNovoArquivoFluxo(corEditores, corTexto, fonteMonospace));

        // Trata o clique do 'Enter' no console para responder ao Scanner do programa executado
        campoConsoleInput.addActionListener(e -> {
            String inputAluno = campoConsoleInput.getText();
            if (processoOutputStream != null && !inputAluno.isEmpty()) {
                try {
                    // Adiciona o texto no console visual com cor branca indicando entrada
                    imprimirNoConsole(inputAluno + "\n", Color.WHITE);
                    
                    // Escreve no fluxo do processo real e dá descarga (flush)
                    processoOutputStream.write((inputAluno + "\n").getBytes());
                    processoOutputStream.flush();
                    
                    campoConsoleInput.setText("");
                } catch (IOException ex) {
                    imprimirNoConsole("\n[Erro ao enviar dados para o programa]\n", Color.RED);
                }
            }
        });


        listaArquivos.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {

                if (evt.getClickCount() == 2) {

                    String arq = listaArquivos.getSelectedValue();

                    if (arq != null) abrirArquivoEmAba(arq, corEditores, corTexto, fonteMonospace);

                }

            }

        });

        buscarArquivosLocais();

    }

    private void abrirArquivoEmAba(String nomeArquivo, Color fundo, Color texto, Font fonte) {

        if (editoresAbertos.containsKey(nomeArquivo)) {

            painelAbasCodigo.setSelectedComponent(editoresAbertos.get(nomeArquivo).getParent().getParent());
            return;

        }

        try {

            String conteudo = Files.readString(new File(nomeArquivo).toPath());
            
            // MODIFICAÇÃO: Acopla o documento de coloração de sintaxe Java
            JavaSyntaxDocument docColorido = new JavaSyntaxDocument();
            JTextPane novoEditor = new JTextPane(docColorido);
            novoEditor.setText(conteudo);
            
            novoEditor.setBackground(fundo);
            novoEditor.setForeground(texto);
            novoEditor.setFont(fonte);
            novoEditor.setCaretColor(Color.WHITE);

            // --- NOVO RECURSO: AJUSTE DE INDENTAÇÃO (TAB = 4 ESPAÇOS) ---
            InputMap inputMap = novoEditor.getInputMap(JComponent.WHEN_FOCUSED);
            ActionMap actionMap = novoEditor.getActionMap();

            inputMap.put(KeyStroke.getKeyStroke("TAB"), "inserirQuatroEspacos");
            actionMap.put("inserirQuatroEspacos", new AbstractAction() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    try {
                        novoEditor.getDocument().insertString(novoEditor.getCaretPosition(), "    ", null);
                    } catch (BadLocationException ignored) {}
                }
            });
            // -----------------------------------------------------------

            JScrollPane scroll = new JScrollPane(novoEditor);
            
            // Injeta o módulo anterior de numeração de linhas laterais convertido para JTextArea
            JTextArea areaNumeros = new JTextArea("1");
            areaNumeros.setBackground(new Color(34, 34, 34));
            areaNumeros.setForeground(new Color(120, 120, 120));
            areaNumeros.setFont(fonte);
            areaNumeros.setEditable(false);
            
            novoEditor.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {

                private void atualizar() {

                    int linhas = docColorido.getDefaultRootElement().getElementCount();
                    StringBuilder sb = new StringBuilder();
                    for(int i=1; i<=linhas; i++) sb.append(i).append("\n");
                    areaNumeros.setText(sb.toString());

                }

                public void insertUpdate(javax.swing.event.DocumentEvent e) { atualizar(); }

                public void removeUpdate(javax.swing.event.DocumentEvent e) { atualizar(); }

                public void changedUpdate(javax.swing.event.DocumentEvent e) { atualizar(); }

            });

            scroll.setRowHeaderView(areaNumeros);
            painelAbasCodigo.addTab(nomeArquivo, scroll);
            editoresAbertos.put(nomeArquivo, novoEditor);

            // --- ATIVAR MÓDULO DE BOTÃO FECHAR (X) INDIVIDUAL ---
            int indexBotao = painelAbasCodigo.indexOfComponent(scroll);
            JPanel painelAbaCustomizada = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            painelAbaCustomizada.setOpaque(false);
            JLabel labelAba = new JLabel(nomeArquivo);
            JButton botaoFecharX = new JButton("x");
            botaoFecharX.setMargin(new Insets(0, 4, 0, 4));
            botaoFecharX.setFocusable(false);
            botaoFecharX.setBorderPainted(false);
            botaoFecharX.setContentAreaFilled(false);
            botaoFecharX.setForeground(Color.RED);
            botaoFecharX.setCursor(new Cursor(Cursor.HAND_CURSOR));

            botaoFecharX.addActionListener(evt -> {

                salvarArquivoPorNome(nomeArquivo); 

                // Força gravação ao fechar a aba
                int indexAba = painelAbasCodigo.indexOfTab(nomeArquivo);

                if (indexAba != -1) {

                    painelAbasCodigo.remove(indexAba);
                    editoresAbertos.remove(nomeArquivo);

                }

            });

            painelAbaCustomizada.add(labelAba);
            painelAbaCustomizada.add(botaoFecharX);
            painelAbasCodigo.setTabComponentAt(indexBotao, painelAbaCustomizada);
            painelAbasCodigo.setSelectedComponent(scroll);

        } catch (IOException e) {

            JOptionPane.showMessageDialog(this, "Erro ao abrir o arquivo.");

        }

    }

    private void salvarArquivoAbaAtiva() {

        int index = painelAbasCodigo.getSelectedIndex();
        if (index == -1) return;
        String nomeArquivo = painelAbasCodigo.getTitleAt(index);
        salvarArquivoPorNome(nomeArquivo);

    }

    private void salvarArquivoPorNome(String nomeArquivo) {

        JTextPane editor = editoresAbertos.get(nomeArquivo);

        if (editor != null) {

            try {

                Files.writeString(new File(nomeArquivo).toPath(), editor.getText());

            } catch (IOException e) {

                System.err.println("Erro no salvamento automático de: " + nomeArquivo);

            }

        }

    }

    private void criarNovoArquivoFluxo(Color fundo, Color texto, Font fonte) {

        String nomeClasse = JOptionPane.showInputDialog(this, "Nome da classe Java:", "Criar Arquivo", JOptionPane.QUESTION_MESSAGE);
        if (nomeClasse == null || nomeClasse.trim().isEmpty()) return;
        
        nomeClasse = nomeClasse.trim().replaceAll("\s+", "");
        if (!nomeClasse.endsWith(".java")) nomeClasse += ".java";
        
        File arquivo = new File(nomeClasse);
        if (arquivo.exists()) return;
        
        try {

            String classeLimpa = arquivo.getName().replace(".java", "");
            String template = "public class " + classeLimpa + 
            " {\n public static void main(String[] args) {\n System.out.println(" + "Executando Java!" + ");\n    }\n}";
            Files.writeString(arquivo.toPath(), template);
            buscarArquivosLocais();
            abrirArquivoEmAba(arquivo.getName(), fundo, texto, fonte);

        } catch (IOException ignored) {}

    }

    private void executarEnvioMensagemComIA() {

        String texto = campoMensagem.getText().trim();

        if (texto.isEmpty()) return;

        areaChat.append("\n\nVocê: " + texto + "\n");
        campoMensagem.setText("");
        rolarChatParaFinal();
        int index = painelAbasCodigo.getSelectedIndex();
        String nomeArquivoAtivo = (index != -1) ? painelAbasCodigo.getTitleAt(index) : "Nenhum";
        String codigoContexto = (index != -1) ? editoresAbertos.get(nomeArquivoAtivo).getText() : "";
        chatFacade.processarNovaMensagem("\n\nUsuário", texto, nomeArquivoAtivo);

        new Thread(() -> {

            String respostaIA;

            if (iaPronta) {

                String instrucaoSistema = "\nVocê é um tutor especialista em programação Java para iniciantes. Analise o código do aluno didaticamente.";
                String promptCompleto = texto + "\n\n[Contexto do Código (" + nomeArquivoAtivo + ")]:\n" + codigoContexto;
                respostaIA = com.tutor.ai.config.AgentConfig.enviarPrompt(instrucaoSistema, promptCompleto);

            } else {

                respostaIA = "\nTutor IA: Configure a GEMINI_API_KEY no terminal.\n";

            }

            chatFacade.processarNovaMensagem("\n\nTutor", respostaIA, nomeArquivoAtivo);

            SwingUtilities.invokeLater(() -> {

                areaChat.append("\n\nTutor:\n" + respostaIA + "\n\n");
                rolarChatParaFinal();

            });

        })

        .start();

    }

    private void executors_mudar_nome_se_quiser_executarCodigoJava() {} // Apenas referência visual

    private void executarCodigoJava() {
        int index = painelAbasCodigo.getSelectedIndex();
        if (index == -1) return;
        String nomeArquivo = painelAbasCodigo.getTitleAt(index);
        salvarArquivoPorNome(nomeArquivo);
        
        limparConsole();
        imprimirNoConsole("Compilando e inicializando " + nomeArquivo + " via Java 25...\n", Color.CYAN);
        campoConsoleInput.setEnabled(true);
        campoConsoleInput.requestFocus();

        new Thread(() -> {
            try {
                // Executa usando o recurso JEP do Java para arquivo único de código fonte
                ProcessBuilder pb = new ProcessBuilder("java", nomeArquivo);
                Process processo = pb.start();

                // Captura o canal de saída de escrita do processo
                processoOutputStream = processo.getOutputStream();

                // Thread A: Lê a Saída Padrão (Verde)
                Thread threadSaida = new Thread(() -> {
                    try (BufferedReader leitor = new BufferedReader(new InputStreamReader(processo.getInputStream()))) {
                        String linha;
                        while ((linha = leitor.readLine()) != null) {
                            imprimirNoConsole(linha + "\n", Color.GREEN);
                        }
                    } catch (IOException ignored) {}
                });

                // Thread B: Lê a Saída de Erros do Compilador/JVM (Vermelho)
                Thread threadErros = new Thread(() -> {
                    try (BufferedReader leitorErro = new BufferedReader(new InputStreamReader(processo.getErrorStream()))) {
                        String linhaErro;
                        while ((linhaErro = leitorErro.readLine()) != null) {
                            imprimirNoConsole(linhaErro + "\n", Color.RED);
                        }
                    } catch (IOException ignored) {}
                });

                threadSaida.start();
                threadErros.start();

                // Aguarda o término da execução do arquivo do aluno
                int codigoTermino = processo.waitFor();
                
                threadSaida.join();
                threadErros.join();

                imprimirNoConsole("\nProcesso finalizado com código: " + codigoTermino + "\n", Color.CYAN);
            } catch (Exception ex) {
                imprimirNoConsole("Erro de Execução Crítico: " + ex.getMessage() + "\n", Color.RED);
            } finally {
                // Reseta e desabilita os controles de input após o encerramento do script
                processoOutputStream = null;
                SwingUtilities.invokeLater(() -> campoConsoleInput.setEnabled(false));
            }
        }).start();
    }

    private void rolarChatParaFinal() {

        SwingUtilities.invokeLater(() -> {

            JScrollBar verticalBar = scrollChat.getVerticalScrollBar();
            verticalBar.setValue(verticalBar.getMaximum());

        });

    }

    private void carregarHistoricoBanco() {

        List historico = chatFacade.obterHistoricoCompleto();

        for (Object obj : historico) {

            Mensagem msg = (Mensagem) obj;
            areaChat.append(msg.getRemetente() + ": " + msg.getMensagem() + "\n");

        }

        if (!historico.isEmpty()) {

            areaChat.append("\n\n--- Histórico SQLite restaurado ---\n\n");
            rolarChatParaFinal();

        }

    }

    private void buscarArquivosLocais() {

        File[] arquivos = new File(".").listFiles((dir, name) -> name.endsWith(".java"));
        modeloLista.clear();

        if (arquivos != null) {

            for (File arquivo : arquivos) modeloLista.addElement(arquivo.getName());

        }

    }

    private void imprimirNoConsole(String texto, Color cor) {
        SwingUtilities.invokeLater(() -> {
            try {
                var doc = areaConsoleOutput.getStyledDocument();
                var estilo = areaConsoleOutput.addStyle("EstiloConsole", null);
                javax.swing.text.StyleConstants.setForeground(estilo, cor);
                doc.insertString(doc.getLength(), texto, estilo);
                areaConsoleOutput.setCaretPosition(doc.getLength()); // Auto-scroll
            } catch (Exception ignored) {}
        });
    }

    private void limparConsole() {
        SwingUtilities.invokeLater(() -> areaConsoleOutput.setText(""));
    }


    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> new MainWindow().setVisible(true));

    }

}
