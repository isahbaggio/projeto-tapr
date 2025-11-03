CREATE TABLE produtos (
    id UUID PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT,
    preco NUMERIC(10, 2) NOT NULL,
    categoria VARCHAR(100) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT true
);
