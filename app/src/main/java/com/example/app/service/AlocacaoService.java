package com.example.app.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.dto.request.AllocationRequestDTO;
import com.example.app.dto.response.UsuarioResponseDTO;
import com.example.app.model.entity.ItemModel;
import com.example.app.model.entity.ProjetoModel;
import com.example.app.model.entity.ProjetoUsuarioModel;
import com.example.app.model.entity.UsuarioModel;
import com.example.app.repository.ItemRepository;
import com.example.app.repository.ProjetoRepository;
import com.example.app.repository.ProjetoUsuarioRepository;
import com.example.app.repository.UsuarioRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AlocacaoService {

        @Autowired
        private ItemRepository itemRepository;

        @Autowired
        private UsuarioRepository usuarioRepository;

        @Autowired
        private ProjetoRepository projetoRepository;

        @Autowired
        private ProjetoUsuarioRepository projetoUsuarioRepository;

        @Transactional(readOnly = true)
        public List<UsuarioResponseDTO> listarProfissionaisAtivos() {
                // log.info("Buscando todos os profissionais ativos no sistema...");

                return usuarioRepository.findByAtivoTrueAndCargo(UsuarioModel.Cargo.Profissional).stream()
                        .map(user -> new UsuarioResponseDTO(
                                user.getId(),
                                user.getNomeUsuario(),
                                user.getEmail(),
                                user.getCargo() != null ? user.getCargo().name() : null,
                                user.getNivelExperiencia() != null ? user.getNivelExperiencia() : null,
                                user.getValorHora()))
                        .collect(Collectors.toList());
        }

        @Transactional(readOnly = true)
        public List<UsuarioResponseDTO> listarUsuariosAtivos() {
                return usuarioRepository.findByAtivoTrue().stream()
                        .map(user -> new UsuarioResponseDTO(
                                user.getId(),
                                user.getNomeUsuario(),
                                user.getEmail(),
                                user.getCargo() != null ? user.getCargo().name() : null,
                                user.getNivelExperiencia() != null ? user.getNivelExperiencia() : null,
                                user.getValorHora()))
                        .collect(Collectors.toList());
        }

        @Transactional(readOnly = true)
        public List<UsuarioResponseDTO> listarProfissionaisDoProjeto(Long projectId) {
                log.info("Buscando profissionais vinculados ao projeto ID: {}", projectId);

                List<ProjetoUsuarioModel> vinculos = projetoUsuarioRepository
                        .findByProjetoIdAndDataDesvinculoIsNull(projectId);

                return vinculos.stream()
                        .map(vinculo -> {
                                UsuarioModel user = vinculo.getUsuario(); // Extrai o usuário do vínculo
                                return new UsuarioResponseDTO(
                                        user.getId(),
                                        user.getNomeUsuario(),
                                        user.getEmail(),
                                        user.getCargo() != null ? user.getCargo().name() : null,
                                        user.getNivelExperiencia() != null ? user.getNivelExperiencia()
                                                : null,
                                        user.getValorHora());
                        })
                        .collect(Collectors.toList());
        }

        @Transactional
        public void vincularProfissionais(AllocationRequestDTO request) {
                log.info("Iniciando alocação para o Item ID: {} no Projeto ID: {}",
                        request.getItemId(), request.getProjectId());

                ItemModel item = itemRepository.findById(request.getItemId())
                        .orElseThrow(() -> new RuntimeException("Erro: Item não encontrado."));

                ProjetoModel projeto = projetoRepository.findById(request.getProjectId())
                        .orElseThrow(() -> new RuntimeException("Erro: Projeto não encontrado."));

                UUID profissionalId = request.getProfessionalIds().get(0);

                UsuarioModel profissional = usuarioRepository.findById(profissionalId)
                        .orElseThrow(() -> new RuntimeException("Erro: Profissional não encontrado."));

                item.setUsuarioModel(profissional);
                itemRepository.save(item);

                if (!projetoUsuarioRepository.existsByProjetoAndUsuario(projeto, profissional)) {
                        ProjetoUsuarioModel novoVinculo = new ProjetoUsuarioModel();
                        novoVinculo.setProjeto(projeto);
                        novoVinculo.setUsuario(profissional);
                        novoVinculo.setDataVinculo(LocalDate.now());
                        projetoUsuarioRepository.save(novoVinculo);

                        log.info("Profissional {} vinculado ao item {} com sucesso.", profissional.getNomeUsuario(),
                                item.getDescricao());
                }

        }
}