# 🛡️ SpaceShield — Sistema de Cibersegurança Orbital

Sistema acadêmico de monitoramento e segurança de satélites, desenvolvido para a FIAP. O projeto é composto por duas camadas: um terminal CLI em Java e uma interface web estática.

---

## ⚙️ Requisitos

- **Java 21**
- **Gradle** (via wrapper incluído no projeto)
- **Python 3** (apenas para rodar a interface web)

---

## 🖥️ Rodando o Terminal Java (CLI)

Na raiz do projeto, execute:

```bash
./gradlew run
```

> No Windows, use `gradlew.bat run`

O terminal interativo será iniciado. Use as credenciais padrão para entrar:

- **Email:** `admin@spaceshield.com`
- **Senha:** `1234`

---

## 🌐 Rodando a Interface Web

Na **raiz do projeto**, suba um servidor HTTP com Python:

```bash
python3 -m http.server 8000
```

Depois acesse no navegador:

```
http://0.0.0.0:8000/app/src/main/templates/login.html
```

Use as mesmas credenciais acima para autenticar.

---

## 📁 Estrutura do Projeto

```
SpaceSec/
├── app/
│   └── src/main/
│       ├── java/com/spaceshield/
│       │   ├── model/          # Entidades do domínio
│       │   ├── service/        # Lógica de negócio
│       │   └── SpaceShieldApp.java
│       ├── resources/
│       │   ├── app.js          # Lógica da interface web
│       │   └── style.css       # Estilos
│       └── templates/          # Páginas HTML
├── database/
│   └── database.sql
└── build.gradle
```

---

## 🎯 Conexão com os ODS da ONU

Este projeto foi desenvolvido com base nos seguintes Objetivos de Desenvolvimento Sustentável:

**ODS 9 — Indústria, inovação e infraestrutura**
Satélites compõem a espinha dorsal da comunicação global, do rastreamento logístico e da conectividade de última milha. Ataques a essa infraestrutura têm impacto direto sobre redes de internet, sistemas bancários e comunicações militares. O SpaceShield aborda a proteção dessa camada crítica.

**ODS 11 — Cidades e comunidades sustentáveis**
Serviços urbanos essenciais — como mobilidade, abastecimento e resposta a emergências — dependem de sinais GPS e de comunicação via satélite. A indisponibilidade ou comprometimento desses sistemas pode paralisar cidades inteiras. Monitorar e reagir a anomalias nessa infraestrutura é parte da resiliência urbana.

**ODS 8 — Trabalho decente e crescimento econômico**
Operações logísticas, transações financeiras e cadeias de suprimento globais dependem da integridade de dados transmitidos via satélite. Falhas de segurança nessa camada geram prejuízos econômicos em escala e afetam diretamente empregos e a estabilidade de setores produtivos.

---

## 📖 Sobre o Projeto

O SpaceShield é um **Security Operations Center (SOC)** voltado para infraestrutura orbital. O sistema permite monitorar ativos de satélite, registrar eventos de acesso e reagir a tentativas de invasão em tempo real.

### Funcionalidades

**Autenticação**
- Login e registro de operadores com senhas protegidas via BCrypt (na camada Java)
- Controle de sessão por `localStorage` na camada web

**Gestão de Satélites**
- Cadastro de satélites com ID único gerado automaticamente
- Cada satélite pertence a um operador e exibe função, status e nível de risco
- O nível de risco sobe automaticamente conforme eventos são registrados (Low → Medium → High)

**Simulação de Ataques (Red Team)**
- O dashboard principal possui um módulo de injeção de falhas para fins acadêmicos
- O operador informa o ID do satélite alvo, o tipo de ameaça e os logs capturados
- Ameaças classificadas como `Invasão`, `Unauthorized`, `Suspicious` ou `Spoofing` geram um **SecurityAlert** automaticamente

**Alertas de Segurança**
- Listagem de todos os alertas críticos da frota do operador
- Possibilidade de marcar alertas como resolvidos

**Relatórios de Incidentes**
- Histórico completo de `AccessEvents` por satélite
- Exibe o nível de risco atual e todos os eventos registrados com data/hora

### Modelos de Domínio

| Classe | Responsabilidade |
|---|---|
| `User` | Operador autenticado no sistema |
| `Satellite` | Ativo monitorado, vinculado a um operador |
| `AccessEvent` | Registro de um evento de acesso ou incidente |
| `SecurityAlert` | Alerta gerado a partir de um evento crítico |

### Serviços

| Classe | Responsabilidade |
|---|---|
| `AuthService` | Registro e autenticação de usuários com BCrypt |
| `Monitoring` | Gerencia satélites, eventos e geração de alertas |
| `Report` | Geração do histórico de incidentes de um satélite |

---

## 🔐 Credenciais Padrão

| Campo | Valor |
|---|---|
| Email | `admin@spaceshield.com` |
| Senha | `1234` |
