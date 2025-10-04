package com.IceCreamParlor.controller;

import com.IceCreamParlor.dto.entities.RelatorioEntity;
import com.IceCreamParlor.dto.repositories.RelatorioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/relatorio")
@RequiredArgsConstructor
public class RelatorioController {

    private final RelatorioRepository relatorioRepository;

    @GetMapping
    public List<RelatorioEntity> listarTodos() {
        return relatorioRepository.findAll();
    }
}