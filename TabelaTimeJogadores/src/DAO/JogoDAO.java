package DAO;

import MODEL.Jogo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JogoDAO {
    private Connection connection;

    public JogoDAO(Connection connection) {
        this.connection = connection;
    }

    public void create(Jogo jogo) throws SQLException {
        String sql = "INSERT INTO jogos (time_a_id, time_b_id, data, resultado) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, jogo.getTimeAId()); // Usar o ID do time A
            stmt.setInt(2, jogo.getTimeBId()); // Usar o ID do time B
            stmt.setString(3, jogo.getData());
            stmt.setString(4, jogo.getResultado());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    jogo.setId(rs.getInt(1)); // Obtém o ID gerado
                }
            }
        }
    }

    public Jogo read(int id) throws SQLException {
        String sql = "SELECT * FROM jogos WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Jogo jogo = new Jogo();
                    jogo.setId(rs.getInt("id"));
                    jogo.setTimeAId(rs.getInt("time_a_id")); // Usar o ID do time A
                    jogo.setTimeBId(rs.getInt("time_b_id")); // Usar o ID do time B
                    jogo.setData(rs.getString("data"));
                    jogo.setResultado(rs.getString("resultado"));
                    return jogo;
                }
            }
        }
        return null; // Jogo não encontrado
    }

    public List<Jogo> read() throws SQLException {
        List<Jogo> jogos = new ArrayList<>();
        String sql = "SELECT * FROM jogos";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Jogo jogo = new Jogo();
                jogo.setId(rs.getInt("id"));
                jogo.setTimeAId(rs.getInt("time_a_id")); // Usar o ID do time A
                jogo.setTimeBId(rs.getInt("time_b_id")); // Usar o ID do time B
                jogo.setData(rs.getString("data"));
                jogo.setResultado(rs.getString("resultado"));
                jogos.add(jogo);
            }
        }
        return jogos;
    }

    public void update(Jogo jogo) throws SQLException {
        String sql = "UPDATE jogos SET time_a_id = ?, time_b_id = ?, data = ?, resultado = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, jogo.getTimeAId()); // Usar o ID do time A
            stmt.setInt(2, jogo.getTimeBId()); // Usar o ID do time B
            stmt.setString(3, jogo.getData());
            stmt.setString(4, jogo.getResultado());
            stmt.setInt(5, jogo.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM jogos WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
