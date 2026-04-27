package com.example.app.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.example.app.mensageria.ApontamentoProducer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerMapping;

import com.example.app.dto.request.ApontamentoRequestDTO;
import com.example.app.dto.request.ApontamentoUpdateRequestDTO;
import com.example.app.dto.response.ApontamentoResponseDTO;
import com.example.app.exception.NegocioException;
import com.example.app.exception.RecursoNaoEncontradoException;
import com.example.app.mapper.ApontamentoMapper;
import com.example.app.model.entity.ApontamentoModel;
import com.example.app.repository.ApontamentoRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ApontamentoService {
    private final ApontamentoRepository repository;
    private final ApontamentoMapper mapper;
    private final HandlerMapping resourceHandlerMapping;
    private final ApontamentoProducer producer;

    public List<ApontamentoResponseDTO> listarApontamentos() {
        return mapper.toResponseList(repository.findAll());
    }

    public ApontamentoResponseDTO buscarApontamentoPorId(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Apontamento não encontrado - id: " + id));
    }

    public List<ApontamentoResponseDTO> buscarApontamentoPorUsuarioId(UUID usuarioId) {
        List<ApontamentoModel> apontamentos = repository.findByUsuarioId(usuarioId);
        return apontamentos.stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    public ApontamentoResponseDTO salvarApontamento(ApontamentoRequestDTO dto) {
        ApontamentoModel novaEntidade = mapper.toEntity(dto);

        if (repository.existeConflito(dto.dataApontamento(), dto.horaInicio(), dto.horaFim())) {
            throw new NegocioException("Você já possui um apontamento nesse horário");
        }
        if (!validaFimAposInicio(novaEntidade.getHoraInicio(), novaEntidade.getHoraFim())) {
            throw new NegocioException("Horário de fim não pode ser anterior ao início");
        }

        if (novaEntidade.getPausaInicio() != null && novaEntidade.getPausaFim() != null) {
            if (!validaPausaEntreFimInicio(novaEntidade.getPausaInicio(), novaEntidade.getPausaFim(),
                    novaEntidade.getHoraInicio(), novaEntidade.getHoraFim())) {
                throw new NegocioException(
                        "O horário de pausa deve estar compreendido entre o horário de início e fim da atividade");
            }
        }

        double horas = calcularHorasLiquidas(novaEntidade.getHoraInicio(), novaEntidade.getHoraFim(),
                novaEntidade.getPausaInicio(), novaEntidade.getPausaFim());
        novaEntidade.setHorasLiquidas(horas);
        // mensagem publicada
        producer.publicar(dto);
        return mapper.toResponse(repository.save(novaEntidade));
    }

    @Transactional
    public ApontamentoResponseDTO atualizar(Long id, ApontamentoUpdateRequestDTO dto) {
        ApontamentoModel apontamentoExistente = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Apontamento não encontrado com id: " + id));

        if (dto.horaInicio() != null) {
            apontamentoExistente.setHoraInicio(dto.horaInicio());
        }
        if (dto.horaFim() != null) {
            apontamentoExistente.setHoraFim(dto.horaFim());
        }
        if (dto.pausaInicio() != null) {
            apontamentoExistente.setPausaInicio(dto.pausaInicio());
        }
        if (dto.pausaFim() != null) {
            apontamentoExistente.setPausaFim(dto.pausaFim());
        }

        apontamentoExistente.setHorasLiquidas(
                calcularHorasLiquidas(
                        apontamentoExistente.getHoraInicio(),
                        apontamentoExistente.getHoraFim(),
                        apontamentoExistente.getPausaInicio(),
                        apontamentoExistente.getPausaFim()));
        return mapper.toResponse(apontamentoExistente);
    }

    @Transactional
    public void excluir(Long id) {
        if (!repository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Não é possível excluir: ID não encontrado");
        }
        repository.deleteById(id);
    }

    private Double calcularHorasLiquidas(LocalDateTime horaInicio, LocalDateTime horaFim, LocalDateTime pausaInicio,
            LocalDateTime pausaFim) {
        if (horaInicio == null || horaFim == null)
            return 0.0;
        long minutos = java.time.Duration.between(horaInicio, horaFim).toMinutes();
        long minutosPausa = 0;
        if (pausaInicio == null || pausaFim == null) {
            System.out.println("⏰ HORAS LÍQUIDAS sem pausa: " + minutos / 60);
            return minutos / 60.0;
        } else {
            minutosPausa = java.time.Duration.between(pausaInicio, pausaFim).toMinutes();
            System.out.println("⏰ HORAS LÍQUIDAS com pausa: " + (minutos - minutosPausa) / 60.0);
            return (minutos - minutosPausa) / 60.0;
        }
    }

    private boolean validaFimAposInicio(LocalDateTime inicio, LocalDateTime fim) {
        return fim.isAfter(inicio);
    }

    private boolean validaPausaEntreFimInicio(LocalDateTime pausaInicio, LocalDateTime pausaFim, LocalDateTime inicio,
            LocalDateTime fim) {
        return pausaInicio.isAfter(inicio) && pausaFim.isBefore(fim);
    }

}
