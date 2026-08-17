package com.tutor.ai.service;

import com.tutor.ai.database.ChatDAO;
import com.tutor.ai.model.Mensagem;
import java.util.List;

public class ChatFacade {

    private final ChatDAO dao = new ChatDAO();

    public void processarNovaMensagem(String remetente, String texto, String arquivoAtivo) {
        Mensagem msg = new Mensagem(remetente, texto, arquivoAtivo);
        
        ArquivoBinarioService.adicionarMensagem(msg);
        
        new Thread(this::sincronizarBufferComBanco).start();
    }

    private synchronized void sincronizarBufferComBanco() {
        List<Mensagem> mensagensPendentes = ArquivoBinarioService.lerMensagens();
        
        if (!mensagensPendentes.isEmpty()) {
            for (Mensagem msg : mensagensPendentes) {
                boolean sucesso = dao.salvar(msg);
                if (!sucesso) return; 
            }
            ArquivoBinarioService.limparArquivo();
        }
    }

    public List<Mensagem> obterHistoricoCompleto() {
        return dao.listarTodas();
    }
}
