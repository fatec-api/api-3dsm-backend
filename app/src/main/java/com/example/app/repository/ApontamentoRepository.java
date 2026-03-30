package com.example.app.repository;

import com.example.app.model.entity.ApontamentoModel;
import com.example.app.model.entity.ClienteModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ApontamentoRepository extends JpaRepository<ApontamentoModel, Long> {
    List<ApontamentoModel> findByUsuarioIdAndDataApontamento(Long usuarioId, LocalDateTime data);

}
