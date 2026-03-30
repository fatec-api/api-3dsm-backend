package com.example.app.service;

import com.example.app.dto.ProjetoUsuarioDTO;
import com.example.app.model.entity.ProjetoModel;
import com.example.app.model.entity.ProjetoUsuarioModel;
import com.example.app.model.entity.UsuarioModel;
import com.example.app.repository.ProjetoRepository;
import com.example.app.repository.ProjetoUsuarioRepository;
import com.example.app.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ProjetoUsuarioService {

    @Autowired
    private ProjetoUsuarioRepository projetoUsuarioRepository;

    @Autowired
    private ProjetoRepository projetoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public ProjetoUsuarioModel associar(ProjetoUsuarioDTO dto) {

        ProjetoModel projeto = projetoRepository.findById(dto.getProjetoId()).orElseThrow(() -> new RuntimeException("Projeto não encontrado."));

        UsuarioModel usuario = usuarioRepository.findById(dto.getUsuarioId()).orElseThrow(() -> new RuntimeException("Usuario não encontrado."));

        // evitar duplicidade
        if (projetoUsuarioRepository.existsByProjetoAndUsuario(projeto, usuario)) {
            throw new RuntimeException("Usuário já associado a este projeto.");
        }

        // validar usuario ativo
        if (!usuario.isAtivo()) {
            throw new RuntimeException("Este usuário não está ativo e não pode ser associado ao projeto.");
        }

        // criar vinculo/associacao
        ProjetoUsuarioModel associacao = new ProjetoUsuarioModel();
        associacao.setProjeto(projeto);
        associacao.setUsuario(usuario);
        associacao.setDataVinculo(LocalDate.now());

        return projetoUsuarioRepository.save(associacao);

    }
}

