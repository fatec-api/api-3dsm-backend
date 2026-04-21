package br.com.jth.servico_gestao.controller;

import br.com.jth.servico_gestao.dto.request.UsuarioRequestDTO;
import br.com.jth.servico_gestao.dto.request.UsuarioUpdateRequestDTO;
import br.com.jth.servico_gestao.dto.response.UsuarioMessageDTO;
import br.com.jth.servico_gestao.dto.response.UsuarioResponseDTO;
import br.com.jth.servico_gestao.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @PostMapping("/cadastrar")
    public ResponseEntity<UsuarioResponseDTO> cadastrar(@RequestBody @Valid UsuarioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.cadastrarUsuario(dto));
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizar(
            @PathVariable UUID id,
            @RequestBody @Valid UsuarioUpdateRequestDTO dto) {
        return ResponseEntity.ok(usuarioService.alterarUsuario(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(usuarioService.pegarUsuario(id));
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        usuarioService.excluirUsuario(id);
        return ResponseEntity.noContent().build();
    }
}