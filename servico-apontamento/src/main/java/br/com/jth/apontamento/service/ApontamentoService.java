package br.com.jth.apontamento.service;

import br.com.jth.apontamento.dto.request.ApontamentoRequestDTO;
import br.com.jth.apontamento.dto.request.ApontamentoUpdateRequestDTO;
import br.com.jth.apontamento.dto.response.ApontamentoResponseDTO;
import br.com.jth.apontamento.exception.NegocioException;
import br.com.jth.apontamento.exception.RecursoNaoEncontradoException;
import br.com.jth.apontamento.mapper.ApontamentoMapper;
import br.com.jth.apontamento.model.ApontamentoModel;
import br.com.jth.apontamento.repository.ApontamentoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerMapping;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ApontamentoService {
    private final ApontamentoRepository repository;
    private final ApontamentoMapper mapper;
    private final HandlerMapping resourceHandlerMapping;

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

        if (repository.existeConflito(dto.usuarioId(), dto.dataApontamento(), dto.horaInicio(), dto.horaFim())) {
            throw new NegocioException("Você já possui um apontamento nesse horário");
        }
        if (!validaFimAposInicio(novaEntidade.getHoraInicio(), novaEntidade.getHoraFim())) {
            throw new NegocioException("Horário de fim não pode ser igual ou anterior ao início");
        }

        double horas = calcularHorasLiquidas(novaEntidade.getHoraInicio(), novaEntidade.getHoraFim());
        novaEntidade.setHorasLiquidas(horas);
        System.out.println("ITEM ID: " + novaEntidade.getItemId());
        return mapper.toResponse(repository.save(novaEntidade));
    }

    @Transactional
    public ApontamentoResponseDTO atualizar(Long id, ApontamentoUpdateRequestDTO dto) {
        ApontamentoModel apontamentoExistente = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Apontamento não encontrado com id: " + id));

        if (dto.itemId() != null) {
            apontamentoExistente.setItemId(dto.itemId());
        }
        if (dto.usuarioId() != null) {
            apontamentoExistente.setUsuarioId(dto.usuarioId());
        }
        if (dto.dataApontamento() != null) {
            apontamentoExistente.setDataApontamento(dto.dataApontamento());
        }
        if (dto.horaInicio() != null) {
            apontamentoExistente.setHoraInicio(dto.horaInicio());
        }
        if (dto.horaFim() != null) {
            apontamentoExistente.setHoraFim(dto.horaFim());
        }
        if (dto.observacao() != null) {
            apontamentoExistente.setObservacao(dto.observacao());
        }

        apontamentoExistente.setHorasLiquidas(
                calcularHorasLiquidas(
                        apontamentoExistente.getHoraInicio(),
                        apontamentoExistente.getHoraFim()));

        LocalDateTime inicio = apontamentoExistente.getHoraInicio();
        LocalDateTime fim = apontamentoExistente.getHoraFim();
        LocalDateTime data = apontamentoExistente.getDataApontamento();
        UUID usuarioId = apontamentoExistente.getUsuarioId();

        if (repository.existeConflitoParaEdicao(usuarioId, id, data, inicio, fim)) {
            throw new NegocioException("Você já possui um apontamento nesse horário");
        }
        if (!validaFimAposInicio(apontamentoExistente.getHoraInicio(), apontamentoExistente.getHoraFim())) {
            throw new NegocioException("Horário de fim não pode ser igual ou anterior ao início");
        }

        ApontamentoModel atualizado = repository.save(apontamentoExistente);
        return mapper.toResponse(atualizado);
    }

    @Transactional
    public void excluir(Long id) {
        if (!repository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Não é possível excluir: ID não encontrado");
        }
        repository.deleteById(id);
    }

    private Double calcularHorasLiquidas(LocalDateTime horaInicio, LocalDateTime horaFim) {
        if (horaInicio == null || horaFim == null)
            return 0.0;
        long minutos = java.time.Duration.between(horaInicio, horaFim).toMinutes();
        System.out.println("HORAS LÍQUIDAS: " + minutos / 60.0);
        return minutos / 60.0;

    }

    private boolean validaFimAposInicio(LocalDateTime inicio, LocalDateTime fim) {
        return fim.isAfter(inicio);
    }

}