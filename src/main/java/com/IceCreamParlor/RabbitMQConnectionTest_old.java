package com.IceCreamParlor;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Classe para testar a conexão com CloudAMQP
 * Execute esta classe standalone antes de rodar a aplicação completa
 */
public class RabbitMQConnectionTest_old {

    /*   public static void main(String[] args) {
        System.out.println("🔍 Testando conexão com CloudAMQP...\n");

        ConnectionFactory factory = new ConnectionFactory();

        try {
            // Configuração CloudAMQP
            factory.setHost("jackal.rmq.cloudamqp.com");
            factory.setPort(5671);
            factory.setUsername("kmskovrf");
            factory.setPassword("Ei9fF3bR2B6P_7_R_vaxARNfCXkKqj1d");
            factory.setVirtualHost("kmskovrf");

            // SSL habilitado (obrigatório para CloudAMQP)
            factory.useSslProtocol();

            // Timeouts
            factory.setConnectionTimeout(10000);
            factory.setHandshakeTimeout(10000);

            System.out.println("📡 Tentando conectar...");
            System.out.println("Host: " + factory.getHost());
            System.out.println("Port: " + factory.getPort());
            System.out.println("VHost: " + factory.getVirtualHost());
            System.out.println("SSL: Habilitado\n");
            // Tenta criar conexão
            Connection connection = factory.newConnection("test-connection");

            if (connection.isOpen()) {
                System.out.println("✅ SUCESSO! Conexão estabelecida com CloudAMQP");
                System.out.println("Connection ID: " + connection.getId());
                System.out.println("Server Properties: " + connection.getServerProperties());

                // Fecha a conexão de teste
                connection.close();
                System.out.println("\n✅ Conexão fechada corretamente");
            }

        } catch (Exception e) {
            System.err.println("❌ ERRO ao conectar com CloudAMQP:");
            System.err.println("Tipo: " + e.getClass().getName());
            System.err.println("Mensagem: " + e.getMessage());

            if (e.getCause() != null) {
                System.err.println("Causa raiz: " + e.getCause().getMessage());
            }

            System.err.println("\n🔧 Possíveis soluções:");
            System.err.println("1. Verifique se as credenciais estão corretas no CloudAMQP");
            System.err.println("2. Confirme que sua instância CloudAMQP está ativa");
            System.err.println("3. Verifique se há firewall bloqueando porta 5671");
            System.err.println("4. Teste a URL no navegador: https://jackal.rmq.cloudamqp.com");

            e.printStackTrace();
            System.exit(1);
        }
    }*/
}