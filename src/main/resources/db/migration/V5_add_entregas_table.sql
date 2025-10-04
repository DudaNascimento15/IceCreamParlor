-- TABELA: ENTREGAS
create table if not exists entregas (
    id uuid primary key,
    pedido_id uuid primary key,
    status varchar(20) not null, -- CRIADO, DESPACHADO, A_CAMINHO, ENTREGUE
    atualizado_em timestamptz not null default now()
);
