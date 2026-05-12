package br.com.jth.servico_gestao.service;

import br.com.jth.servico_gestao.dto.request.ClienteRequestDTO;
import br.com.jth.servico_gestao.dto.response.ClienteResponseDTO;
import br.com.jth.servico_gestao.mapper.ClienteMapper;
import br.com.jth.servico_gestao.model.ClienteModel;
import br.com.jth.servico_gestao.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    public ClienteResponseDTO cadastrar(ClienteRequestDTO dto) {

        String nomeEmpresa = dto.getNomeEmpresa().trim();
        String email = normalizarEmail(dto.getEmail());
        String cnpj = normalizarCnpj(dto.getCnpj());

        ClienteModel model = clienteMapper.toModel(dto);

        model.setNomeEmpresa(nomeEmpresa);
        model.setEmail(email);
        model.setCnpj(cnpj);

        return clienteMapper.toResponse(clienteRepository.save(model));
    }

    String normalizarCnpj(String cnpj) {
        if (cnpj == null) return "";
        return cnpj.replaceAll("[.\\-/\\s]", "").trim();
    }

    String normalizarEmail(String email) {
        if (email == null) return "";
        return email.trim().toLowerCase();
    }
}
