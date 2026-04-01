package com.example.app.service;

import com.example.app.dto.request.AllocationRequestDTO;
import com.example.app.dto.response.UsuarioResponseDTO;
import com.example.app.model.entity.ItemModel;
import com.example.app.model.entity.ProjetoModel;
import com.example.app.model.entity.UsuarioModel;
import com.example.app.repository.ItemRepository;
import com.example.app.repository.ProjetoRepository;
import com.example.app.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AlocacaoService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProjetoRepository projetoRepository;


    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarProfissionaisDisponiveis(Long projectId) {
        log.info("Buscando profissionais elegíveis para o projeto ID: {}", projectId);
        
        return usuarioRepository.findAll().stream()
            .map(user -> new UsuarioResponseDTO(
                user.getId(),
                user.getNomeUsuario(),
                user.getEmail(),
                user.getCargo() != null ? user.getCargo().name() : null,
                user.getNivelExperiencia() != null ? user.getNivelExperiencia().name() : null
            ))
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

        
        List<UsuarioModel> profissionais = usuarioRepository.findAllById(request.getProfessionalIds());
        
        if (profissionais.isEmpty()) {
            throw new RuntimeException("Erro: Nenhum profissional válido selecionado.");
        }

        item.setProfissionais(profissionais);
        itemRepository.save(item);


        for (UsuarioModel pro : profissionais) {
            if (!projeto.getEquipe().contains(pro)) {
                projeto.getEquipe().add(pro);
            }
        }
        
        projetoRepository.save(projeto);
        log.info("Alocação concluída com sucesso. {} profissionais vinculados.", profissionais.size());
    }
}