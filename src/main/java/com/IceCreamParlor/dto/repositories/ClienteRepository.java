package com.IceCreamParlor.dto.repositories;

import com.IceCreamParlor.dto.entities.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteEntity, UUID> {

    List<ClienteEntity> findByPedidoId(UUID pedidoId);
    List<ClienteEntity> findByClienteId(String clienteId);
}
