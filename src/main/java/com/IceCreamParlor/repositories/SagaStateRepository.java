package com.IceCreamParlor.repositories;

import com.IceCreamParlor.dto.entities.WorkflowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SagaStateRepository extends JpaRepository<WorkflowEntity, UUID> {
    Optional<WorkflowEntity> findByPedidoId(UUID pedidoId);
}