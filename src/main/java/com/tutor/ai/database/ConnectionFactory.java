package com.tutor.ai.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class ConnectionFactory {
	
	private static final String URL = "jdbc:sqlite:tutor_ia.db";

	static {

		// Inicializa o banco de dados e cria a tabela 
		// automaticamente se não existir
		try (Connection conn = getConnection();
	         Statement stmt = conn.createStatement()) {

			String sql = """
			       CREATE TABLE IF NOT EXISTS historico_chat (
			       	    id INTEGER PRIMARY KEY AUTOINCREMENT,
			       	    remetente TEXT NOT NULL,
			       	    mensagem TEXT NOT NULL,
			       	    data_hora TEXT NOT NULL,
			       	    arquivo_contexto TEXT
			       );
			       """;
			stmt.execute(sql);
			
		} catch (Exception e) {

			System.err.println("Erro ao iniciar o banco de dados SQLite: " + e.getMessage());
		
		}

	}

	public static Connection getConnection() throws SQLException {

		return DriverManager.getConnection(URL);

	}

	public static void fecharConexao(Connection conn) {

		if (conn != null) {

			try {

				conn.close();

			} catch (SQLException e) {

				System.err.println("Erro ao fechar a conexão com o banco: " + e.getMessage());

			}

		}
	}

	public static void fecharStatement(Statement stmt) {

		if (stmt != null) {

			try {

				stmt.close();

			} catch (SQLException e) {

				System.err.println("Erro ao fechar o Statement: " + e.getMessage());

			}

		}

	}
	
}

