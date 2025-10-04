package com.IceCreamParlor;

import com.IceCreamParlor.config.RabbitMqConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


import java.util.Properties;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQHealthCheck implements CommandLineRunner {

    private final RabbitAdmin rabbitAdmin;

    @Override
    public void run(String... args) {
        try {
            Properties properties = rabbitAdmin.getQueueProperties(RabbitMqConfig.Q_CAIXA);

            if (properties != null) {
                log.info("✅ CONEXÃO COM CLOUDAMQP ESTABELECIDA COM SUCESSO!");
                log.info("📊 Fila {} configurada - {} mensagens",
                    RabbitMqConfig.Q_CAIXA,
                    properties.get("QUEUE_MESSAGE_COUNT"));
            }

            // Lista todas as filas criadas
            log.info("🔧 Filas configuradas:");
            log.info("  - {}", RabbitMqConfig.Q_CAIXA);
            log.info("  - {}", RabbitMqConfig.Q_ESTOQUE);
            log.info("  - {}", RabbitMqConfig.Q_PRODUCAO);
            log.info("  - {}", RabbitMqConfig.Q_ENTREGAS);
            log.info("  - {}", RabbitMqConfig.Q_CLIENTE);
            log.info("  - {}", RabbitMqConfig.Q_RELATORIO);
            log.info("  - {}", RabbitMqConfig.Q_WORKFLOW);

        } catch (Exception e) {
            log.error("❌ ERRO AO CONECTAR NO CLOUDAMQP: {}", e.getMessage());
            log.error("🔍 Verifique:");
            log.error("  1. Credenciais no application.yaml");
            log.error("  2. Se o plano CloudAMQP está ativo");
            log.error("  3. Se há conexões disponíveis no plano");
        }
    }
}