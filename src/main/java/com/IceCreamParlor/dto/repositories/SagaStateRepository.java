package com.IceCreamParlor.dto.repositories;

import com.IceCreamParlor.dto.entities.SagaState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SagaStateRepository extends JpaRepository<SagaState, UUID> {
}
