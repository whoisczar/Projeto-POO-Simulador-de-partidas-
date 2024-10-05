package com.mycompany.timejogadoresmaven;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import MODEL.Time;      
import MODEL.Jogador;  
import MODEL.Jogo;      
import DAO.TimeDAO;
import DAO.JogadorDAO;
import DAO.JogoDAO;  
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.swing.UIManager;

public class TimeJogadoresMaven extends JFrame {

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

    public TimeJogadoresMaven() {
        setTitle("Times e Jogadores");

        // Definir tamanho preferido e fixar a janela
        setPreferredSize(new Dimension(600, 600));
        setMaximumSize(new Dimension(600, 600));
        setMinimumSize(new Dimension(300, 300));

        // Tornar a janela não redimensionável
        setResizable(false);

        // Carrega o ícone
        ImageIcon icon = new ImageIcon("src\\main\\java\\com\\mycompany\\Imagens\\nba_icon.png");
        setIconImage(icon.getImage());

        // Fechar a aplicação ao fechar a janela
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Inicializar banco de dados e UI
        initializeDatabase();
        initializeUI();

        // Dimensionar corretamente os componentes
        pack();

        // Centralizar na tela após definir o tamanho
        setLocationRelativeTo(null);
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
        panelTimes = new JPanel(new BorderLayout()); // Usando BorderLayout
        String[] columnNames = {"ID", "Nome", "Vitorias"};
        tableModelTimes = new DefaultTableModel(columnNames, 0);
        tableTimes = new JTable(tableModelTimes);
        JScrollPane scrollPaneTimes = new JScrollPane(tableTimes);

        // Ajustar larguras das colunas da tabela de Times
        tableTimes.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        tableTimes.getColumnModel().getColumn(1).setPreferredWidth(500); // Nome
        tableTimes.getColumnModel().getColumn(2).setPreferredWidth(50);  // Vitórias

        JButton btnAddTime = new JButton("Adicionar Time");
        JButton btnEditTime = new JButton("Editar Time");
        JButton btnDeleteTime = new JButton("Deletar Time");
        JButton btnReloadTimes = new JButton("Recarregar Tabela");

        btnAddTime.addActionListener(this::addTime);
        btnEditTime.addActionListener(this::editTime);
        btnDeleteTime.addActionListener(this::deleteTime);
        btnReloadTimes.addActionListener(this::reloadTimes);

        // Criar um painel para os botões
        JPanel panelButtonsTimes = new JPanel();
        panelButtonsTimes.add(btnAddTime);
        panelButtonsTimes.add(btnEditTime);
        panelButtonsTimes.add(btnDeleteTime);
        panelButtonsTimes.add(btnReloadTimes);

        panelTimes.add(panelButtonsTimes, BorderLayout.NORTH);
        panelTimes.add(scrollPaneTimes, BorderLayout.CENTER);

        tabbedPane.addTab("Times", panelTimes);

        // Painel de Jogadores
        panelJogadores = new JPanel(new BorderLayout());
        String[] jogadorColumnNames = {"ID", "Nome", "Altura", "Time", "Pontos"};
        tableModelJogadores = new DefaultTableModel(jogadorColumnNames, 0);
        tableJogadores = new JTable(tableModelJogadores);
        JScrollPane scrollPaneJogadores = new JScrollPane(tableJogadores);

        // Ajustar larguras das colunas da tabela de Jogadores
        tableJogadores.getColumnModel().getColumn(0).setPreferredWidth(66);
        tableJogadores.getColumnModel().getColumn(1).setPreferredWidth(200);
        tableJogadores.getColumnModel().getColumn(2).setPreferredWidth(66);
        tableJogadores.getColumnModel().getColumn(3).setPreferredWidth(200);
        tableJogadores.getColumnModel().getColumn(4).setPreferredWidth(66);

        JButton btnAddJogador = new JButton("Adicionar Jogador");
        JButton btnEditJogador = new JButton("Editar Jogador");
        JButton btnDeleteJogador = new JButton("Deletar Jogador");
        JButton btnReloadJogadores = new JButton("Recarregar Tabela");

        btnAddJogador.addActionListener(this::addJogador);
        btnEditJogador.addActionListener(this::editJogador);
        btnDeleteJogador.addActionListener(this::deleteJogador);
        btnReloadJogadores.addActionListener(this::reloadTabela);

        // Criar um painel para os botões
        JPanel panelButtonsJogadores = new JPanel();
        panelButtonsJogadores.add(btnAddJogador);
        panelButtonsJogadores.add(btnEditJogador);
        panelButtonsJogadores.add(btnDeleteJogador);
        panelButtonsJogadores.add(btnReloadJogadores);

        panelJogadores.add(panelButtonsJogadores, BorderLayout.NORTH);
        panelJogadores.add(scrollPaneJogadores, BorderLayout.CENTER);

        tabbedPane.addTab("Jogadores", panelJogadores);

        // Painel de Jogos
        panelJogos = new JPanel(new BorderLayout());
        String[] jogoColumnNames = {"ID", "Time A", "Time B", "Data", "Resultado"};
        tableModelJogos = new DefaultTableModel(jogoColumnNames, 0);
        tableJogos = new JTable(tableModelJogos);
        JScrollPane scrollPaneJogos = new JScrollPane(tableJogos);

        // Ajustar larguras das colunas da tabela de Jogos
        tableJogos.getColumnModel().getColumn(0).setPreferredWidth(30);
        tableJogos.getColumnModel().getColumn(1).setPreferredWidth(130);
        tableJogos.getColumnModel().getColumn(2).setPreferredWidth(130);
        tableJogos.getColumnModel().getColumn(3).setPreferredWidth(60);
        tableJogos.getColumnModel().getColumn(4).setPreferredWidth(200);

        JButton btnAddJogo = new JButton("Adicionar Jogo");
        JButton btnDeleteJogo = new JButton("Deletar Jogo");
        JButton btnReloadJogos = new JButton("Recarregar Tabela");

        btnAddJogo.addActionListener(this::addJogo);
        btnDeleteJogo.addActionListener(this::deleteJogo);
        btnReloadJogos.addActionListener(this::reloadJogos);

        // Criar um painel para os botões
        JPanel panelButtonsJogos = new JPanel();
        panelButtonsJogos.add(btnAddJogo);
        panelButtonsJogos.add(btnDeleteJogo);
        panelButtonsJogos.add(btnReloadJogos);

        panelJogos.add(panelButtonsJogos, BorderLayout.NORTH);
        panelJogos.add(scrollPaneJogos, BorderLayout.CENTER);

        tabbedPane.addTab("Jogos", panelJogos);

        add(tabbedPane);

        loadTimes();      // Carrega os times automaticamente
        loadJogadores();  // Carrega os jogadores automaticamente
        loadJogos();      // Carrega os jogos automaticamente
    }

    private void loadTimes() {
        try {
            List<Time> times = timeDAO.read();

            // Verifica se a lista de times está vazia
            if (times == null || times.isEmpty()) {
                System.out.println("Nenhum time encontrado.");
                return;
            }

            // Ordena os times em ordem decrescente de vitórias
            times.sort(Comparator.comparingInt(Time::getVitorias).reversed());

            // Agora carrega os times na tabela
            tableModelTimes.setRowCount(0); // Limpa a tabela
            for (Time time : times) {
                Object[] row = {
                    time.getId(),
                    time.getNome(),
                    time.getVitorias()
                };
                tableModelTimes.addRow(row); // Adiciona a linha na tabela
            }
            System.out.println("Times carregados com sucesso!");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar times.", "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
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
                    jogador.getTimeNome(),
                    jogador.getPontos()
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
        // Exibe um input dialog para o nome do time
        String nome = JOptionPane.showInputDialog(this, "Nome do Time:");

        if (nome != null && !nome.trim().isEmpty()) {
            try {
                // Verifica se o nome do time já existe
                if (timeDAO.existsByName(nome)) {
                    JOptionPane.showMessageDialog(this, "Um time com esse nome já existe.", "Erro", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Cria um novo objeto Time e preenche com o nome inserido
                Time time = new Time();
                time.setNome(nome);

                // Salva o novo time no banco de dados
                timeDAO.create(time);

                // Recarrega a tabela de times
                loadTimes();

                JOptionPane.showMessageDialog(this, "Time adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao adicionar time.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Nome do time não pode estar vazio.", "Erro", JOptionPane.WARNING_MESSAGE);
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
                dialog.setLayout(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(10, 10, 10, 10); // Espaçamento entre os componentes

                // Campos de texto para as informações do time
                JTextField nomeField = new JTextField(time.getNome(), 20); // Ajuste de largura

                // Adiciona os campos ao modal
                gbc.gridx = 0; gbc.gridy = 0;
                dialog.add(new JLabel("Nome:"), gbc);
                gbc.gridx = 1; gbc.gridy = 0;
                dialog.add(nomeField, gbc);

                // Botões para salvar ou cancelar
                JButton saveButton = new JButton("Salvar");
                JButton cancelButton = new JButton("Cancelar");

                gbc.gridx = 0; gbc.gridy = 1;
                dialog.add(saveButton, gbc);
                gbc.gridx = 1; gbc.gridy = 1;
                dialog.add(cancelButton, gbc);

                // Ação do botão "Salvar"
                saveButton.addActionListener(event -> {
                    try {
                        String nome = nomeField.getText().trim();

                        // Atualiza o objeto time
                        time.setNome(nome);

                        // Salva as mudanças no banco de dados
                        timeDAO.update(time);

                        // Atualiza a tabela
                        loadTimes();

                        // Fecha o modal
                        dialog.dispose();
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
        criarJogadorFrame.setLocationRelativeTo(this); // 'this' refere-se à janela atual (TimeJogadoresMaven)

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
private void editJogador(ActionEvent e) {
    int selectedRow = tableJogadores.getSelectedRow();

    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Selecione um jogador para editar.", "Erro", JOptionPane.WARNING_MESSAGE);
        return;
    }

    int id = (int) tableModelJogadores.getValueAt(selectedRow, 0);

    try {
        Jogador jogador = jogadorDAO.readById(id);
        if (jogador == null) {
            JOptionPane.showMessageDialog(this, "Jogador não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Criar o modal para edição
        JDialog modal = new JDialog(this, "Editar Jogador", true);
        modal.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10); // Espaçamento entre os componentes

        // Campos de edição
        JTextField nomeField = new JTextField(jogador.getNome(), 20);
        JTextField alturaField = new JTextField(String.valueOf(jogador.getAltura()), 20);

        // Carrega os times
        loadTimes();
        JComboBox<String> timeComboBox = new JComboBox<>();

        // Preenche o ComboBox com os nomes dos times
        for (int i = 0; i < tableModelTimes.getRowCount(); i++) {
            String timeNome = (String) tableModelTimes.getValueAt(i, 1);
            timeComboBox.addItem(timeNome);
        }

        // Define o time atual do jogador como selecionado no ComboBox
        timeComboBox.setSelectedItem(jogador.getTimeNome());

        // Adiciona campos ao modal
        gbc.gridx = 0; gbc.gridy = 0;
        modal.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        modal.add(nomeField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        modal.add(new JLabel("Altura (em metros):"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        modal.add(alturaField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        modal.add(new JLabel("Time:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        modal.add(timeComboBox, gbc);

        // Botão para salvar as alterações
        JButton saveButton = new JButton("Salvar");
        saveButton.addActionListener(actionEvent -> {
            String nome = nomeField.getText().trim();
            String alturaStr = alturaField.getText().trim();
            String timeNome = (String) timeComboBox.getSelectedItem();

            if (nome.isEmpty() || alturaStr.isEmpty() || timeNome == null) {
                JOptionPane.showMessageDialog(modal, "Todos os campos devem ser preenchidos.", "Erro", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Validação da altura
            double altura;
            try {
                altura = Double.parseDouble(alturaStr);
                if (altura <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(modal, "Altura inválida.", "Erro", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Atualiza os dados do jogador
            jogador.setNome(nome);
            jogador.setAltura(altura);
            jogador.setTimeNome(timeNome);

            try {
                jogadorDAO.update(jogador); // Atualiza no banco de dados
                JOptionPane.showMessageDialog(modal, "Jogador atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                loadJogadores(); // Atualiza a tabela de jogadores
                modal.dispose(); // Fecha o modal após salvar
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(modal, "Erro ao salvar as alterações.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Botão para cancelar as alterações
        JButton cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener(actionEvent -> modal.dispose());

        // Adiciona os botões ao modal
        gbc.gridx = 0; gbc.gridy = 3;
        modal.add(saveButton, gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        modal.add(cancelButton, gbc);

        // Configurações finais do modal
        modal.setSize(400, 300);
        modal.setLocationRelativeTo(this); // Centraliza o modal
        modal.setVisible(true); // Exibe o modal
    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Erro ao buscar jogador.", "Erro", JOptionPane.ERROR_MESSAGE);
    }
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

    private void recarregarTodasTabelas() {
        JOptionPane.showMessageDialog(null, "Tabelas Recarregadas");
        loadJogadores();
        loadTimes();
        loadJogos();
    }

    private void reloadTabela(ActionEvent e) {
        recarregarTodasTabelas();
    }

    private void reloadJogos(ActionEvent e) {
        recarregarTodasTabelas();
    }

    private void reloadTimes(ActionEvent e) {
        recarregarTodasTabelas();
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

            // Verificar se ambos os times têm jogadores
            JogadorDAO jogadorDAO = new JogadorDAO(connection);
            List<Jogador> jogadoresTimeA = jogadorDAO.getJogadoresByTimeId(getTimeIdByName(timeANome));
            List<Jogador> jogadoresTimeB = jogadorDAO.getJogadoresByTimeId(getTimeIdByName(timeBNome));

            if (jogadoresTimeA.isEmpty() || jogadoresTimeB.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ambos os times devem ter jogadores antes de iniciar a partida.", "Erro", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Simular pontuação dos times
            int scoreA = 75 + (int) (Math.random() * 36); // Gera pontos entre 75 e 110
            int scoreB = 75 + (int) (Math.random() * 36); // Gera pontos entre 75 e 110

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
                } else {
                    timeDAO.adicionarVitoria(timeBId);
                }
            }
            simularPontos(timeAId, timeBId, scoreA, scoreB); // Chama a distribuição de pontos

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
            loadTimes();

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

    private void simularPontos(int timeAId, int timeBId, int golsTimeA, int golsTimeB) throws SQLException {
        JogadorDAO jogadorDAO = new JogadorDAO(connection);
        TimeDAO timeDAO = new TimeDAO(connection);

        // Obter jogadores de cada time
        List<Jogador> jogadoresTimeA = jogadorDAO.getJogadoresByTimeId(timeAId);
        List<Jogador> jogadoresTimeB = jogadorDAO.getJogadoresByTimeId(timeBId);

        // Obter os nomes dos times
        Time timeA = timeDAO.getTimeById(timeAId);
        Time timeB = timeDAO.getTimeById(timeBId);

        // Distribuir pontos para o time A
        Map<Jogador, Integer> golsPorJogadorA = distribuirPontosEntreJogadores(jogadoresTimeA, golsTimeA);

        // Distribuir pontos para o time B
        Map<Jogador, Integer> golsPorJogadorB = distribuirPontosEntreJogadores(jogadoresTimeB, golsTimeB);

        // Exibir os pontos de ambos os times
        StringBuilder mensagem = new StringBuilder();
        mensagem.append("Pontos do jogo\n\n");

        // Gols do Time A
        mensagem.append("Pontos do: ").append(timeA.getNome()).append("\n");
        for (Map.Entry<Jogador, Integer> entry : golsPorJogadorA.entrySet()) {
            Jogador jogador = entry.getKey();
            int pontos = entry.getValue();
            mensagem.append(jogador.getNome()).append(": ").append(pontos).append("\n");
            jogadorDAO.atualizarPontos(jogador.getId(), pontos); // Aqui você atualiza os pontos no banco
        }

        mensagem.append("\n"); // Espaçamento entre os times

        // Pontos do Time B
        mensagem.append("\nPontos do: ").append(timeB.getNome()).append("\n");
        for (Map.Entry<Jogador, Integer> entry : golsPorJogadorB.entrySet()) {
            Jogador jogador = entry.getKey();
            int pontos = entry.getValue();
            mensagem.append(jogador.getNome()).append(": ").append(pontos).append("\n");
            jogadorDAO.atualizarPontos(jogador.getId(), pontos); // Aqui você atualiza os pontos no banco
        }

        JOptionPane.showMessageDialog(null, mensagem.toString(), "Pontos do jogo", JOptionPane.INFORMATION_MESSAGE);
    }

    private Map<Jogador, Integer> distribuirPontosEntreJogadores(List<Jogador> jogadores, int totalPontos) {
        Map<Jogador, Integer> pontosPorJogador = new HashMap<>();
        Random random = new Random();

        // Inicializa o mapa com 0 pontos para cada jogador
        for (Jogador jogador : jogadores) {
            pontosPorJogador.put(jogador, 0);
        }

        // Calcula a soma total das alturas dos jogadores
        double somaAlturas = jogadores.stream().mapToDouble(Jogador::getAltura).sum();

        // Distribui os pontos de forma aleatória entre os jogadores
        while (totalPontos > 0) {
            // Gera um número aleatório entre 0 e a soma total das alturas (em inteiros)
            int randomHeight = random.nextInt((int) Math.round(somaAlturas * 100)); // Multiplica por 100 para evitar problemas de arredondamento

            // Escolhe o jogador com base na altura
            Jogador jogadorSelecionado = null;
            double acumulador = 0;

            for (Jogador jogador : jogadores) {
                acumulador += jogador.getAltura() * 100; // Aumenta o acumulador pela altura do jogador (multiplicada por 100)
                if (acumulador > randomHeight) {
                    jogadorSelecionado = jogador; // Jogador selecionado com base na altura
                    break;
                }
            }

            // Sorteia se o jogador fez uma jogada de 1, 2 ou 3 pontos
            int pontosFeitos = random.nextInt(3) + 1; // Gera um número entre 1 e 3

            // Verifica se ainda há pontos suficientes para adicionar
            if (totalPontos >= pontosFeitos && jogadorSelecionado != null) {
                pontosPorJogador.put(jogadorSelecionado, pontosPorJogador.get(jogadorSelecionado) + pontosFeitos);
                totalPontos -= pontosFeitos;
            }
        }

        return pontosPorJogador;
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
        try {
            UIManager.setLookAndFeel(new FlatMacDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
        SwingUtilities.invokeLater(() -> {
            TimeJogadoresMaven frame = new TimeJogadoresMaven();
            frame.setVisible(true);
        });
    }

}
