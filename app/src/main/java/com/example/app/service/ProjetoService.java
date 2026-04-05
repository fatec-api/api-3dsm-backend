package com.example.app.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.dto.request.ProjetoRequestDTO;
import com.example.app.dto.response.ProjetoResponseDTO;
import com.example.app.model.entity.ClienteModel;
import com.example.app.model.entity.ProjetoModel;
import com.example.app.model.entity.ProjetoUsuarioModel;
import com.example.app.model.entity.UsuarioModel;
import com.example.app.repository.ClienteRepository;
import com.example.app.repository.ProjetoRepository;
import com.example.app.repository.ProjetoUsuarioRepository;
import com.example.app.repository.UsuarioRepository;

@Service
public class ProjetoService {

    @Autowired
    private ProjetoRepository projetoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProjetoUsuarioRepository projetoUsuarioRepository;

    @Transactional
    public ProjetoModel criarProjeto(ProjetoRequestDTO dto) {

        if (dto.getDataFim().isBefore(dto.getDataInicio())) {
            throw new IllegalArgumentException("A data de término não pode ser anterior à data de início");
        }

        if (dto.getValorOrcamento().compareTo(new BigDecimal("100000")) > 0) {
            throw new IllegalArgumentException("O valor do orçamento excede o limite permitido de 100.000");
        }

        UsuarioModel gestor = usuarioRepository.findById(dto.getGestorId())
                .orElseThrow(() -> new IllegalArgumentException("Gestor não encontrado"));

        ClienteModel cliente = null;
        if (dto.getClienteId() != null) {
            cliente = clienteRepository.findById(dto.getClienteId())
                    .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
        }

        // Cria o projeto
        ProjetoModel projeto = new ProjetoModel();
        projeto.setNomeProjeto(dto.getNomeProjeto());
        projeto.setTipoProjeto(dto.getTipoProjeto());
        projeto.setValorOrcamento(dto.getValorOrcamento());
        projeto.setDataInicio(dto.getDataInicio());
        projeto.setDataFim(dto.getDataFim());
        projeto.setStatus(dto.getStatus());
        projeto.setGestor(gestor);
        projeto.setCliente(cliente);

        // Salva o projeto primeiro para gerar o ID
        ProjetoModel projetoSalvo = projetoRepository.save(projeto);

        // Agora vincula os profissionais na tabela correta (ProjetoUsuarioModel)
        if (dto.getProfissionaisIds() != null && !dto.getProfissionaisIds().isEmpty()) {
            for (UUID profId : dto.getProfissionaisIds()) {
                UsuarioModel profissional = usuarioRepository.findById(profId)
                        .orElseThrow(() -> new IllegalArgumentException("Profissional com ID " + profId + " não encontrado"));

                if (!profissional.isAtivo()) {
                    throw new IllegalArgumentException("O profissional " + profissional.getNomeUsuario() + " não está ativo");
                }

                // Cria a associação real
                ProjetoUsuarioModel vinculo = new ProjetoUsuarioModel();
                vinculo.setProjeto(projetoSalvo);
                vinculo.setUsuario(profissional);
                vinculo.setDataVinculo(LocalDate.now()); // Seta a data do vínculo

                projetoUsuarioRepository.save(vinculo);
            }
        }

        return projetoSalvo;
    }

    public ProjetoResponseDTO converterProjetoParaDTO(ProjetoModel projeto) {
        ProjetoResponseDTO dto = new ProjetoResponseDTO();
        dto.setId(projeto.getId());
        dto.setNomeProjeto(projeto.getNomeProjeto());
        dto.setStatus(projeto.getStatus());
        dto.setTipoProjeto(projeto.getTipoProjeto());
        return dto;
    }

    public List<ProjetoResponseDTO> listarProjetos() {
        List<ProjetoModel> projetos = projetoRepository.findAll();
        List<ProjetoResponseDTO> listaDTO = new ArrayList<>();
        for (ProjetoModel projeto : projetos) {
            listaDTO.add(converterProjetoParaDTO(projeto));
        }
        return listaDTO;
    }

    public ProjetoResponseDTO converterProjetoUnicoDTO(ProjetoModel projeto) {
        ProjetoResponseDTO dto = new ProjetoResponseDTO();
        dto.setId(projeto.getId());
        dto.setNomeProjeto(projeto.getNomeProjeto());
        dto.setTipoProjeto(projeto.getTipoProjeto());
        dto.setValorOrcamento(projeto.getValorOrcamento());
        dto.setDataInicio(projeto.getDataInicio());
        dto.setDataFim(projeto.getDataFim());
        dto.setStatus(projeto.getStatus());

        if (projeto.getGestor() != null) {
            dto.setNomeGestor(projeto.getGestor().getNomeUsuario());
        }

        if (projeto.getCliente() != null) {
            dto.setNomeCliente(projeto.getCliente().getNomeEmpresa());
        }

        return dto;
    }

    public ProjetoResponseDTO listarPorId(Long id) {
        ProjetoModel projeto = projetoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Projeto não encontrado"));
        return converterProjetoUnicoDTO(projeto);
    }
}