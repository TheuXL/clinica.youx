-- Tabela de Roles
CREATE TABLE role (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL UNIQUE
);

-- Tabela de Usuários
CREATE TABLE usuario (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(64) NOT NULL UNIQUE,
    senha VARCHAR(128) NOT NULL
);

-- Tabela para Pacientes
CREATE TABLE paciente (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(64) NOT NULL UNIQUE,
    data_nascimento DATE NOT NULL,
    peso DECIMAL(5,2),
    altura DECIMAL(3,2),
    uf VARCHAR(2) NOT NULL
);

-- Tabela de Usuários e Roles
CREATE TABLE usuario_roles (
    usuario_id INT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (usuario_id, role_id),
    FOREIGN KEY (usuario_id) REFERENCES usuario(id),
    FOREIGN KEY (role_id) REFERENCES role(id)
);

-- Inserir Roles
INSERT INTO role (nome) VALUES ('ROLE_ENFERMEIRO');
INSERT INTO role (nome) VALUES ('ROLE_MEDICO');