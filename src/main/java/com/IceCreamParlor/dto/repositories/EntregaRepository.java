package com.IceCreamParlor.dto.repositories;

import com.IceCreamParlor.dto.entities.EntregaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EntregaRepository extends JpaRepository<EntregaEntity, UUID> {

    Optional<EntregaEntity> findByPedidoId(UUID pedidoId);
}
