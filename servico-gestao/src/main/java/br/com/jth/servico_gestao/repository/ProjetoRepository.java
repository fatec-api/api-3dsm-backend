package br.com.jth.servico_gestao.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.jth.servico_gestao.model.ProjetoModel;

public interface ProjetoRepository extends JpaRepository<ProjetoModel, Long> {
    List<ProjetoModel> findByGestorId(UUID gestorId);
}