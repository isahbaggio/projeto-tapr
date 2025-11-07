CREATE TABLE itens_venda (
    id UUID PRIMARY KEY,
    venda_id UUID NOT NULL,
    tipo_item VARCHAR(20) NOT NULL,
    item_id UUID NOT NULL,
    item_nome VARCHAR(255) NOT NULL,
    quantidade INTEGER NOT NULL,
    preco_unitario DECIMAL(10, 2) NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL,
    CONSTRAINT fk_item_venda FOREIGN KEY (venda_id) REFERENCES vendas(id)
);
