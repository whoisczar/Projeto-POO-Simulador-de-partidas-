package DAO;

import MODEL.Time;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TimeDAO {

    private final Connection connection;

    public TimeDAO(Connection connection) {
        this.connection = connection;
    }

    // Criação de um novo time no banco de dados
    public void create(Time time) throws SQLException {
        String sql = "INSERT INTO times (nome, vitorias) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, time.getNome());
            stmt.setInt(2, time.getVitorias());
            stmt.executeUpdate();
        }
    }

    // Atualização de um time existente
    public void update(Time time) throws SQLException {
        String sql = "UPDATE times SET nome = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, time.getNome());
            stmt.setInt(2, time.getId());
            stmt.executeUpdate();
        }
    }

    // Deleção de um time pelo ID
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM times WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Leitura de um time pelo ID
    public Time readById(int id) throws SQLException {
        String sql = "SELECT * FROM times WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Time time = new Time();
                    time.setId(rs.getInt("id"));
                    time.setNome(rs.getString("nome"));
                    time.setVitorias(rs.getInt("vitorias"));
                    return time; // Retorna o time encontrado
                }
            }
        }
        return null; // Retorna null se o time não for encontrado
    }

    // Leitura de todos os times do banco de dados
    public List<Time> read() throws SQLException {
        List<Time> times = new ArrayList<>();
        String sql = "SELECT * FROM times ORDER BY vitorias DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Time time = new Time();
                time.setId(rs.getInt("id"));
                time.setNome(rs.getString("nome"));
                time.setVitorias(rs.getInt("vitorias"));
                times.add(time); // Adiciona cada time encontrado à lista
            }
        }
        return times; // Retorna a lista de times
    }

    // Novo método para obter o ID de um time pelo nome
    public int getTimeIdByName(String nome) throws SQLException {
        String sql = "SELECT id FROM times WHERE nome = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nome);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id"); // Retorna o ID do time encontrado
                }
            }
        }
        throw new SQLException("Time não encontrado: " + nome); // Lança exceção se não encontrado
    }

    public Time getTimeById(int id) throws SQLException {
        String query = "SELECT * FROM times WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Time time = new Time();
                    time.setId(rs.getInt("id"));
                    time.setNome(rs.getString("nome"));
                    time.setVitorias(rs.getInt("vitorias"));
                    return time;
                }
            }
        }
        throw new SQLException("Time não encontrado com ID: " + id);
    }

    public boolean existsByName(String nome) throws SQLException {
        String query = "SELECT COUNT(*) FROM times WHERE nome = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Retorna true se houver um time com esse nome
            }
        }
        return false;
    }

    // Método para adicionar vitórias a um time
    public void adicionarVitoria(int timeId) throws SQLException {
        String sql = "UPDATE times SET vitorias = vitorias + 1 WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, timeId);
            stmt.executeUpdate();
        }
    }

}
