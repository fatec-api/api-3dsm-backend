package br.com.jth.auditoria.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.jth.auditoria.dto.response.AuditoriaLogResponseDTO;
import br.com.jth.auditoria.model.AuditoriaLog;
import br.com.jth.auditoria.repository.AuditoriaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuditoriaService {

    private final AuditoriaRepository auditoriaRepository;

    public Page<AuditoriaLogResponseDTO> listar(Pageable pageable) {
        return auditoriaRepository.findAll(pageable).map(this::toResponse);
    }

    public List<AuditoriaLogResponseDTO> buscarPorUsuario(String id) {
        return auditoriaRepository.findByUsuarioId(id)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private AuditoriaLogResponseDTO toResponse(AuditoriaLog log) {
        Map<String, Object> detalhes = log.getDetalhes();

        Long itemId = detalhes != null && detalhes.get("itemId") != null
                ? Long.valueOf(detalhes.get("itemId").toString())
                : null;

        UUID usuarioId = log.getUsuarioId() != null
                ? UUID.fromString(log.getUsuarioId())
                : null;

        LocalDateTime dataApontamento = parseDate(detalhes != null ? detalhes.get("dataApontamento") : null);
        LocalDateTime horaInicio = parseDate(detalhes != null ? detalhes.get("horaInicio") : null);
        LocalDateTime horaFim = parseDate(detalhes != null ? detalhes.get("horaFim") : null);

        Double horasLiquidas = detalhes != null && detalhes.get("horasLiquidas") != null
                ? Double.valueOf(detalhes.get("horasLiquidas").toString())
                : null;

        String observacao = detalhes != null ? (String) detalhes.get("observacao") : null;

        return new AuditoriaLogResponseDTO(
                log.getId(),
                log.getCriadoEm(),
                itemId,
                usuarioId,
                dataApontamento,
                horaInicio,
                horaFim,
                horasLiquidas,
                observacao);
    }

    private LocalDateTime parseDate(Object value) {
        if (value == null)
            return null;
        try {
            return LocalDateTime.parse(value.toString());
        } catch (Exception e) {
            try {
                return LocalDateTime.ofInstant(
                        new java.util.Date(value.toString()).toInstant(),
                        java.time.ZoneId.of("UTC"));
            } catch (Exception ex) {
                return null;
            }
        }
    }
}