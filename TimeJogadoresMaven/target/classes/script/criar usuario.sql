CREATE DATABASE IF NOT EXISTS jogadorestimes;
USE jogadorestimes;

drop table if exists  jogadores;
drop table if exists jogos;
drop table if exists times;


CREATE TABLE times (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE,
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
    pontos int default 0,
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

use jogadorestimes;
INSERT INTO times (nome, vitorias) VALUES
('Los Angeles Lakers', 0),
('Chicago Bulls', 0),
('Golden State Warriors', 0),
('Miami Heat', 0),
('Boston Celtics', 0);


INSERT INTO jogadores (nome, idade, posicao, nacionalidade, altura, time_nome, pontos) VALUES
-- Los Angeles Lakers
('LeBron James', 39, 'Ala', 'Estados Unidos', 2.06, 'Los Angeles Lakers', 0),
('Anthony Davis', 31, 'Pivô', 'Estados Unidos', 2.08, 'Los Angeles Lakers', 0),

-- Chicago Bulls
('Zach LaVine', 29, 'Ala-armador', 'Estados Unidos', 1.98, 'Chicago Bulls', 0),
('Nikola Vucevic', 33, 'Pivô', 'Montenegro', 2.13, 'Chicago Bulls', 0),

-- Golden State Warriors
('Stephen Curry', 36, 'Armador', 'Estados Unidos', 1.91, 'Golden State Warriors', 0),
('Draymond Green', 34, 'Ala-pivô', 'Estados Unidos', 1.98, 'Golden State Warriors', 0),

-- Miami Heat
('Jimmy Butler', 35, 'Ala', 'Estados Unidos', 2.01, 'Miami Heat', 0),
('Bam Adebayo', 27, 'Pivô', 'Estados Unidos', 2.06, 'Miami Heat', 0),

-- Boston Celtics
('Jayson Tatum', 26, 'Ala', 'Estados Unidos', 2.03, 'Boston Celtics', 0),
('Jaylen Brown', 27, 'Ala-armador', 'Estados Unidos', 1.98, 'Boston Celtics', 0);