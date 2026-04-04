package com.example.app.service;

import com.example.app.dto.ProjetoUsuarioDTO;
import com.example.app.model.entity.ProjetoModel;
import com.example.app.model.entity.ProjetoUsuarioModel;
import com.example.app.model.entity.UsuarioModel;
import com.example.app.repository.ProjetoRepository;
import com.example.app.repository.ProjetoUsuarioRepository;
import com.example.app.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProjetoUsuarioService {

    @Autowired
    private ProjetoUsuarioRepository projetoUsuarioRepository;

    @Autowired
    private ProjetoRepository projetoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public void associar(ProjetoUsuarioDTO dto) {

        ProjetoModel projeto = projetoRepository.findById(dto.getProjetoId()).orElseThrow(() -> new RuntimeException("Projeto não encontrado."));

        List<UsuarioModel> usuarios = usuarioRepository.findAllById(dto.getUsuarioId());

        if (usuarios.isEmpty()) {
            throw new RuntimeException("Nenhum usuário válido encontrado.");
        }

        for (UsuarioModel usuario : usuarios) {
            if (!usuario.isAtivo()) {
                throw new RuntimeException("Este usuário não está ativo e não pode ser associado ao projeto.");
            }
            if (projetoUsuarioRepository.existsByProjetoAndUsuario(projeto, usuario)) {
                throw new RuntimeException("Usuário já associado a este projeto.");
            }

            ProjetoUsuarioModel associacao = new ProjetoUsuarioModel();
            associacao.setProjeto(projeto);
            associacao.setUsuario(usuario);
            associacao.setDataVinculo(LocalDate.now());

            projetoUsuarioRepository.save(associacao);
        }
    }
}

