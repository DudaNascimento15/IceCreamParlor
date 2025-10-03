package com.IceCreamParlor.service.insterfaces;

import com.IceCreamParlor.dto.events.CaixaEvents;

public interface ClienteService {

    void processarPagamento(CaixaEvents.PagamentoIniciado evento);

}
