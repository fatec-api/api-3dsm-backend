package com.example.app.service;

import com.example.app.dto.request.ApontamentoRequestDTO;
import com.example.app.dto.request.ApontamentoUpdateRequestDTO;
import com.example.app.dto.response.ApontamentoResponseDTO;
import com.example.app.exception.NegocioException;
import com.example.app.exception.RecursoNaoEncontradoException;
import com.example.app.mapper.ApontamentoMapper;
import com.example.app.model.entity.ApontamentoModel;
import com.example.app.repository.ApontamentoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service @AllArgsConstructor
public class ApontamentoService {
    private final ApontamentoRepository repository;
    private final ApontamentoMapper mapper;

    public List<ApontamentoResponseDTO> listarApontamentos() {
        return mapper.toResponseList(repository.findAll());
    }

    public ApontamentoResponseDTO buscarApontamentoPorId(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Apontamento não encontrado - id: " + id));
    }

    @Transactional
    public ApontamentoResponseDTO salvarApontamento(ApontamentoRequestDTO dto) {
        ApontamentoModel novaEntidade = mapper.toEntity(dto);
        List<ApontamentoModel> apontamentos = repository.findByUsuarioIdAndDataApontamento(novaEntidade.getId(), novaEntidade.getDataApontamento());

        validarConflitoHorario(apontamentos, dto, novaEntidade.getId());

        if(!validaFimAposInicio(novaEntidade.getHoraInicio(), novaEntidade.getHoraFim())) {
            throw new NegocioException("Horário de fim não pode ser anterior ao início");
        }

        if(novaEntidade.getPausaInicio() != null && novaEntidade.getPausaFim() != null) {
            if (!validaPausaEntreFimInicio(novaEntidade.getPausaInicio(), novaEntidade.getPausaFim(), novaEntidade.getHoraInicio(), novaEntidade.getHoraFim())) {
                throw new NegocioException("O horário de pausa deve estar compreendido entre o horário de início e fim da atividade");
        }}

        double horas = calcularHorasLiquidas(novaEntidade.getHoraInicio(), novaEntidade.getHoraFim());
        novaEntidade.setHorasLiquidas(horas);
        return mapper.toResponse(repository.save(novaEntidade));
    }

    @Transactional
    public ApontamentoResponseDTO atualizar(Long id, ApontamentoUpdateRequestDTO dto) {
        ApontamentoModel entidadeExistente = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Apontamento não encontrado com id: " + id));

        entidadeExistente.setHorasLiquidas(calcularHorasLiquidas(entidadeExistente.getHoraInicio(), entidadeExistente.getHoraFim()));

        return mapper.toResponse(repository.save(entidadeExistente));
    }

    @Transactional
    public void excluir(Long id) {
        if (!repository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Não é possível excluir: ID não encontrado");
        }
        repository.deleteById(id);
    }

    private Double calcularHorasLiquidas(LocalDateTime inicio, LocalDateTime fim) {
        if (inicio == null || fim == null) return 0.0;
        long minutos = java.time.Duration.between(inicio, fim).toMinutes();
        return minutos / 60.0;
    }

    private boolean validaFimAposInicio(LocalDateTime inicio, LocalDateTime fim) {
        return fim.isAfter(inicio);
    }

    private boolean validaPausaEntreFimInicio(LocalDateTime pausaInicio, LocalDateTime pausaFim, LocalDateTime inicio, LocalDateTime fim) {
        return pausaInicio.isAfter(inicio) && pausaFim.isBefore(fim);
    }

    private void validarConflitoHorario(List<ApontamentoModel> existentes, ApontamentoRequestDTO novo, Long idAtual) {
        if(existentes.isEmpty()) {return;}
        for (ApontamentoModel ext : existentes) {
            if (ext.getId().equals(idAtual)) continue;

            boolean sobrepoe = novo.horaInicio().isBefore(ext.getHoraFim()) && novo.horaFim().isAfter(ext.getHoraInicio());

            if (sobrepoe) {
                throw new NegocioException(String.format(
                        "Conflito de horário! Você já possui o apontamento #%d das %s às %s",
                        ext.getId(),
                        ext.getHoraInicio().toLocalTime(),
                        ext.getHoraFim().toLocalTime()
                ));
            }
        }
    }
}

