package br.com.jth.servico_gestao.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.jth.servico_gestao.dto.response.ClienteResponseDTO;
import br.com.jth.servico_gestao.repository.ClienteRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ClienteService {
    private final ClienteRepository clienteRepository;

    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> listarClientes() {
        return clienteRepository.findAll().stream()
                .map(cliente -> new ClienteResponseDTO(
                        cliente.getId().toString(),
                        cliente.getNomeEmpresa(),
                        cliente.getCnpj(),
                        cliente.getEmail(),
                        cliente.isAtivo()
                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ClienteResponseDTO buscarPorId(Long id) {
        return clienteRepository.findById(id).map(cliente -> new ClienteResponseDTO(
                cliente.getId().toString(),
                cliente.getNomeEmpresa(),
                cliente.getCnpj(),
                cliente.getEmail(),
                cliente.isAtivo()
        )).orElse(null);
    }
}
