package br.com.jth.auditoria.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.jth.auditoria.model.AuditoriaLog;

public interface AuditoriaRepository extends MongoRepository<AuditoriaLog, String> {
    List<AuditoriaLog> findByUsuarioId(String usuarioId);

    List<AuditoriaLog> findByCorrelationId(String correlationId);
}