-- TABELA: CAIXA
create table if not exists caixa (
    id uuid primary key,
    pedido_id uuid primary key,
    valor numeric(14,2) not null,
    status varchar(20) not null,
    motivo varchar(255),
    aprovado_por varchar(50),
    criado_em timestamptz not null default now()
);