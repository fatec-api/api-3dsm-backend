package com.example.app.repository;

import com.example.app.model.entity.ApontamentoModel;
import com.example.app.model.entity.ClienteModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApontamentoRepository extends JpaRepository<ApontamentoModel, Long> {

}
