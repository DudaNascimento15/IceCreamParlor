package com.IceCreamParlor.repositories;

import com.IceCreamParlor.dto.entities.ProducaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProducaoRepository extends JpaRepository<ProducaoEntity, UUID> {

}
