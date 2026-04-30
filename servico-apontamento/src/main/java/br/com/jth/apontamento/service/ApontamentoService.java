package br.com.jth.apontamento.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import br.com.jth.apontamento.dto.response.ApontamentoEventAuditoria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerMapping;

import br.com.jth.apontamento.dto.request.ApontamentoRequestDTO;
import br.com.jth.apontamento.dto.request.ApontamentoUpdateRequestDTO;
import br.com.jth.apontamento.dto.response.ApontamentoAvaliacaoDTO;
import br.com.jth.apontamento.dto.response.ApontamentoResponseDTO;
import br.com.jth.apontamento.dto.response.ItemResponseDTO;
import br.com.jth.apontamento.enums.ApontamentoStatus;
import br.com.jth.apontamento.exception.NegocioException;
import br.com.jth.apontamento.exception.RecursoNaoEncontradoException;
import br.com.jth.apontamento.mapper.ApontamentoMapper;
import br.com.jth.apontamento.mensageria.ApontamentoEventProducer;
import br.com.jth.apontamento.mensageria.projeto.ProjetoQueryProducer;
import br.com.jth.apontamento.model.ApontamentoModel;
import br.com.jth.apontamento.repository.ApontamentoRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ApontamentoService {
    private final ApontamentoRepository repository;
    private final ApontamentoMapper mapper;
    private final ApontamentoEventProducer apontamentoEventProducer;
    private final HandlerMapping resourceHandlerMapping;
    private final ProjetoQueryProducer projetoQueryProducer;

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
        ApontamentoModel apontamento = mapper.toEntity(dto);

        if (repository.existeConflito(dto.usuarioId(), dto.dataApontamento(), dto.horaInicio(), dto.horaFim())) {
            throw new NegocioException("Você já possui um apontamento nesse horário");
        }
        if (!validaFimAposInicio(apontamento.getHoraInicio(), apontamento.getHoraFim())) {
            throw new NegocioException("Horário de fim não pode ser igual ou anterior ao início");
        }

        double horas = calcularHorasLiquidas(apontamento.getHoraInicio(), apontamento.getHoraFim());
        apontamento.setHorasLiquidas(horas);
        System.out.println("ITEM ID: " + apontamento.getItemId());

        ApontamentoModel salvo = repository.save(apontamento);
        apontamentoEventProducer.publicarApontamentoCriado(salvo);
        apontamentoEventProducer.publicarApontamentoAuditoria(salvo);


        return mapper.toResponse(salvo);
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

    @Transactional
    public ApontamentoResponseDTO avaliar(Long id, ApontamentoAvaliacaoDTO dto) {
        ApontamentoModel apontamento = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Apontamento não encontrado com id: " + id));

        if (apontamento.getStatus() != ApontamentoStatus.PENDENTE) {
            throw new NegocioException("Apontamento já foi avaliado");
        }
        if (dto.status() == ApontamentoStatus.REPROVADO &&
                (dto.justificativaReprovacao() == null || dto.justificativaReprovacao().isBlank())) {
            throw new NegocioException("Justificativa é obrigatória ao reprovar um apontamento");
        }
        if (dto.status() == ApontamentoStatus.PENDENTE) {
            throw new NegocioException("Não é possível avaliar um apontamento como pendente");
        }

        apontamento.setStatus(dto.status());
        apontamento.setJustificativaReprovacao(dto.justificativaReprovacao());

        ApontamentoModel salvo = repository.save(apontamento);
        apontamentoEventProducer.publicarApontamentoAvaliado(salvo);

        
        return mapper.toResponse(salvo);
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

    public List<ApontamentoResponseDTO> buscarApontamentoPendentePorProjetoId(Long projetoId) {
        
        // 1. Busca os itens que pertencem a esse projeto lá no microsserviço de Gestão
        List<ItemResponseDTO> itensDoProjeto = projetoQueryProducer.buscarItensParaApontamento(projetoId);

        // Se o projeto não existir ou não tiver itens, já retornamos vazio para evitar select desnecessário
        if (itensDoProjeto.isEmpty()) {
            return List.of();
        }

        // 2. Extrai apenas os IDs dos itens para facilitar a query (o famoso "select doido")
        List<Long> itensIds = itensDoProjeto.stream()
                .map(ItemResponseDTO::getId)
                .collect(Collectors.toList());

        // 3. Faz o select no banco local de Apontamentos
        // Aqui buscamos apontamentos que estejam ligados a esses itens e que tenham status PENDENTE
        List<ApontamentoModel> apontamentosPendentes = repository
                .findByItemIdInAndStatus(itensIds, ApontamentoStatus.PENDENTE);

        // 4. Converte para DTO e retorna
        return apontamentosPendentes.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

}