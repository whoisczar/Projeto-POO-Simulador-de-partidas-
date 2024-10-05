package DAO;

import MODEL.Jogador;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JogadorDAO {

    private Connection connection;

    public JogadorDAO(Connection connection) {
        this.connection = connection;
    }

    // Criação de um novo jogador no banco de dados
    public void create(Jogador jogador) throws SQLException {
        String sql = "INSERT INTO jogadores (nome, idade, posicao, nacionalidade, altura, time_nome) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, jogador.getNome());
            stmt.setInt(2, jogador.getIdade());
            stmt.setString(3, jogador.getPosicao());
            stmt.setString(4, jogador.getNacionalidade());
            stmt.setDouble(5, jogador.getAltura());
            stmt.setString(6, jogador.getTimeNome());
            stmt.setInt(7, jogador.getPontos());
            stmt.executeUpdate();
        }
    }

    // Atualização de um jogador existente
    public void update(Jogador jogador) throws SQLException {
        String sql = "UPDATE jogadores SET nome = ?, idade = ?, posicao = ?, nacionalidade = ?, altura = ?, time_nome = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, jogador.getNome());
            stmt.setInt(2, jogador.getIdade());
            stmt.setString(3, jogador.getPosicao());
            stmt.setString(4, jogador.getNacionalidade());
            stmt.setDouble(5, jogador.getAltura());
            stmt.setString(6, jogador.getTimeNome());
            stmt.setInt(7, jogador.getId());
            stmt.executeUpdate();
        }
    }

    // Deleção de um jogador pelo ID
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM jogadores WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Leitura de um jogador pelo ID
    public Jogador readById(int id) throws SQLException {
        String sql = "SELECT * FROM jogadores WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Jogador jogador = new Jogador(1, "Nome do Jogador", 1.8, "Atacante", 25, "Brasileiro", "Time Exemplo");
                    jogador.setId(rs.getInt("id"));
                    jogador.setNome(rs.getString("nome"));
                    jogador.setIdade(rs.getInt("idade"));
                    jogador.setPosicao(rs.getString("posicao"));
                    jogador.setNacionalidade(rs.getString("nacionalidade"));
                    jogador.setAltura(rs.getDouble("altura"));
                    jogador.setTimeNome(rs.getString("time_nome")); // Mudança para "time_nome"
                    return jogador;
                }
            }
        }
        return null; // Jogador não encontrado
    }

 // Leitura de todos os jogadores do banco de dados
public List<Jogador> read() throws SQLException {
    List<Jogador> jogadores = new ArrayList<>();
    String sql = "SELECT * FROM jogadores ORDER BY pontos DESC";
    try (PreparedStatement stmt = connection.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
            Jogador jogador = new Jogador(1, "Nome do Jogador", 1.8, "Atacante", 25, "Brasileiro", "Time Exemplo");
            jogador.setId(rs.getInt("id"));
            jogador.setNome(rs.getString("nome"));
            jogador.setIdade(rs.getInt("idade"));
            jogador.setPosicao(rs.getString("posicao"));
            jogador.setNacionalidade(rs.getString("nacionalidade"));
            jogador.setAltura(rs.getDouble("altura"));
            jogador.setTimeNome(rs.getString("time_nome"));
            jogador.setPontos(rs.getInt("pontos"));
            jogadores.add(jogador);
        }
    }
    return jogadores;
}


    public void atualizarPontos(int jogadorId, int pontos) throws SQLException {
        String sql = "UPDATE jogadores SET pontos = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, pontos);
            stmt.setInt(2, jogadorId);
            stmt.executeUpdate();
        }
    }
    // Leitura de todos os jogadores de um determinado time
public List<Jogador> getJogadoresByTimeId(int timeId) throws SQLException {
    List<Jogador> jogadores = new ArrayList<>();
    String sql = "SELECT * FROM jogadores WHERE time_nome = (SELECT nome FROM times WHERE id = ?)";
    
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setInt(1, timeId); // Define o ID do time
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Jogador jogador = new Jogador(1, "Nome do Jogador", 1.8, "Atacante", 25, "Brasileiro", "Time Exemplo");
                jogador.setId(rs.getInt("id"));
                jogador.setNome(rs.getString("nome"));
                jogador.setIdade(rs.getInt("idade"));
                jogador.setPosicao(rs.getString("posicao"));
                jogador.setNacionalidade(rs.getString("nacionalidade"));
                jogador.setAltura(rs.getDouble("altura"));
                jogador.setTimeNome(rs.getString("time_nome"));
                jogador.setPontos(rs.getInt("pontos"));
                jogadores.add(jogador);
            }
        }
    }
    return jogadores;
}

    
}
