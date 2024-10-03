package CONEXAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBD {

    // URL de conexão, usuário e senha do banco de dados (ajuste conforme necessário)
    private static final String URL = "jdbc:mysql://localhost:3306/jogadorestimes";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    // Instância única da conexão
    private static Connection connection = null;

    // Construtor privado para evitar instanciamento
    private ConexaoBD() { }

    // Método para obter a conexão com o banco de dados
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Carrega o driver do MySQL (dependendo do banco, o driver pode mudar)
                Class.forName("com.mysql.cj.jdbc.Driver");
                // Estabelece a conexão
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Conexão estabelecida com sucesso!");
            } catch (ClassNotFoundException e) {
                throw new SQLException("Erro ao carregar o driver do banco de dados", e);
            }
        }
        return connection;
    }

    // Método para fechar a conexão
    public static void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("Conexão fechada com sucesso!");
        }
    }
}
