package com.example.app.controller;

import com.example.app.dto.request.UsuarioRequestdto;
import com.example.app.dto.response.UsuarioResponsedto;
import com.example.app.model.entity.UsuarioModel;
import com.example.app.service.CadastroUsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CadastroUsuarioController {

    @Autowired
    CadastroUsuarioService cadastroUsuarioService;

    @PostMapping("/cadastrar/usuario")
    public ResponseEntity<UsuarioResponsedto> cadastrarUsuario(@RequestBody @Valid UsuarioRequestdto usuarioRequestdto){
        UsuarioModel usuarioModel = cadastroUsuarioService.cadastrarUsuario(usuarioRequestdto);


        UsuarioResponsedto resposta = new UsuarioResponsedto(usuarioModel.getNomeUsuario(), usuarioModel.getEmail(), usuarioModel.getSenha(), usuarioModel.getValorHora(), usuarioModel.getCargo(), usuarioModel.isAtivo(), usuarioModel.getCriado_em());
        return new ResponseEntity<>(resposta, HttpStatus.CREATED);
    }

}
