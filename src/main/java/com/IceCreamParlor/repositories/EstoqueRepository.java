package com.IceCreamParlor.repositories;

import com.IceCreamParlor.dto.entities.EstoqueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EstoqueRepository extends JpaRepository<EstoqueEntity, UUID> {


}
