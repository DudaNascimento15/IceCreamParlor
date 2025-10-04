-- TABELA: RELATORIO++
create table if not exists relatorio (
    id uuid primary key,
    pedido_id uuid,
    origem varchar(50), -- caixa, estoque, entregas, etc.
    evento varchar(100),
    payload jsonb,
    criado_em timestamptz not null default now()
);