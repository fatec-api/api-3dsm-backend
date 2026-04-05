package com.example.app.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.dto.request.ApontamentoRequestDTO;
import com.example.app.dto.request.ApontamentoUpdateRequestDTO;
import com.example.app.dto.response.ApontamentoResponseDTO;
import com.example.app.service.ApontamentoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/apontamentos")
@RequiredArgsConstructor
public class ApontamentoController {
    private final ApontamentoService service;

    @GetMapping
    public ResponseEntity<List<ApontamentoResponseDTO>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(service.listarApontamentos());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ApontamentoResponseDTO>> findPorUsarioId(@PathVariable UUID usuarioId) {
        return ResponseEntity.status(HttpStatus.OK).body(service.buscarApontamentoPorUsuarioId(usuarioId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApontamentoResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.buscarApontamentoPorId(id));
    }

    @PostMapping
    public ResponseEntity<ApontamentoResponseDTO> salvar(@RequestBody @Valid ApontamentoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvarApontamento(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApontamentoResponseDTO> atualizar(@PathVariable Long id,
            @RequestBody @Valid ApontamentoUpdateRequestDTO request) {
        return ResponseEntity.status(HttpStatus.OK).body(service.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
