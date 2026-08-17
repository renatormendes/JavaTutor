import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ExercicioDois {
	
	public static void main(String[] args) {

		// 1. Defina o caminho do seu banco de dados
		String url = "jdbc:sqlite:meubancoteste.db";

		// 2. Abra a conexão dentro do 'try' para que ela feche sozinha depois
		try(Connection conn = DriverManager.getConnection(url)) {

			if(conn != null) {

				System.out.println("Conexão com o SQLite estabelecida com sucesso!");

			}

		} catch(SQLException e) {

			System.out.println("Ops, ocorreu um erro de conexão: " + e.getMessage());

		}

		System.out.println("\nÉ isso aí, Pe-pe-soall!!!");

	}
        
}
