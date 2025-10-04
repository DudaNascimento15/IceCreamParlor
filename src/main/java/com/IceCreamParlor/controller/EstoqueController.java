package com.IceCreamParlor.controller;

import com.IceCreamParlor.dto.entities.EstoqueEntity;
import com.IceCreamParlor.dto.repositories.EstoqueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estoque")
@RequiredArgsConstructor
public class EstoqueController {

    private final EstoqueRepository estoqueRepository;

    @GetMapping
    public List<EstoqueEntity> listarTodos() {
        return estoqueRepository.findAll();
    }
}