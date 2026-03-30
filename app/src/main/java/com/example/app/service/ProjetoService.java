package com.example.app.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.app.dto.request.ProjetoRequestDTO;
import com.example.app.dto.response.ProjetoResponseDTO;
import com.example.app.model.entity.ClienteModel;
import com.example.app.model.entity.ProjetoModel;
import com.example.app.model.entity.UsuarioModel;
import com.example.app.repository.ClienteRepository;
import com.example.app.repository.ProjetoRepository;
import com.example.app.repository.UsuarioRepository;

@Service
public class ProjetoService {

    @Autowired
    private ProjetoRepository projetoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    public ProjetoModel criarProjeto(ProjetoRequestDTO dto) {

        if (dto.getDataFim().isBefore(dto.getDataInicio())) {
            throw new IllegalArgumentException("A data de término não pode ser anterior à data de início");
        }

        UsuarioModel gestor = usuarioRepository.findById(dto.getGestorId())
                .orElseThrow(() -> new IllegalArgumentException("Gestor não encontrado"));

        UsuarioModel profissional = null;
        if (dto.getProfissionalAlocadoId() != null) {
            profissional = usuarioRepository.findById(dto.getProfissionalAlocadoId())
                    .orElseThrow(() -> new IllegalArgumentException("Profissional alocado não encontrado"));

            if (!profissional.isAtivo()) {
                throw new IllegalArgumentException("O profissional alocado não está ativo");
            }
        }

        ClienteModel cliente = null;
        if (dto.getClienteId() != null) {
            cliente = clienteRepository.findById(dto.getClienteId())
                    .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
        }

        if (dto.getValorOrcamento().compareTo(new BigDecimal("100000")) > 0) {
            throw new IllegalArgumentException("Valor muito alto");
        }

        ProjetoModel projeto = new ProjetoModel();
        projeto.setNomeProjeto(dto.getNomeProjeto());
        projeto.setTipoProjeto(dto.getTipoProjeto());
        projeto.setValorOrcamento(dto.getValorOrcamento());
        projeto.setDataInicio(dto.getDataInicio());
        projeto.setDataFim(dto.getDataFim());
        projeto.setStatus(dto.getStatus());
        projeto.setGestor(gestor);
        projeto.setProfissionalAlocado(profissional);
        projeto.setCliente(cliente);

        return projetoRepository.save(projeto);
    }

    public ProjetoResponseDTO converterProjetoParaDTO(ProjetoModel projeto) {

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

        dto.setNomeProjeto(projeto.getNomeProjeto());
        dto.setTipoProjeto(projeto.getTipoProjeto());
        dto.setStatus(projeto.getStatus());

        return dto;
    }

    public ProjetoResponseDTO listarPorId(Long id) {

        ProjetoModel projeto = projetoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Projeto não encontrado"));

        return converterProjetoUnicoDTO(projeto);
    }

}
