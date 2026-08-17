<img width="500" height="300" alt="img001" src="https://github.com/user-attachments/assets/e8d87026-7ebf-42b3-8abb-e56a4e9e9bac" />
<img width="500" height="300" alt="img002" src="https://github.com/user-attachments/assets/b65934d0-c2e9-47db-8120-7845551b8f17" />
<img width="500" height="300" alt="img003" src="https://github.com/user-attachments/assets/7fb82e66-9c14-403d-926f-974b5353fc88" />
<img width="500" height="300" alt="img004" src="https://github.com/user-attachments/assets/ea82681b-177c-4330-bbf5-c85ca8952cd9" />
<img width="500" height="300" alt="img005" src="https://github.com/user-attachments/assets/057b4230-478e-4338-b247-45d7efed7d49" />
<img width="500" height="300" alt="img006" src="https://github.com/user-attachments/assets/41596be1-a474-4ff8-b550-9e62b488bf75" />


# 🤖 Java AI Tutor — IDE Interativa & Mentor de Programação com Gemini 3.5

Um ambiente de desenvolvimento integrado (IDE) de alta performance e consumo ultra-baixo de memória, projetado especificamente para auxiliar programadores iniciantes no aprendizado da linguagem Java. O sistema une um editor multimídia com abas editáveis, realce de sintaxe nativo estilo Monokai, console interativo bidirecional capaz de processar fluxos de leitura (`Scanner`), atalhos de formatação rápida de código e um tutor pedagógico integrado ao **Google Gemini 3.5-Flash**.

---

## 🏛️ Desafio de Engenharia e Arquitetura de Software

O maior diferencial técnico deste projeto foi o seu desenvolvimento sob restrições severas de infraestrutura de hardware:
* **Sistema Operacional:** Linux Lite 8
* **Hardware Alvo:** Máquina com apenas **4GB de Memória RAM**
* **Linguagem Base:** Java 25 (Ambiente de visualização via JEP de arquivo único de código fonte)
* **Banco de Dados:** SQLite3 local

### A Tomada de Decisão: Por que Swing e SDK Nativo?
Inicialmente, cogitou-se o uso de JavaFX com Spring AI (Spring Boot 3). Contudo, em perfis de hardware com 4GB de RAM, a JVM do Spring Boot associada ao WebKit do JavaFX gerava gargalos severos de paginação em disco (*swap memory*), além de estouros de buffer. 

**A Solução de Engenharia:** Desenvolver a interface puramente em **Java Swing (com Look and Feel Nimbus customizado em Dark Mode)** e substituir o Spring AI pelo **SDK oficial e nativo de IA da Google (`com.google.genai`)**. Isso reduziu o consumo de memória RAM em mais de 60%, garantindo inicialização instantânea e fluidez absoluta no Linux Lite.

---

## ✨ Funcionalidades do Ecossistema

* **IDE Multitabs (`JTabbedPane`):** Permite abrir e gerenciar múltiplos arquivos `.java` simultaneamente em abas com botões individuais de fechamento `(X)`.
* **Salvamento Automático por Alternância:** Ao alternar de uma aba para outra ou ao fechar uma aba, a IDE grava o código modificado em disco de forma transparente, prevenindo perdas de código pelo aluno.
* **Realce de Sintaxe Nativo (*Syntax Highlighting*):** Motor gráfico baseado em Regex que colore palavras-chave (Rosa), Strings (Amarelo) e Comentários (Cinza) em tempo real, fornecendo uma experiência visual idêntica ao clássico tema Monokai do *Sublime Text 4*.
* **Régua Numérica Lateral Inteligente:** Calha de contagem de linhas automática e dinâmica acoplada ao scroll de cada aba de código.
* **Barra de Atalhos de Snippets (`FormattingToolBar`):** Permite a injeção instantânea de blocos estruturais de código (como `for`, `if/else`, `public static void main`, `System.out.println` e instanciação de `Scanner`) na posição exata do cursor do aluno.
* **Console Interativo de Execução (E/S de Dados):** Terminal integrado assíncrono que compila e roda a classe atual usando `ProcessBuilder` nativo do Java 25. Ele diferencia saídas do console (Verde), entradas do usuário (Branco) e logs de erros de compilação (Vermelho). O console aceita fluxo de entrada dinâmico via teclado (essencial para testar códigos com `Scanner(System.in)`).
* **Persistência Híbrida de Chat (Buffer Binário + SQLite):** Para mitigar qualquer latência de I/O bloqueante na interface gráfica, as mensagens enviadas são gravadas de forma atômica imediata em um arquivo serializado binário `.dat`. Uma thread assíncrona recolhe o buffer e transfere os dados para o banco permanente **SQLite3**.
* **Sistema de Exportação e Limpeza:** Permite gerar relatórios completos das sessões de mentoria em formato **PDF** via biblioteca **OpenPDF**, salvando um backup de segurança físico de forma automática antes de limpar as tabelas do banco de dados.

---

## 🛠️ Tecnologias e Dependências do Ecossistema

O arquivo `pom.xml` foi mantido enxuto e limpo para garantir o build estável no Maven Central, livre de dependências fantasmas ou instabilidades de repositórios experimentais:

* **`com.google.genai:google-genai:1.24.0`** - SDK oficial da Google para comunicação direta de baixa latência com o Gemini.
* **`com.github.librepdf:openpdf:1.3.30`** - Biblioteca leve para geração de relatórios estruturados em PDF.
* **`org.xerial:sqlite-jdbc:3.45.1.0`** - Driver JDBC para conexão local e persistência em arquivo com o SQLite.

---

## 📜 Histórico de Desafios Técnicos Encontrados & Soluções Aplicadas

Durante o ciclo de desenvolvimento em ambiente Java moderno puro, deparamo-nos com desafios técnicos cruciais de compilação, tipagem e interface:

### 1. Conflito de Resolução de Dependências da Spring AI
* **Problema:** O Maven Central acusava erros de *Missing Dependency Version* ou *Artifact Not Found* ao tentar buscar os starters da Spring AI para o Gemini.
* **Causa:** Versões da linha `1.x` e `2.x` da Spring AI dependem de repositórios exclusivos de Milestones (`repo.spring.io`) e exigem a acoplagem pesada do Spring Boot Starter para expor propriedades de autoconfiguração.
* **Solução:** Substituição completa do ecossistema Spring AI pelo SDK Nativo em Java SE do Google GenAI. O código tornou-se mais enxuto, direto e independente de servidores de marcos (*milestones*).

### 2. Ganância de Expressão Regular em Comentários de Uma Linha (`//`)
* **Problema:** Ao inserir um comentário de uma linha (`//`), todo o código restante do arquivo Java abaixo dele assumia a cor cinza dos comentários, quebrando o realce de sintaxe.
* **Causa:** O padrão Regex utilizado (`//.*`) avaliava de forma gananciosa incluindo os caracteres de controle invisíveis de quebra de linha.
* **Solução:** O padrão foi ajustado cirurgicamente para `//[^\\n]*`. Desse modo, o analisador léxico encerra a pintura cinza estritamente no final da linha atual, retornando as cores corretas à linha subsequente.

### 3. Falha de Tipagem Genérica Bruta na Restauração do SQLite
* **Problema:** O Maven Compiler Plugin disparava o erro `incompatible types: java.lang.Object cannot be converted to com.tutor.ai.model.Mensagem`.
* **Causa:** A inferência automática do laço `for` do Java 25 interpretou a lista retornada por reflexão do banco de dados como uma coleção de objetos genéricos brutos (*raw type Object*).
* **Solução:** Implementação de um *cast* explícito no laço de repetição (`Mensagem msg = (Mensagem) obj;`), blindando a compilação de forma estática.

### 4. Travamento da Interface Gráfica durante Entrada de Dados
* **Problema:** A Janela Swing congelava e parava de responder se o programa Java compilado do aluno ficasse aguardando um input via `teclado.nextLine()`.
* **Causa:** O processamento do fluxo de entrada do console estava rodando na *Event Dispatch Thread* (EDT), que é a thread principal responsável pelo desenho dos botões da tela.
* **Solução:** O processo de execução gráfica foi isolado e chaveado para rodar em uma **Thread Secundária**, utilizando sub-threads independentes para escutar a saída padrão e a saída de erros simultaneamente de forma assíncrona.

### 5. Limitação de Eventos na Transição de Componentes de Texto (`JTextField` vs `JTextArea`)
* **Problema:** O compilador do Java 25 barrava o build com o erro `cannot find symbol: method addActionListener(...)` após a modificação da área de entrada do chat para suportar múltiplas linhas.
* **Causa:** Para possibilitar o envio de mensagens extensas, o campo de digitação foi migrado de um `JTextField` (linha única) para um `JTextArea`. Contudo, o Swing não disponibiliza o método `addActionListener` para componentes do tipo `JTextArea`, gerando a quebra estática de assinatura.
* **Solução:** Removeu-se o listener linear direto e implementou-se um mapeamento desacoplado de entradas de teclado (*Key Bindings*) através de `InputMap` e `ActionMap`. Foi configurada a combinação de teclas **`Ctrl + Enter`**, permitindo que o estudante quebre linhas livremente com a tecla `Enter` comum e envie o bloco completo de dados à inteligência artificial de forma assíncrona.

---

## 🚀 Como Executar o Projeto no Linux Lite 8

1. Certifique-se de que a sua credencial do Google AI Studio está ativa exportando a variável de ambiente:
   ```bash
   export GEMINI_API_KEY="SuaChaveRealDoGeminiAqui"
   source ~/.bashrc
   ```
2. Limpe as compilações anteriores, efetue o download seguro das dependências oficiais e inicialize a IDE Gráfica Maximizada:
   ```bash
   mvn clean compile exec:java
   ```
