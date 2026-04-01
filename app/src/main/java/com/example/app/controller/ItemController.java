package com.example.app.controller;

import com.example.app.dto.response.ItemResponsedto;
import com.example.app.dto.request.ItemRequestdto;
import com.example.app.service.CadastroItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/itens")
public class ItemController {

    @Autowired
    private CadastroItemService cadastroItemService;

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
}