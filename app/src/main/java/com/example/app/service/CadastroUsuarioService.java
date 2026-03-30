package com.example.app.service;

import com.example.app.dto.request.UsuarioRequestdto;
import com.example.app.exception.EmailJaCadastradoException;
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

    public void cadastrarUsuario(UsuarioRequestdto usuarioRequestdto) {

        if (usuarioRepository.existsByEmail(usuarioRequestdto.getEmail())) {
            throw new EmailJaCadastradoException("E-mail informado já está em uso.");
        }

        UsuarioModel usuarioModel = usuarioMapper.toEntity(usuarioRequestdto);
        usuarioModel.setSenha(bCryptPasswordEncoder.encode(usuarioRequestdto.getSenha()));
        usuarioModel.setAtivo(true);

        usuarioRepository.save(usuarioModel);
    }
}