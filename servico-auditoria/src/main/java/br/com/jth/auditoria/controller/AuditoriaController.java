package br.com.jth.auditoria.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jth.auditoria.model.AuditoriaLog;
import br.com.jth.auditoria.repository.AuditoriaRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auditorias")
@RequiredArgsConstructor
public class AuditoriaController {

    private final AuditoriaRepository auditoriaRepository;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<AuditoriaLog>> listar(
            @PageableDefault(sort = "timestamp", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(auditoriaRepository.findAll(pageable));
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<?> buscarPorUsuario(@PathVariable String id) {
        return ResponseEntity.ok(auditoriaRepository.findByUsuarioId(id));
    }
}
