from locust import HttpUser, task, between
import random

class KeycloakAuthLoadTest(HttpUser):
    # Tempo de espera entre cada requisição de um mesmo usuário (1 a 3 segundos)
    # Como queremos estressar o login, mantemos o tempo curto.
    wait_time = between(1, 3)

    @task
    def login_with_password(self):
        # Sorteia um usuário aleatório da nossa base de 1 a 1000
        user_id = random.randint(1, 1000)
        username = f"loaduser_{user_id}"

        # Payload do OIDC (padrão x-www-form-urlencoded)
        payload = {
            "client_id": "spring-api-client", # Nome do client que criamos antes
            "username": username,
            "password": "senha123",
            "grant_type": "password"
        }

        headers = {
            "Content-Type": "application/x-www-form-urlencoded"
        }

        # Faz a requisição POST para o Token Endpoint
        # O catch_response=True permite validarmos o resultado manualmente
        with self.client.post(
            "/realms/spring-boot-realm/protocol/openid-connect/token",
            data=payload,
            headers=headers,
            catch_response=True,
            name="Gerar Token JWT" # Nome que aparecerá no relatório
        ) as response:

            # Validação do teste:
            if response.status_code == 200 and "access_token" in response.text:
                response.success()
            elif response.status_code == 401:
                response.failure(f"Credenciais inválidas para {username}")
            else:
                response.failure(f"Erro no servidor: {response.status_code} - {response.text}")

 # locust -f locustfile.py