CREATE DATABASE IF NOT EXISTS jogadorestimes;
USE jogadorestimes;

DROP TABLE IF EXISTS jogadores;
DROP TABLE IF EXISTS jogos;
DROP TABLE IF EXISTS times;

CREATE TABLE times (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE,
    cidade VARCHAR(100),
    estado VARCHAR(50),
    ano_fundacao INT,
    tecnico VARCHAR(100)
);

CREATE TABLE jogadores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    idade INT NOT NULL,
    posicao VARCHAR(50) NOT NULL,
    nacionalidade VARCHAR(50),
    altura DOUBLE NOT NULL,
    time_nome varchar(50),
    FOREIGN KEY (time_nome) REFERENCES times(nome) ON DELETE SET NULL
);

CREATE TABLE jogos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    time_a_id INT NOT NULL,
    time_b_id INT NOT NULL,
    data DATE NOT NULL,
    resultado VARCHAR(50),
    FOREIGN KEY (time_a_id) REFERENCES times(id) ON DELETE CASCADE,
    FOREIGN KEY (time_b_id) REFERENCES times(id) ON DELETE CASCADE
);



