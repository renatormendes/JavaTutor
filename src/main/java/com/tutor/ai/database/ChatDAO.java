package com.tutor.ai.database;

import com.tutor.ai.model.Mensagem; // Verifique se está exatamente assim, sem o prefixo "main."
//import java.sql.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChatDAO {

    public boolean salvar(Mensagem msg) {
        String sql = "INSERT INTO historico_chat (remetente, mensagem, data_hora, arquivo_contexto) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, msg.getRemetente());
            stmt.setString(2, msg.getMensagem());
            stmt.setString(3, msg.getDataHora());
            stmt.setString(4, msg.getArquivoContexto());
            
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao salvar mensagem no SQLite: " + e.getMessage());
            return false;
        }
    }

    public List<Mensagem> listarTodas() {
        List<Mensagem> lista = new ArrayList<>();
        String sql = "SELECT * FROM historico_chat ORDER BY id ASC";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Mensagem(
                    rs.getInt("id"),
                    rs.getString("remetente"),
                    rs.getString("mensagem"),
                    rs.getString("data_hora"),
                    rs.getString("arquivo_contexto")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar mensagens do SQLite: " + e.getMessage());
        }
        return lista;
    }

    public boolean atualizar(Mensagem msg) {
        String sql = "UPDATE historico_chat SET mensagem = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, msg.getMensagem());
            stmt.setInt(2, msg.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar mensagem no SQLite: " + e.getMessage());
            return false;
        }
    }

    public boolean deletar(int id) {
        String sql = "DELETE FROM historico_chat WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao deletar do SQLite: " + e.getMessage());
            return false;
        }
    }
}
