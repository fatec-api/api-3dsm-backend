package br.com.jth.servico_gestao.mapper;

import br.com.jth.servico_gestao.dto.request.ClienteRequestDTO;
import br.com.jth.servico_gestao.dto.response.ClienteResponseDTO;
import br.com.jth.servico_gestao.model.ClienteModel;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public ClienteModel toModel(ClienteRequestDTO dto) {
        ClienteModel model = new ClienteModel();
        model.setNomeEmpresa(dto.getNomeEmpresa());
        model.setEmail(dto.getEmail());
        model.setCnpj(dto.getCnpj());
        return model;
    }

    public ClienteResponseDTO toResponse(ClienteModel model) {
        return new ClienteResponseDTO(
                model.getId(),
                model.getNomeEmpresa(),
                model.getEmail(),
                model.getCnpj(),
                model.getDataCadastro(),
                model.isAtivo()
        );
    }
}

