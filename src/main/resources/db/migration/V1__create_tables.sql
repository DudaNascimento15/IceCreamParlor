create table if not exists workflow (
    id uuid primary key,
    pedido_id uuid not null,
    cliente_id varchar(100) not null,
    total numeric(14,2) not null,
    status varchar(20) not null, -- INICIADO, PAGAMENTO_OK, ESTOQUE_OK, CONFIRMADO
    pagamento_ok boolean default false,
    estoque_ok boolean default false,
    confirmado_em timestamp
);

create table if not exists caixa (
    id uuid primary key,
    pedido_id uuid not null,
    valor numeric(14,2) not null,
    status varchar(20) not null,
    motivo varchar(255),
    aprovado_por varchar(50),
    criado_em timestamp not null default now()
);

create table if not exists estoque (
    id uuid primary key,
    pedido_id uuid not null,
    itens_reservados boolean not null,
    status varchar(20) not null, -- CONFIRMADO ou NEGADO
    motivo varchar(255),
    reservado_em timestamp not null default now()
);

create table if not exists producao (
    id uuid primary key,
    pedido_id uuid not null,
    status varchar(20) not null, -- EM_PREPARO, PRONTO
    iniciado_em timestamp not null default now(),
    finalizado_em timestamp
);

create table if not exists entrega (
    id uuid primary key,
    pedido_id uuid not null,
    status varchar(50) not null,
    criado_em timestamp not null default now()
);

create table if not exists cliente (
    id uuid primary key,
    pedido_id uuid not null,
    cliente_id varchar(50) not null,
    mensagem varchar(255) not null,
    criado_em timestamp not null default now()
);

create table if not exists relatorio (
    id uuid primary key,
    pedido_id uuid,
    origem varchar(50), -- caixa, estoque, entregas, etc.
    evento varchar(100),
    payload jsonb,
    criado_em timestamp not null default now()
);



