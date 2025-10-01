create table if not exists saga_workflow (
  pedido_id uuid primary key,
  cliente_id varchar(60) not null,
  total numeric(14,2) not null,
  pagamento_aprovado boolean not null default false,
  estoque_reservado boolean not null default false,
  criado_em timestamptz not null default now()
);

create table if not exists processed_event (
  message_id varchar(100) primary key
);
