package com.example.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.dto.request.ProjetoRequestDTO;
import com.example.app.dto.response.ProjetoResponseDTO;
import com.example.app.model.entity.ProjetoModel;
import com.example.app.service.ProjetoService;

import jakarta.validation.Valid;

@RestController
public class ProjetoController {

    @Autowired
    ProjetoService projetoService;

    @PostMapping("/cadastrar/projeto")
    public ResponseEntity<ProjetoResponseDTO> cadastrarProjeto(
            @RequestBody @Valid ProjetoRequestDTO projetoRequestDTO) {

        ProjetoModel salvo = projetoService.criarProjeto(projetoRequestDTO);

        ProjetoResponseDTO resposta = new ProjetoResponseDTO();
        resposta.setId(salvo.getId());
        resposta.setNomeProjeto(salvo.getNomeProjeto());
        resposta.setTipoProjeto(salvo.getTipoProjeto());
        resposta.setValorOrcamento(salvo.getValorOrcamento());
        resposta.setDataInicio(salvo.getDataInicio());
        resposta.setDataFim(salvo.getDataFim());
        resposta.setStatus(salvo.getStatus());
        resposta.setNomeGestor(salvo.getGestor().getNomeUsuario());
        resposta.setNomeCliente(salvo.getCliente() != null ? salvo.getCliente().getNomeEmpresa() : null);

        return new ResponseEntity<>(resposta, HttpStatus.CREATED);
    }

    @GetMapping("/listar/projetos")
    public ResponseEntity<List<ProjetoResponseDTO>> listarProjetos() {
        return ResponseEntity.ok(projetoService.listarProjetos());
    }

    @GetMapping("/listar/projetos/{id}")
    public ResponseEntity<ProjetoResponseDTO> buscarPorId(@PathVariable Long id) {
        ProjetoResponseDTO projeto = projetoService.listarPorId(id);
        return ResponseEntity.ok(projeto);
    }
}
