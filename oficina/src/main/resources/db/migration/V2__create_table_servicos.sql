CREATE TABLE servicos (
    id UUID PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT,
    preco NUMERIC(10, 2) NOT NULL,
    duracao INTEGER NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT true
);
