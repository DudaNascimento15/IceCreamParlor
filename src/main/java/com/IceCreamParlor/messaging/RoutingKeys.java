package com.IceCreamParlor.messaging;

public interface RoutingKeys {
    // Caixa
    String CAIXA_PAGAMENTO_INICIADO = "caixa.pagamento.iniciado";
    String CAIXA_PAGAMENTO_APROVADO = "caixa.pagamento.aprovado";
    String CAIXA_PAGAMENTO_NEGADO   = "caixa.pagamento.negado";

    // Estoque
    String ESTOQUE_RESERVA_SOLICITADA = "estoque.reserva.solicitada";
    String ESTOQUE_RESERVA_CONFIRMADA = "estoque.reserva.confirmada";
    String ESTOQUE_RESERVA_NEGADA     = "estoque.reserva.negada";

    // Pedidos / Produção / Entregas
    String PEDIDOS_PEDIDO_CRIAR     = "pedidos.pedido.criar";
    String PEDIDOS_PEDIDO_CONFIRMADO= "pedidos.pedido.confirmado";

    String ENTREGAS_PEDIDO_CRIAR    = "entregas.pedido.criar";
    String ENTREGAS_PEDIDO_DESPACHADO = "entregas.pedido.despachado";
    String ENTREGAS_PEDIDO_A_CAMINHO = "entregas.pedido.a_caminho";
    String ENTREGAS_PEDIDO_ENTREGUE  = "entregas.pedido.entregue";

    // Relatórios / Workflow
    String RELATORIO_ANY = "relatorio.*";
    String WORKFLOW_ANY  = "workflow.*";
}
