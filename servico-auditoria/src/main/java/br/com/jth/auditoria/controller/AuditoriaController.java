package br.com.jth.auditoria.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jth.auditoria.dto.response.AuditoriaLogResponseDTO;
import br.com.jth.auditoria.service.AuditoriaService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auditorias")
@RequiredArgsConstructor
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    @GetMapping
    public ResponseEntity<List<AuditoriaLogResponseDTO>> listar(
            @PageableDefault(sort = "timestamp", direction = Sort.Direction.DESC) Pageable pageable) {

        return ResponseEntity.ok(auditoriaService.listar(pageable).getContent());
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<AuditoriaLogResponseDTO>> buscarPorUsuario(@PathVariable String id) {

        return ResponseEntity.ok(auditoriaService.buscarPorUsuario(id));
    }
}