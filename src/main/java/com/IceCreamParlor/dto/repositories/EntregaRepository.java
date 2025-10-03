package com.IceCreamParlor.dto.repositories;

import com.IceCreamParlor.dto.entities.ClienteEntity;
import com.IceCreamParlor.dto.entities.EntregaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EntregaRepository extends JpaRepository<EntregaEntity, UUID> {

    List<ClienteEntity> findByPedidoId(UUID pedidoId);
    List<ClienteEntity> findByClienteId(String clienteId);
}
