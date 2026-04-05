package com.example.app.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.app.model.entity.ApontamentoModel;

public interface ApontamentoRepository extends JpaRepository<ApontamentoModel, Long> {
    List<ApontamentoModel> findByUsuarioIdAndDataApontamento(Long usuarioId, LocalDateTime data);

    List<ApontamentoModel> findByUsuarioId(UUID usuarioId);

    @Query("""
                SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END
                FROM ApontamentoModel a
                WHERE a.dataApontamento = :data
                AND a.horaInicio < :novoFim
                AND a.horaFim > :novoInicio
            """)
    boolean existeConflito(LocalDateTime data, LocalDateTime novoInicio, LocalDateTime novoFim);

}