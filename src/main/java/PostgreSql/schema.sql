create table if not exists workflow (
  pedido_id uuid primary key,
  cliente_id varchar(100) not null,
  total numeric(14,2) not null,
  status varchar(20) not null, -- ex: INICIADO, PAGAMENTO_OK, ESTOQUE_OK, CONFIRMADO
  pagamento_ok boolean default false,
  estoque_ok boolean default false,
  confirmado_em timestamptz
);


create table if not exists processed_event (
  message_id varchar(100) primary key
);

-- TABELA: CAIXA
create table if not exists caixa (
    pedido_id uuid primary key,
    valor numeric(14,2) not null,
    status varchar(20) not null,
    motivo varchar(255),
    aprovado_por varchar(50),
    criado_em timestamptz not null default now()
);

-- TABELA: ESTOQUE
create table if not exists estoque (
    pedido_id uuid primary key,
    itens_reservados boolean not null,
    status varchar(20) not null, -- CONFIRMADO ou NEGADO
    motivo varchar(255),
    reservado_em timestamptz not null default now()
);

-- TABELA: PRODUCAO
create table if not exists producao (
    pedido_id uuid primary key,
    status varchar(20) not null, -- EM_PREPARO, PRONTO
    iniciado_em timestamptz not null default now(),
    finalizado_em timestamptz
);

-- TABELA: ENTREGAS
create table if not exists entregas (
    pedido_id uuid primary key,
    status varchar(20) not null, -- CRIADO, DESPACHADO, A_CAMINHO, ENTREGUE
    atualizado_em timestamptz not null default now()
);

-- TABELA: CLIENTE (Notificações de pedidos)
create table if not exists cliente (
    id serial primary key,
    pedido_id uuid not null,
    cliente_id varchar(50) not null,
    mensagem varchar(255) not null,
    criado_em timestamptz not null default now()
);


-- TABELA: RELATORIO (log de eventos para BI ou auditoria)
create table if not exists relatorio_eventos (
    id serial primary key,
    pedido_id uuid,
    origem varchar(50), -- caixa, estoque, entregas, etc.
    evento varchar(100),
    payload jsonb,
    criado_em timestamptz not null default now()
);

-- TABELA: WORKFLOW (Estado do pedido)
create table if not exists workflow (
    pedido_id uuid primary key,
    status varchar(30) not null, -- EX: EM_ANDAMENTO, APROVADO, NEGADO, CANCELADO
    pagamento_ok boolean default false,
    estoque_ok boolean default false,
    confirmado_em timestamptz,
    criado_em timestamptz not null default now()
);