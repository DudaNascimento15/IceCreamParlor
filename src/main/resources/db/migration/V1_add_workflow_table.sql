create table if not exists workflow (
    id uuid primary key,
  pedido_id uuid primary key,
  cliente_id varchar(100) not null,
  total numeric(14,2) not null,
  status varchar(20) not null, -- ex: INICIADO, PAGAMENTO_OK, ESTOQUE_OK, CONFIRMADO
  pagamento_ok boolean default false,
  estoque_ok boolean default false,
  confirmado_em timestamptz
);