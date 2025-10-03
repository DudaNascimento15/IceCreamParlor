package com.IceCreamParlor.dto.repositories;

import com.IceCreamParlor.dto.entities.CaixaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CaixaRepository extends JpaRepository<CaixaEntity, UUID> {
}
