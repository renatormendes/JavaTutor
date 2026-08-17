package com.tutor.ai.config;

import com.google.genai.Client;

public class AgentConfig {

    private static Client client;

    public static synchronized Client getChatClient() {
        if (client == null) {
            String apiKey = System.getenv("GEMINI_API_KEY");
            if (apiKey == null || apiKey.isBlank()) {
                throw new IllegalStateException("ERRO: GEMINI_API_KEY não configurada no terminal!");
            }
            
            try {
                // Inicializa o cliente padrão do Google GenAI utilizando a sua chave de API
                client = Client.builder().apiKey(apiKey).build();
            } catch (Exception e) {
                throw new RuntimeException("Erro ao instanciar o SDK do Google GenAI: " + e.getMessage(), e);
            }
        }
        return client;
    }

    public static String enviarPrompt(String instrucaoSistema, String promptUsuario) {
        try {
            Client genAiClient = getChatClient();
            String promptCompleto = instrucaoSistema + "\n\nUsuário pergunta:\n" + promptUsuario;
            
            // O retorno da chamada do SDK oficial entrega o texto diretamente por inferência
            var response = genAiClient.models.generateContent(
                "gemini-3.5-flash", 
                promptCompleto, 
                null
            );
            return response.text();
        } catch (Exception e) {
            return "Tutor IA (Erro de Processamento): " + e.getMessage();
        }
    }
}
