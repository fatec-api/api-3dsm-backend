package com.example.app.controller;

import com.example.app.dto.request.ItemRequestdto;
import com.example.app.dto.response.ItemResponsedto;
import com.example.app.model.entity.ItemModel;
import com.example.app.service.CadastroItemService;
import jakarta.validation.Valid;
import jdk.jfr.Registered;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ItemController {

    @Autowired
    CadastroItemService cadastroItemService;

    @PostMapping("cadastro/item")
    public ResponseEntity<ItemResponsedto> cadastrarItem(@RequestBody @Valid ItemRequestdto itemRequestdto){
        ItemModel itemModel = cadastroItemService.cadastrarItem(itemRequestdto);

        ItemResponsedto resposta = new ItemResponsedto(itemModel.getCódigo(), itemModel.getDescricao(), itemModel.getDataAtribuicao().atStartOfDay(), itemModel.getPrevisaoHoras(), itemModel.getNivelAtividade(), itemModel.getUsuarioModel(), itemModel.getProjetoModel());
        return new ResponseEntity<>(resposta, HttpStatus.OK);
    }
}
