package com.example.app.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.app.dto.request.ItemRequestdto;
import com.example.app.dto.response.ItemResponsedto;
import com.example.app.service.CadastroItemService;
import com.example.app.service.ListagemItemService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/itens")
public class ItemController {

    @Autowired
    private CadastroItemService cadastroItemService;

    @Autowired
    private ListagemItemService listagemItemService;

    @PreAuthorize("hasRole('GESTOR')")
    @PostMapping("/cadastro/item")
    public ResponseEntity<Map<String, Object>> cadastrarItem(
            @RequestBody @Valid ItemRequestdto itemRequestdto) {

        ItemResponsedto resposta = cadastroItemService.cadastrarItem(itemRequestdto);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "mensagem", "Item criado com sucesso",
                "item", resposta
        ));
    }

    @PreAuthorize("hasAnyRole('GESTOR', 'DESENVOLVEDOR')")
    @GetMapping("/projeto/{projetoId}")
    public ResponseEntity<List<ItemResponsedto>> listarItensPorProjeto(@PathVariable Long projetoId) {
        List<ItemResponsedto> itens = listagemItemService.listarPorProjeto(projetoId);

        if (itens.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(itens);
    }

    @PreAuthorize("hasAnyRole('GESTOR', 'DESENVOLVEDOR')")
    @GetMapping("/profissional/{usuarioId}")
    public ResponseEntity<List<ItemResponsedto>> listarItensPorProfissional(@PathVariable UUID usuarioId) {
        List<ItemResponsedto> itens = listagemItemService.listarPorProfissional(usuarioId);

        if (itens.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(itens);
    }
}