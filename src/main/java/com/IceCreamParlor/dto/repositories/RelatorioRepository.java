package com.IceCreamParlor.dto.repositories;

import com.IceCreamParlor.dto.entities.RelatorioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RelatorioRepository extends JpaRepository<RelatorioEntity, UUID> {

}
