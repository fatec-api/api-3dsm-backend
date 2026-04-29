package br.com.jth.auditoria.model;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "logs_auditoria")
public class AuditoriaLog {

    @Id
    private String id;
    private String correlationId;
    private LocalDateTime criadoEm;
    private String servicoOrigem;
    private String usuarioId;
    private String tipoAcao;
    private Map<String, Object> detalhes;
}