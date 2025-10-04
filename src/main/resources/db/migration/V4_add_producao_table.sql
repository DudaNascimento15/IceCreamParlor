-- TABELA: PRODUCAO
create table if not exists producao (
    id uuid primary key,
    pedido_id uuid primary key,
    status varchar(20) not null, -- EM_PREPARO, PRONTO
    iniciado_em timestamptz not null default now(),
    finalizado_em timestamptz
);