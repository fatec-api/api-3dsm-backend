package com.example.app.service;

import com.example.app.exception.RecursoNaoEncontradoExcecao;
import com.example.app.model.entity.ApontamentoModel;
import com.example.app.model.entity.UsuarioModel;
import com.example.app.repository.ApontamentoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @AllArgsConstructor
public class ApontamentoService {
    private final ApontamentoRepository repository;

    public List<ApontamentoModel> listarApontamentos() {
        return repository.findAll();
    }

    public ApontamentoModel buscarApontamentoPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoExcecao("Não foi possível localizar o apontamento com id: " + id));
    }

    public ApontamentoModel salvarApontamento(ApontamentoModel ap) {
        return repository.save(ap);
    }

    public ApontamentoModel atualizarApontamento(Long id, ApontamentoRequestDTO dto) {
        ApontamentoModel ap = buscarApontamentoPorId(id);
        if(dto.getItem() != null) {
            ap.setItem(dto.getItem());
        }
        if(dto.getUsuario() != null) {
            ap.setUsuario(dto.getUsuario());
        }
        if(dto.getHoraInicio() != null) {
            ap.setHoraInicio(dto.getHoraInicio());
        }
        if(dto.horaFim() != null) {
            ap.setHoraFim(dto.getHoraFim());
        }
        if(dto.getObservacao() != null) {
            ap.setObservacao(dto.getObservacao());
        }
        ap.setHorasLiquidas(ap.getHoraFim().minus (ap.getHoraInicio());
    }
}
