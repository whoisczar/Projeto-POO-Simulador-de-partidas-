CREATE DATABASE IF NOT EXISTS jogadorestimes;
USE jogadorestimes;

drop table if exists  jogadores;
drop table if exists jogos;
drop table if exists times;


CREATE TABLE times (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE,
    cidade VARCHAR(100),
    estado VARCHAR(50),
    ano_fundacao INT,
    tecnico VARCHAR(100),
    vitorias int default 0
);

CREATE TABLE jogadores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    idade INT NOT NULL,
    posicao VARCHAR(50) NOT NULL,
    nacionalidade VARCHAR(50),
    altura DOUBLE NOT NULL,
    time_nome varchar(50),
    gols int default 0,
    FOREIGN KEY (time_nome) REFERENCES times(nome) ON DELETE SET NULL
);

CREATE TABLE jogos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    time_a_id INT NOT NULL,
    time_b_id INT NOT NULL,
    data VARCHAR(50) NOT NULL,
    resultado VARCHAR(50),
    FOREIGN KEY (time_a_id) REFERENCES times(id) ON DELETE CASCADE,
    FOREIGN KEY (time_b_id) REFERENCES times(id) ON DELETE CASCADE
);


INSERT INTO times (nome, cidade, estado, ano_fundacao, tecnico, vitorias)
VALUES 
('Flamengo', 'Rio de Janeiro', 'RJ', 1895, 'Jorge Jesus', 15),
('Palmeiras', 'São Paulo', 'SP', 1914, 'Abel Ferreira', 12),
('Atlético Mineiro', 'Belo Horizonte', 'MG', 1908, 'Cuca', 10),
('Grêmio', 'Porto Alegre', 'RS', 1903, 'Renato Gaúcho', 8),
('Corinthians', 'São Paulo', 'SP', 1910, 'Vítor Pereira', 14);


INSERT INTO jogadores (nome, idade, posicao, nacionalidade, altura, time_nome, gols)
VALUES 
('Gabriel Barbosa', 26, 'Atacante', 'Brasil', 1.76, 'Flamengo', 12),
('Giorgian de Arrascaeta', 30, 'Meia', 'Uruguai', 1.72, 'Flamengo', 8),
('Rony', 28, 'Atacante', 'Brasil', 1.69, 'Palmeiras', 10),
('Gustavo Gómez', 30, 'Zagueiro', 'Paraguai', 1.85, 'Palmeiras', 2),
('Hulk', 37, 'Atacante', 'Brasil', 1.80, 'Atlético Mineiro', 14),
('Nacho Fernández', 33, 'Meia', 'Argentina', 1.74, 'Atlético Mineiro', 6),
('Ferreira', 25, 'Atacante', 'Brasil', 1.71, 'Grêmio', 7),
('Pedro Geromel', 38, 'Zagueiro', 'Brasil', 1.90, 'Grêmio', 1),
('Fábio Santos', 38, 'Lateral', 'Brasil', 1.80, 'Corinthians', 3),
('Yuri Alberto', 23, 'Atacante', 'Brasil', 1.83, 'Corinthians', 9);
