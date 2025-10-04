-- TABELA: ESTOQUE
create table if not exists estoque (
    id uuid primary key,
    pedido_id uuid primary key,
    itens_reservados boolean not null,
    status varchar(20) not null, -- CONFIRMADO ou NEGADO
    motivo varchar(255),
    reservado_em timestamptz not null default now()
);
