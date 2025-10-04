-- TABELA: CLIENTE (Notificações de pedidos)
create table if not exists cliente (
    id uuid primary key,
    pedido_id uuid not null,
    cliente_id varchar(50) not null,
    mensagem varchar(255) not null,
    criado_em timestamptz not null default now()
);