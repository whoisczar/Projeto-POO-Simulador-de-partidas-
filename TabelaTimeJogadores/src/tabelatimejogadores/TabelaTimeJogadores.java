package tabelatimejogadores;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import MODEL.Time;      // Ajuste o caminho conforme necessário
import MODEL.Jogador;   // Ajuste o caminho conforme necessário
import MODEL.Jogo;      // Ajuste o caminho conforme necessário
import DAO.TimeDAO;
import DAO.JogadorDAO;
import DAO.JogoDAO;  // Certifique-se de que você tem uma classe JogoDAO
import java.awt.GridLayout;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class TabelaTimeJogadores extends JFrame {

    private static final String URL = "jdbc:mysql://localhost:3306/jogadorestimes";
    private static final String USER = "root"; // Altere conforme necessário
    private static final String PASSWORD = "root"; // Altere conforme necessário

    private JTabbedPane tabbedPane;
    private JPanel panelTimes;
    private JPanel panelJogadores;
    private JPanel panelJogos;

    private JTable tableTimes;
    private DefaultTableModel tableModelTimes;

    private JTable tableJogadores;
    private DefaultTableModel tableModelJogadores;

    private JTable tableJogos;
    private DefaultTableModel tableModelJogos;

    private Connection connection;
    private TimeDAO timeDAO;
    private JogadorDAO jogadorDAO;
    private JogoDAO jogoDAO;

    public TabelaTimeJogadores() {
        setTitle("Times e Jogadores");
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initializeDatabase();
        initializeUI();
    }

    private void initializeDatabase() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            timeDAO = new TimeDAO(connection);
            jogadorDAO = new JogadorDAO(connection);
            jogoDAO = new JogoDAO(connection); // Inicializa o JogoDAO
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void initializeUI() {
        tabbedPane = new JTabbedPane();

        // Painel de Times
        panelTimes = new JPanel();
        String[] columnNames = {"ID", "Nome", "Cidade", "Técnico", "Vitorias"};
        tableModelTimes = new DefaultTableModel(columnNames, 0);
        tableTimes = new JTable(tableModelTimes);
        JScrollPane scrollPaneTimes = new JScrollPane(tableTimes);

        // Ajustar larguras das colunas da tabela de Times
        tableTimes.getColumnModel().getColumn(0).setPreferredWidth(25);  // ID
        tableTimes.getColumnModel().getColumn(1).setPreferredWidth(150); // Nome
        tableTimes.getColumnModel().getColumn(2).setPreferredWidth(150); // Cidade
        tableTimes.getColumnModel().getColumn(3).setPreferredWidth(100); // Técnico
        tableTimes.getColumnModel().getColumn(4).setPreferredWidth(100);  // Vitorias

        JButton btnAddTime = new JButton("Adicionar Time");
        JButton btnEditTime = new JButton("Editar Time");
        JButton btnDeleteTime = new JButton("Deletar Time");

        btnAddTime.addActionListener(this::addTime);
        btnEditTime.addActionListener(this::editTime);
        btnDeleteTime.addActionListener(this::deleteTime);

        panelTimes.add(btnAddTime);
        panelTimes.add(btnEditTime);
        panelTimes.add(btnDeleteTime);
        panelTimes.add(scrollPaneTimes);

        tabbedPane.addTab("Times", panelTimes);

        // Painel de Jogadores
        panelJogadores = new JPanel();
        String[] jogadorColumnNames = {"ID", "Nome", "Altura", "Origem", "Time", "Gols"};
        tableModelJogadores = new DefaultTableModel(jogadorColumnNames, 0);
        tableJogadores = new JTable(tableModelJogadores);
        JScrollPane scrollPaneJogadores = new JScrollPane(tableJogadores);

        // Ajustar larguras das colunas da tabela de Jogadores
        tableJogadores.getColumnModel().getColumn(0).setPreferredWidth(25);  // ID
        tableJogadores.getColumnModel().getColumn(1).setPreferredWidth(150); // Nome
        tableJogadores.getColumnModel().getColumn(2).setPreferredWidth(70);  // Altura
        tableJogadores.getColumnModel().getColumn(3).setPreferredWidth(100); // Origem
        tableJogadores.getColumnModel().getColumn(4).setPreferredWidth(150); // Time
        tableJogadores.getColumnModel().getColumn(5).setPreferredWidth(50);  // Gols

        JButton btnAddJogador = new JButton("Adicionar Jogador");
        JButton btnEditJogador = new JButton("Editar Jogador");
        JButton btnDeleteJogador = new JButton("Deletar Jogador");

        btnAddJogador.addActionListener(this::addJogador);
        btnEditJogador.addActionListener(this::editJogador);
        btnDeleteJogador.addActionListener(this::deleteJogador);

        panelJogadores.add(btnAddJogador);
        panelJogadores.add(btnEditJogador);
        panelJogadores.add(btnDeleteJogador);
        panelJogadores.add(scrollPaneJogadores);

        tabbedPane.addTab("Jogadores", panelJogadores);

        // Painel de Jogos
        panelJogos = new JPanel();
        String[] jogoColumnNames = {"ID", "Time A", "Time B", "Data", "Resultado"};
        tableModelJogos = new DefaultTableModel(jogoColumnNames, 0);
        tableJogos = new JTable(tableModelJogos);
        JScrollPane scrollPaneJogos = new JScrollPane(tableJogos);

        // Ajustar larguras das colunas da tabela de Jogos
        tableJogos.getColumnModel().getColumn(0).setPreferredWidth(25);  // ID
        tableJogos.getColumnModel().getColumn(1).setPreferredWidth(150); // Time A
        tableJogos.getColumnModel().getColumn(2).setPreferredWidth(150); // Time B
        tableJogos.getColumnModel().getColumn(3).setPreferredWidth(80); // Data
        tableJogos.getColumnModel().getColumn(4).setPreferredWidth(110); // Resultado

        JButton btnAddJogo = new JButton("Adicionar Jogo");
        JButton btnEditJogo = new JButton("Editar Jogo");
        JButton btnDeleteJogo = new JButton("Deletar Jogo");

        btnAddJogo.addActionListener(this::addJogo);
        btnDeleteJogo.addActionListener(this::deleteJogo);

        panelJogos.add(btnAddJogo);
        panelJogos.add(btnDeleteJogo);
        panelJogos.add(scrollPaneJogos);

        tabbedPane.addTab("Jogos", panelJogos);

        add(tabbedPane);

        loadTimes();      // Carrega os times automaticamente
        loadJogadores();  // Carrega os jogadores automaticamente
        loadJogos();      // Carrega os jogos automaticamente
    }

    private void loadTimes() {
        try {
            List<Time> times = timeDAO.read();
            tableModelTimes.setRowCount(0); // Limpa a tabela
            for (Time time : times) {
                Object[] row = {
                    time.getId(),
                    time.getNome(),
                    time.getCidade(),
                    time.getTecnico(),
                    time.getVitorias()
                };
                System.out.println(time.getVitorias());
                tableModelTimes.addRow(row); // Adiciona a linha na tabela
            }
            System.out.println("Times carregados com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar times.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    void loadJogadores() {
        try {
            List<Jogador> jogadores = jogadorDAO.read();
            tableModelJogadores.setRowCount(0); // Limpa a tabela
            for (Jogador jogador : jogadores) {
                Object[] row = {
                    jogador.getId(),
                    jogador.getNome(),
                    jogador.getAltura(),
                    jogador.getNacionalidade(),
                    jogador.getTimeNome(),
                    jogador.getGols()
                };
                tableModelJogadores.addRow(row); // Adiciona a linha na tabela
            }
            System.out.println("Jogadores carregados com sucesso!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar jogadores.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadJogos() {
        try {
            List<Jogo> jogos = jogoDAO.read();
            tableModelJogos.setRowCount(0); // Limpa a tabela

            // Instância do TimeDAO para buscar os nomes dos times
            TimeDAO timeDAO = new TimeDAO(connection);

            for (Jogo jogo : jogos) {
                // Obtem os nomes dos times com base nos IDs
                String nomeTimeA = timeDAO.readById(jogo.getTimeAId()).getNome();
                String nomeTimeB = timeDAO.readById(jogo.getTimeBId()).getNome();

                Object[] row = {
                    jogo.getId(),
                    nomeTimeA, // Usa o nome do time A
                    nomeTimeB, // Usa o nome do time B
                    jogo.getData(),
                    jogo.getResultado()
                };
                tableModelJogos.addRow(row); // Adiciona a linha na tabela
            }
            System.out.println("Jogos carregados com sucesso!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar jogos.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar os nomes dos times.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addTime(ActionEvent e) {
        // Cria o modal
        JDialog dialog = new JDialog(this, "Adicionar Time", true);
        dialog.setLayout(new GridLayout(6, 2));

        // Campos de texto para as informações do novo time
        JTextField nomeField = new JTextField();
        JTextField cidadeField = new JTextField();
        JTextField estadoField = new JTextField();
        JTextField anoFundacaoField = new JTextField();
        JTextField tecnicoField = new JTextField();

        // Adiciona os campos ao modal
        dialog.add(new JLabel("Nome:"));
        dialog.add(nomeField);
        dialog.add(new JLabel("Cidade:"));
        dialog.add(cidadeField);
        dialog.add(new JLabel("Estado:"));
        dialog.add(estadoField);
        dialog.add(new JLabel("Ano de Fundação:"));
        dialog.add(anoFundacaoField);
        dialog.add(new JLabel("Técnico:"));
        dialog.add(tecnicoField);

        // Botões para adicionar ou cancelar
        JButton addButton = new JButton("Adicionar");
        JButton cancelButton = new JButton("Cancelar");

        dialog.add(addButton);
        dialog.add(cancelButton);

        // Ação do botão "Adicionar"
        addButton.addActionListener(event -> {
            try {
                String nome = nomeField.getText();
                String cidade = cidadeField.getText();
                String estado = estadoField.getText();
                int anoFundacao = Integer.parseInt(anoFundacaoField.getText());
                String tecnico = tecnicoField.getText();

                if (!nome.isEmpty() && !cidade.isEmpty() && !estado.isEmpty() && !tecnico.isEmpty()) {
                    // Cria um novo objeto Time e preenche com os dados inseridos
                    Time time = new Time();
                    time.setNome(nome);
                    time.setCidade(cidade);
                    time.setEstado(estado);
                    time.setAnoFundacao(anoFundacao);
                    time.setTecnico(tecnico);

                    // Salva no banco de dados
                    timeDAO.create(time);

                    // Recarrega a tabela de times
                    loadTimes();

                    // Fecha o modal
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Preencha todos os campos.", "Erro", JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Ano de fundação inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog, "Erro ao adicionar time.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Ação do botão "Cancelar"
        cancelButton.addActionListener(event -> dialog.dispose());

        // Configurações do modal
        dialog.pack();
        dialog.setLocationRelativeTo(this); // Centraliza o modal
        dialog.setVisible(true); // Exibe o modal
    }

    private void editJogador(ActionEvent e) {
        int selectedRow = tableJogadores.getSelectedRow();
        if (selectedRow != -1) { // Verifica se alguma linha está selecionada
            int id = (int) tableModelJogadores.getValueAt(selectedRow, 0); // Obtém o ID do jogador selecionado
            try {
                Jogador jogador = jogadorDAO.readById(id);
                if (jogador != null) {
                    String nome = JOptionPane.showInputDialog(this, "Nome do Jogador:", jogador.getNome());
                    String alturaStr = JOptionPane.showInputDialog(this, "Altura do Jogador:", jogador.getAltura());

                    // Carrega os times
                    loadTimes(); // Certifique-se de que a tabela de times está carregada

                    // Obtém a lista de nomes de times do modelo da tabela
                    List<String> timesNomes = new ArrayList<>();
                    for (int i = 0; i < tableModelTimes.getRowCount(); i++) {
                        timesNomes.add((String) tableModelTimes.getValueAt(i, 1)); // Coluna com o nome do time
                    }

                    // Cria o dialogo para seleção de time
                    String timeNome = (String) JOptionPane.showInputDialog(
                            this,
                            "Nome do Time:",
                            "Selecione o Time",
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            timesNomes.toArray(),
                            jogador.getTimeNome() // Preenche o campo com o time atual
                    );

                    if (nome != null && alturaStr != null && timeNome != null) {
                        double altura = Double.parseDouble(alturaStr);
                        jogador.setNome(nome);
                        jogador.setAltura(altura);
                        jogador.setTimeNome(timeNome);
                        jogadorDAO.update(jogador);
                        loadJogadores(); // Atualiza a tabela de jogadores
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao editar jogador.", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Altura inválida.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um jogador para editar.", "Erro", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void editTime(ActionEvent e) {
        int selectedRow = tableTimes.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) tableModelTimes.getValueAt(selectedRow, 0); // Obtém o ID do time selecionado
            try {
                Time time = timeDAO.readById(id);
                if (time != null) {
                    // Cria o modal
                    JDialog dialog = new JDialog(this, "Editar Time", true);
                    dialog.setLayout(new GridLayout(6, 2));

                    // Campos de texto para as informações do time
                    JTextField nomeField = new JTextField(time.getNome());
                    JTextField cidadeField = new JTextField(time.getCidade());
                    JTextField estadoField = new JTextField(time.getEstado());
                    JTextField anoFundacaoField = new JTextField(String.valueOf(time.getAnoFundacao()));
                    JTextField tecnicoField = new JTextField(time.getTecnico());

                    // Adiciona os campos ao modal
                    dialog.add(new JLabel("Nome:"));
                    dialog.add(nomeField);
                    dialog.add(new JLabel("Cidade:"));
                    dialog.add(cidadeField);
                    dialog.add(new JLabel("Estado:"));
                    dialog.add(estadoField);
                    dialog.add(new JLabel("Ano de Fundação:"));
                    dialog.add(anoFundacaoField);
                    dialog.add(new JLabel("Técnico:"));
                    dialog.add(tecnicoField);

                    // Botões para salvar ou cancelar
                    JButton saveButton = new JButton("Salvar");
                    JButton cancelButton = new JButton("Cancelar");

                    dialog.add(saveButton);
                    dialog.add(cancelButton);

                    // Ação do botão "Salvar"
                    saveButton.addActionListener(event -> {
                        try {
                            String nome = nomeField.getText();
                            String cidade = cidadeField.getText();
                            String estado = estadoField.getText();
                            int anoFundacao = Integer.parseInt(anoFundacaoField.getText());
                            String tecnico = tecnicoField.getText();

                            // Atualiza o objeto time
                            time.setNome(nome);
                            time.setCidade(cidade);
                            time.setEstado(estado);
                            time.setAnoFundacao(anoFundacao);
                            time.setTecnico(tecnico);

                            // Salva as mudanças no banco de dados
                            timeDAO.update(time);

                            // Atualiza a tabela
                            loadTimes();

                            // Fecha o modal
                            dialog.dispose();
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(dialog, "Ano de fundação inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(dialog, "Erro ao salvar alterações.", "Erro", JOptionPane.ERROR_MESSAGE);
                        }
                    });

                    // Ação do botão "Cancelar"
                    cancelButton.addActionListener(event -> dialog.dispose());

                    // Configurações do modal
                    dialog.pack();
                    dialog.setLocationRelativeTo(this); // Centraliza o modal
                    dialog.setVisible(true); // Exibe o modal
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao editar time.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um time para editar.", "Erro", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteTime(ActionEvent e) {
        int selectedRow = tableTimes.getSelectedRow(); // Verifica a linha selecionada na tabela
        if (selectedRow != -1) { // Verifica se alguma linha está selecionada
            try {
                // Obtém o ID da coluna 0 da linha selecionada, assumindo que o ID está na primeira coluna
                int id = (Integer) tableModelTimes.getValueAt(selectedRow, 0); // Obtenha como Integer
                timeDAO.delete(id); // Deleta o time com base no ID
                loadTimes(); // Atualiza a tabela de times
                JOptionPane.showMessageDialog(this, "Time deletado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (ClassCastException ex) {
                JOptionPane.showMessageDialog(this, "Tipo de dado inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao deletar time.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um time para deletar.", "Erro", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void addJogador(ActionEvent e) {
        // Cria uma nova instância do JFrame para adicionar jogador
        criarJogadorFrame criarJogadorFrame = new criarJogadorFrame();

        // Configura a janela para que ela apareça centralizada em relação à janela pai
        criarJogadorFrame.setLocationRelativeTo(this); // 'this' refere-se à janela atual (TabelaTimeJogadores)

        // Adiciona um WindowListener para recarregar a tabela quando a janela for fechada
        criarJogadorFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                loadJogadores(); // Atualiza a tabela de jogadores ao fechar a janela de adição
            }
        });

        // Torna o JFrame visível
        criarJogadorFrame.setVisible(true);
    }

    private void deleteJogador(ActionEvent e) {
        int selectedRow = tableJogadores.getSelectedRow(); // Obtém a linha selecionada
        if (selectedRow != -1) { // Verifica se uma linha está selecionada
            int jogadorId = (Integer) tableModelJogadores.getValueAt(selectedRow, 0); // Assume que a primeira coluna contém o ID
            int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja deletar o jogador com ID: " + jogadorId + "?", "Confirmar Deletar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    jogadorDAO.delete(jogadorId); // Chama o método delete do JogadorDAO
                    loadJogadores(); // Recarrega a lista de jogadores
                    JOptionPane.showMessageDialog(this, "Jogador deletado com sucesso!");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Erro ao deletar jogador.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um jogador para deletar.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
private void addJogo(ActionEvent e) {
    try {
        // Obter times
        TimeDAO timeDAO = new TimeDAO(connection);
        List<Time> times = timeDAO.read();
        if (times.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Não há times cadastrados.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Selecionar times
        JComboBox<String> comboTimeA = new JComboBox<>();
        JComboBox<String> comboTimeB = new JComboBox<>();
        times.forEach(time -> {
            comboTimeA.addItem(time.getNome());
            comboTimeB.addItem(time.getNome());
        });

        JPanel panel = new JPanel();
        panel.add(new JLabel("Time A:"));
        panel.add(comboTimeA);
        panel.add(new JLabel("Time B:"));
        panel.add(comboTimeB);

        if (JOptionPane.showConfirmDialog(this, panel, "Adicionar Jogo", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) != JOptionPane.OK_OPTION) {
            return; // Se o usuário cancelar, saia.
        }

        String timeANome = (String) comboTimeA.getSelectedItem();
        String timeBNome = (String) comboTimeB.getSelectedItem();

        if (timeANome.equals(timeBNome)) {
            JOptionPane.showMessageDialog(this, "Escolha dois times diferentes.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Simular pontuação dos times
        int scoreA = (int) (Math.random() * 6);
        int scoreB = (int) (Math.random() * 6);
        String ganhador = scoreA > scoreB ? timeANome : scoreA < scoreB ? timeBNome : "Empate";

        // Captura a data e hora atuais usando LocalDateTime
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = now.format(formatter); // Formata a data

        // Mostra o resultado
        String resultadoFinal = ganhador + " (" + Math.max(scoreA, scoreB) + " - " + Math.min(scoreA, scoreB) + ")";
        JOptionPane.showMessageDialog(this, resultadoFinal, "Resultado do Jogo", JOptionPane.INFORMATION_MESSAGE);
        
        // Define os IDs dos times
        int timeAId = getTimeIdByName(timeANome);
        int timeBId = getTimeIdByName(timeBNome);

        // Adiciona vitória ao time vencedor, se houver
        if (!ganhador.equals("Empate")) {
            if (ganhador.equals(timeANome)) {
                timeDAO.adicionarVitoria(timeAId);
                simularGols(timeAId, timeBId, scoreA, scoreB); // Chama a distribuição de gols para o time A
            } else {
                timeDAO.adicionarVitoria(timeBId);
                simularGols(timeBId, timeAId, scoreB, scoreA); // Chama a distribuição de gols para o time B
            }
        }

        // Adiciona o jogo ao banco de dados
        Jogo jogo = new Jogo();
        jogo.setTimeAId(timeAId); // Define o ID do time A
        jogo.setTimeBId(timeBId); // Define o ID do time B
        jogo.setResultado(resultadoFinal);
        jogo.setData(formattedDate); // Define a data atual
        
        // Inserir o jogo no banco de dados
        JogoDAO jogoDAO = new JogoDAO(connection);
        jogoDAO.create(jogo);
        loadJogos(); // Recarrega a lista de jogos
        loadJogadores();

    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Erro ao adicionar jogo: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Erro inesperado: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    }
}


// Método auxiliar para obter o ID do time pelo nome
    private int getTimeIdByName(String timeNome) throws SQLException {
        TimeDAO timeDAO = new TimeDAO(connection);
        List<Time> times = timeDAO.read();

        System.out.println("Buscando ID para o time: " + timeNome); // Log do time que está sendo buscado

        for (Time time : times) {
            System.out.println("Comparando com: " + time.getNome()); // Log dos nomes encontrados
            if (time.getNome().equalsIgnoreCase(timeNome)) { // Usar equalsIgnoreCase para evitar problemas de capitalização
                System.out.println("Time encontrado: " + time.getNome() + " com ID: " + time.getId());
                return time.getId(); // Retorna o ID do time correspondente
            }
        }
        throw new SQLException("Time não encontrado: " + timeNome); // Lança exceção se o time não for encontrado
    }

   private void simularGols(int timeAId, int timeBId, int golsTimeA, int golsTimeB) throws SQLException {
    JogadorDAO jogadorDAO = new JogadorDAO(connection);
    
    // Obter jogadores de cada time
    List<Jogador> jogadoresTimeA = jogadorDAO.getJogadoresByTimeId(timeAId);
    List<Jogador> jogadoresTimeB = jogadorDAO.getJogadoresByTimeId(timeBId);
    
    // Identifica o time vencedor
    List<Jogador> jogadoresVencedores;
    if (golsTimeA > golsTimeB) {
        jogadoresVencedores = jogadoresTimeA;
    } else if (golsTimeB > golsTimeA) {
        jogadoresVencedores = jogadoresTimeB;
    } else {
        jogadoresVencedores = new ArrayList<>(); // Empate, não há vencedores
    }

    // Distribui gols entre os jogadores do time vencedor
    if (!jogadoresVencedores.isEmpty() && (golsTimeA > 0 || golsTimeB > 0)) {
        int golsVencedor = (golsTimeA > golsTimeB) ? golsTimeA : golsTimeB; // Gols do time vencedor
        distribuirGolsEntreJogadores(jogadoresVencedores, golsVencedor);
    }
}

private void distribuirGolsEntreJogadores(List<Jogador> jogadores, int totalGols) throws SQLException {
    JogadorDAO jogadorDAO = new JogadorDAO(connection);

    while (totalGols > 0) {
        // Seleciona aleatoriamente um jogador da lista para receber um gol
        int jogadorIndex = (int) (Math.random() * jogadores.size());
        Jogador jogador = jogadores.get(jogadorIndex);

        // Incrementa os gols do jogador e atualiza no banco de dados
        jogador.setGols(jogador.getGols() + 1);
        jogadorDAO.updateGols(jogador.getId(), jogador.getGols());

        // Decrementa o total de gols a serem distribuídos
        totalGols--;
    }
}


    private void deleteJogo(ActionEvent e) {
        int selectedRow = tableJogos.getSelectedRow();
        if (selectedRow != -1) {
            int jogoId = (Integer) tableModelJogos.getValueAt(selectedRow, 0); // Assume que a primeira coluna contém o ID do jogo
            int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja deletar o jogo com ID: " + jogoId + "?", "Confirmar Deletar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    jogoDAO.delete(jogoId);
                    loadJogos(); // Recarrega a lista de jogos
                    JOptionPane.showMessageDialog(this, "Jogo deletado com sucesso!");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Erro ao deletar jogo.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um jogo para deletar.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TabelaTimeJogadores frame = new TabelaTimeJogadores();
            frame.setVisible(true);
        });
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }

            }
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(TabelaTimeJogadores.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                Logger.getLogger(TabelaTimeJogadores.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(TabelaTimeJogadores.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedLookAndFeelException ex) {
                Logger.getLogger(TabelaTimeJogadores.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}