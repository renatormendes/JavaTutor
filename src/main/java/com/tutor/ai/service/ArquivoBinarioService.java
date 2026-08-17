package com.tutor.ai.service;

import com.tutor.ai.model.Mensagem;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ArquivoBinarioService {

    private static final String NOME_ARQUIVO = "buffer_chat.dat";

    public static synchronized void salvarMensagens(List<Mensagem> mensagens) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(NOME_ARQUIVO))) {
            oos.writeObject(mensagens);
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados no arquivo binário: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static synchronized List<Mensagem> lerMensagens() {
        File arquivo = new File(NOME_ARQUIVO);
        if (!arquivo.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            return (List<Mensagem>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao ler dados do arquivo binário: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static void adicionarMensagem(Mensagem msg) {
        List<Mensagem> atuais = lerMensagens();
        atuais.add(msg);
        salvarMensagens(atuais);
    }

    public static void limparArquivo() {
        File arquivo = new File(NOME_ARQUIVO);
        if (arquivo.exists()) {
            arquivo.delete();
        }
    }
}
