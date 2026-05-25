package br.com.jth.servico_gestao.mensageria.apontamento;

import br.com.jth.servico_gestao.config.RabbitMQConfig;
import br.com.jth.servico_gestao.enums.usuario.Cargo;
import br.com.jth.servico_gestao.model.UsuarioModel;
import br.com.jth.servico_gestao.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class UsuarioEventConsumer {

    private final UsuarioRepository usuarioRepository;

    @RabbitListener(queues = RabbitMQConfig.KEYCLOAK_USUARIO_CRIADO_QUEUE)
    public void onUsuarioCriado(Map<String, Object> event) {
        try {
            String keycloakId = event.get("id").toString();
            log.info("Usuário criado no Keycloak recebido: {}", keycloakId);

            if (usuarioRepository.existsById(UUID.fromString(keycloakId))) {
                log.warn("Usuário {} já existe no banco, ignorando.", keycloakId);
                return;
            }

            UsuarioModel usuario = new UsuarioModel();
            usuario.setId(UUID.fromString(keycloakId));
            usuario.setNomeUsuario(event.getOrDefault("username", "").toString());
            usuario.setEmail(event.getOrDefault("email", "").toString());
            usuario.setValorHora(BigDecimal.ZERO);
            //usuario.setCargos(Set.of(Cargo.COLABORADOR));
            usuario.setAtivo(true);

            usuarioRepository.save(usuario);
            log.info("Usuário {} salvo no banco com sucesso.", keycloakId);

        } catch (Exception e) {
            log.error("Erro ao processar evento de criação de usuário: {}", e.getMessage(), e);
        }
    }

    @RabbitListener(queues = RabbitMQConfig.KEYCLOAK_USUARIO_ATUALIZADO_QUEUE)
    public void onUsuarioAtualizado(Map<String, Object> event) {
        try {
            UUID keycloakId = UUID.fromString(event.get("id").toString());
            log.info("Atualização de usuário Keycloak recebida: {}", keycloakId);

            usuarioRepository.findById(keycloakId).ifPresentOrElse(usuario -> {
                if (event.containsKey("username")) {
                    usuario.setNomeUsuario(event.get("username").toString());
                }
                if (event.containsKey("email")) {
                    usuario.setEmail(event.get("email").toString());
                }
                usuarioRepository.save(usuario);
                log.info("Usuário {} atualizado no banco.", keycloakId);
            }, () -> log.warn("Usuário {} não encontrado para atualização.", keycloakId));

        } catch (Exception e) {
            log.error("Erro ao processar evento de atualização de usuário: {}", e.getMessage(), e);
        }
    }

    @RabbitListener(queues = RabbitMQConfig.KEYCLOAK_USUARIO_DELETADO_QUEUE)
    public void onUsuarioDeletado(Map<String, Object> event) {
        try {
            UUID keycloakId = UUID.fromString(event.get("id").toString());
            log.info("Deleção de usuário Keycloak recebida: {}", keycloakId);

            usuarioRepository.findById(keycloakId).ifPresentOrElse(usuario -> {
                usuario.setAtivo(false); // soft delete
                usuarioRepository.save(usuario);
                log.info("Usuário {} desativado no banco.", keycloakId);
            }, () -> log.warn("Usuário {} não encontrado para deletar.", keycloakId));

        } catch (Exception e) {
            log.error("Erro ao processar evento de deleção de usuário: {}", e.getMessage(), e);
        }
    }
}