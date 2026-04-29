-- Criação dos bancos de dados dos microsserviços
CREATE DATABASE IF NOT EXISTS gestao_db;
CREATE DATABASE IF NOT EXISTS apontamentos;
CREATE DATABASE IF NOT EXISTS app; -- temporario para testar o microsservico apontamentos

-- Concede privilégios ao usuário da aplicação (appuser) para acessar esses bancos recém-criados
GRANT ALL PRIVILEGES ON gestao_db.* TO 'appuser'@'%';
GRANT ALL PRIVILEGES ON apontamentos.* TO 'appuser'@'%';
GRANT ALL PRIVILEGES ON app.* TO 'appuser'@'%';

-- Aplica as permissões
FLUSH PRIVILEGES;