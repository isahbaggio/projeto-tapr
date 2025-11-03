INSERT INTO produtos (id, nome, descricao, preco, categoria, ativo) VALUES
(gen_random_uuid(), 'Shampoo para Limpeza', 'Shampoo automotivo concentrado para limpeza profunda', 45.90, 'Produtos de Limpeza', true),
(gen_random_uuid(), 'Pneu 185/65 R15', 'Pneu radial para veículos de passeio', 320.00, 'Pneus', true),
(gen_random_uuid(), 'Vela de Ignição NGK', 'Vela de ignição de platina', 28.50, 'Peças', true),
(gen_random_uuid(), 'Óleo de Motor 5W30', 'Óleo sintético para motor', 65.00, 'Lubrificantes', true),
(gen_random_uuid(), 'Filtro de Ar', 'Filtro de ar para motores 1.0 a 1.6', 38.00, 'Peças', true);

INSERT INTO servicos (id, nome, descricao, preco, duracao, ativo) VALUES
(gen_random_uuid(), 'Lavagem Padrão', 'Lavagem externa e aspiração interna', 50.00, 30, true),
(gen_random_uuid(), 'Lavagem Premium', 'Lavagem completa com cera e hidratação', 120.00, 90, true),
(gen_random_uuid(), 'Troca de Óleo', 'Troca de óleo e filtro', 80.00, 45, true),
(gen_random_uuid(), 'Alinhamento e Balanceamento', 'Alinhamento de direção e balanceamento de rodas', 150.00, 60, true),
(gen_random_uuid(), 'Polimento', 'Polimento de pintura completo', 300.00, 240, true);

INSERT INTO clientes (id, nome, telefone, email, cpf, endereco, ativo) VALUES
(gen_random_uuid(), 'João Silva', '(11) 98765-4321', 'joao.silva@email.com', '123.456.789-00', 'Rua das Flores, 123', true),
(gen_random_uuid(), 'Maria Santos', '(11) 91234-5678', 'maria.santos@email.com', '987.654.321-00', 'Av. Paulista, 1000', true),
(gen_random_uuid(), 'Pedro Oliveira', '(11) 99999-8888', 'pedro.oliveira@email.com', '456.789.123-00', 'Rua dos Pinheiros, 456', true);
