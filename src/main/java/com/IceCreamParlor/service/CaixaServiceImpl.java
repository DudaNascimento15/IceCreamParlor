package com.IceCreamParlor.service;

import com.IceCreamParlor.dto.entities.CaixaEntity;
import com.IceCreamParlor.dto.enums.StatusCaixaEnum;
import com.IceCreamParlor.dto.events.CaixaEvents;
import com.IceCreamParlor.dto.events.WorkflowEvents;
import com.IceCreamParlor.dto.repositories.CaixaRepository;
import com.IceCreamParlor.producer.CaixaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class CaixaServiceImpl implements CaixaService {

    private final CaixaRepository caixaRepository;

    private final CaixaProducer caixaProducer;

    private final Random random = new Random();

    private final RabbitTemplate rabbit;

    private static final String EX = "sorv.ex";

    public boolean simularAprovacao() {
        return new Random().nextDouble() < 0.7;
    }

    @Override
    @Transactional
    public void processarPagamento(WorkflowEvents.PagamentoIniciado evento, String correlationId, String usuario) {
        log.info("Processando pagamento para o pedido: " + evento.pedidoId());

        boolean aprovado = simularAprovacao();

        var pedidoIdString = evento.pedidoId().toString();

        if (aprovado) {
            CaixaEntity caixa = new CaixaEntity(
                pedidoIdString,
                StatusCaixaEnum.APROVADO.toString(),
                evento.valorTotal()
            );
            caixaRepository.save(caixa);

            CaixaEvents.PagamentoAprovado aprovadoEvent =
                new CaixaEvents.PagamentoAprovado(
                    evento.pedidoId(),
                    evento.valorTotal(),
                    evento.clienteId()
                );

            caixaProducer.publishPagamentoAprovado(aprovadoEvent, correlationId, usuario);
            log.info("Pagamento aprovado, para o pedido: {}" + evento.pedidoId());
        } else {
            CaixaEntity caixa = new CaixaEntity(
                pedidoIdString,
                StatusCaixaEnum.NEGADO.toString(),
                evento.valorTotal()
            );

            caixaRepository.save(caixa);

            CaixaEvents.PagamentoNegado negadoEvent =
                new CaixaEvents.PagamentoNegado(
                    evento.pedidoId(),
                    "Pagamento negado",
                    evento.clienteId()
                );

            caixaProducer.publishPagamentoNegado(negadoEvent, correlationId, usuario);

            log.warn("pagamento negado para o pedido: {}" + evento.pedidoId());
        }
    }

}