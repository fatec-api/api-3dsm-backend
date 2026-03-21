package com.example.app.service;

import com.example.app.dto.request.UsuarioRequestdto;
import com.example.app.mapper.UsuarioMapper;
import com.example.app.model.entity.UsuarioModel;
import com.example.app.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CadastroUsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioMapper usuarioMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UsuarioModel cadastrarUsuario(UsuarioRequestdto usuarioRequestdto) {
        UsuarioModel usuarioModel = usuarioMapper.toEntity(usuarioRequestdto);
        usuarioModel.setSenha(bCryptPasswordEncoder.encode(usuarioRequestdto.getSenha()));
        return  usuarioRepository.save(usuarioModel);

    }
    }
