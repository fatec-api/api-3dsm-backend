package com.example.app.controller;

import com.example.app.dto.ProjetoUsuarioDTO;
import com.example.app.model.entity.ProjetoUsuarioModel;
import com.example.app.service.ProjetoUsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProjetoUsuarioController {

    @Autowired
    private ProjetoUsuarioService service;

    @PostMapping("/associar-projeto")
    public ResponseEntity<ProjetoUsuarioModel> associar(@RequestBody @Valid ProjetoUsuarioDTO dto) {
        ProjetoUsuarioModel associacao = service.associar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(associacao);
    }
}
