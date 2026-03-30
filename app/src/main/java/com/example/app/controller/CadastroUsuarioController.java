package com.example.app.controller;

import com.example.app.dto.request.UsuarioRequestdto;
import com.example.app.dto.response.UsuarioResponsedto;
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
    public ResponseEntity<UsuarioResponsedto> cadastrarUsuario(
            @RequestBody @Valid UsuarioRequestdto usuarioRequestdto) {

        cadastroUsuarioService.cadastrarUsuario(usuarioRequestdto);

        return new ResponseEntity<>(
                new UsuarioResponsedto("Cadastro realizado com sucesso"),
                HttpStatus.CREATED
        );
    }
}