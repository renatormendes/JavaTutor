package com.tutor.ai.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Mensagem implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String remetente;
    private String mensagem;
    private String dataHora;
    private String arquivoContexto;

    // Construtor completo para o banco
    public Mensagem(Integer id, String remetente, String mensagem, String dataHora, String arquivoContexto) {
        this.id = id;
        this.remetente = remetente;
        this.mensagem = mensagem;
        this.dataHora = dataHora;
        this.arquivoContexto = arquivoContexto;
    }

    // Construtor rápido para novas mensagens
    public Mensagem(String remetente, String mensagem, String arquivoContexto) {
        this.remetente = remetente;
        this.mensagem = mensagem;
        this.dataHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.arquivoContexto = arquivoContexto;
    }

    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getRemetente() { return remetente; }
    public String getMensagem() { return mensagem; }
    public String getDataHora() { return dataHora; }
    public String getArquivoContexto() { return arquivoContexto; }

    @Override
    public String toString() {
        return "[" + dataHora + "] " + remetente + ": " + mensagem;
    }
}
