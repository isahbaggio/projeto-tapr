CREATE TABLE vendas (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    data_venda TIMESTAMP NOT NULL,
    valor_total DECIMAL(10, 2) NOT NULL,
    observacoes TEXT,
    cancelada BOOLEAN NOT NULL DEFAULT false,
    CONSTRAINT fk_venda_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id)
);
