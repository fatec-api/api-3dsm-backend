package br.com.jth.servico_gestao.security;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.jth.servico_gestao.model.ClienteModel;

public interface ClienteRepository extends JpaRepository<ClienteModel, Long> {

    Optional<ClienteModel> findByEmail(String email);
    Optional<ClienteModel> findByCnpj(String cnpj);
    
    List<ClienteModel> findByAtivoTrue();

    List<ClienteModel> findByAtivoTrueAndNomeEmpresaContainingIgnoreCaseOrAtivoTrueAndCnpjContaining(
            String nomeEmpresa,
            String cnpj
    );
}