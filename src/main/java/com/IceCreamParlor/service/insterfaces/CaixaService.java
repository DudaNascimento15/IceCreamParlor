package com.IceCreamParlor.service.insterfaces;

import com.IceCreamParlor.dto.events.CaixaEvents;

public interface CaixaService {

    void processarPagamento(CaixaEvents.PagamentoIniciado evento);

}
