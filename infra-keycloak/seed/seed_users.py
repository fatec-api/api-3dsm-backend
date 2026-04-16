import requests

# Configurações do seu Keycloak local
KEYCLOAK_URL = "http://localhost:8081"
REALM = "java-the-hutt"
ADMIN_USER = "admin"
ADMIN_PASS = "admin"
QTD_USERS = 1000

def get_admin_token():
    url = f"{KEYCLOAK_URL}/realms/master/protocol/openid-connect/token"
    payload = {
        "client_id": "admin-cli",
        "username": ADMIN_USER,
        "password": ADMIN_PASS,
        "grant_type": "password"
    }
    response = requests.post(url, data=payload)
    return response.json()["access_token"]

def create_users():
    token = get_admin_token()
    headers = {
        "Authorization": f"Bearer {token}",
        "Content-Type": "application/json"
    }

    print(f"Criando {QTD_USERS} usuários no realm '{REALM}'...")

    for i in range(1, QTD_USERS + 1):
        username = f"loaduser_{i}"
        payload = {
            "username": username,
            "enabled": True,
            "credentials": [{
                "type": "password",
                "value": "senha123",
                "temporary": False
            }]
        }

        url = f"{KEYCLOAK_URL}/admin/realms/{REALM}/users"
        res = requests.post(url, json=payload, headers=headers)

        if res.status_code == 201:
            if i % 100 == 0:
                print(f"{i} usuários criados...")
        elif res.status_code == 409:
            pass # Usuário já existe, ignora
        else:
            print(f"Erro ao criar {username}: {res.text}")

if __name__ == "__main__":
    # Importante instalar o requests antes de rodar: pip install requests
    create_users()
    print("Finalizado!")

    # pip install requests
    # python seed_users.py