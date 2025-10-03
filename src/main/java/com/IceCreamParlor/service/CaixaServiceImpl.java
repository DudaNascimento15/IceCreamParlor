package com.IceCreamParlor.service;

import com.IceCreamParlor.dto.entities.CaixaEntity;
import com.IceCreamParlor.dto.events.CaixaEvents;
import com.IceCreamParlor.dto.repositories.CaixaRepository;
import com.IceCreamParlor.service.insterfaces.CaixaService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CaixaServiceImpl implements CaixaService {

    private final CaixaRepository caixaRepository;

    private final RabbitTemplate rabbit;

    public CaixaServiceImpl(CaixaRepository caixaRepository, RabbitTemplate rabbit) {
        this.caixaRepository = caixaRepository;
        this.rabbit = rabbit;
    }

    private static final String EX = "sorv.ex";

    public void processarPagamento(CaixaEvents.PagamentoIniciado evento) {
        System.out.println("Processando pagamento para o pedido: " + evento.pedidoId());

        boolean aprovado = simularAprovacao();

        var pedidoIdString = evento.pedidoId().toString();

        if (aprovado) {
            CaixaEntity caixa = new CaixaEntity(pedidoIdString, "APROVADO", evento.valorTotal());
            caixaRepository.save(caixa);

            CaixaEvents.PagamentoAprovado aprovaoEvent = new CaixaEvents.PagamentoAprovado(evento.pedidoId(), evento.valorTotal());

            rabbit.convertAndSend(EX, "caixa.pagamento.aprovado", aprovaoEvent);
        }  else{
            CaixaEntity caixa = new CaixaEntity(pedidoIdString, "NEGADO", evento.valorTotal());
            caixaRepository.save(caixa);

            CaixaEvents.PagamentoNegado negadoEvent = new CaixaEvents.PagamentoNegado(evento.pedidoId(), "Pagamento negado");

            rabbit.convertAndSend(EX, "caixa.pagamento.negado", negadoEvent);

            System.out.println("Pagamento negado");
        }
    }

    public boolean simularAprovacao() {
        return new Random().nextDouble() < 0.7;
    }

}