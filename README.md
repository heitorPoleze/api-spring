# Sistema Bibliotecário — API REST com Spring Boot

Trabalho da disciplina **Tópicos Especiais II — Programação Avançada (Java)**, Tecnologia em Sistemas para Internet, IFES Campus Santa Teresa.

## Contexto

API Desenvolvida para uma biblioteca poder controlar os Livros, Leitores e Empréstimos.

## Como iniciar a aplicação

### Pré-requisitos

- **JDK 21** instalado, com `JAVA_HOME` configurado.
- Não é preciso instalar o Maven: o projeto usa o Maven Wrapper (`mvnw`).

### Pelo terminal

Na raiz do projeto:

```bash
# Linux / macOS / Git Bash
./mvnw spring-boot:run

# Windows (PowerShell ou CMD)
mvnw.cmd spring-boot:run
```

### Pela IDE (IntelliJ IDEA)

1. Abra a pasta do projeto (o IntelliJ reconhece o `pom.xml`).
2. Habilite o *annotation processing* (necessário para o Lombok), se a IDE pedir.
3. Execute a classe `SistemaBibliotecarioApplication`.

### Endereço da API

Por padrão, a API sobe em:

```
http://localhost:8080/api
```

A porta e o caminho base podem ser alterados pelas variáveis `SERVER_PORT` e `API_BASE_PATH`, definidas no ambiente ou em um arquivo `.env` na raiz do projeto:

```properties
SERVER_PORT=8080
API_BASE_PATH=/api
```

### Carga inicial de dados

Ao iniciar, a classe `CargaInicialDados` popula a aplicação com dados de exemplo. O console mostra:

```
Carga inicial concluída: 5 leitores, 6 livros, 9 empréstimos (3 devolvidos, 3 ativos, 3 atrasados).
```

## Rotas

Todas as rotas abaixo são relativas a `http://localhost:8080/api`.

### Livros — `/livros`

| Método | Rota | Descrição | Sucesso |
|---|---|---|---|
| POST | `/livros` | Cadastra um livro | 201 |
| GET | `/livros` | Lista livros (aceita filtros) | 200 |
| GET | `/livros/{id}` | Busca um livro pelo ID | 200 |
| PUT | `/livros/{id}` | Atualiza um livro | 200 |
| DELETE | `/livros/{id}` | Remove um livro | 204 |

Filtros do `GET /livros` (opcionais e combináveis):

- `autor`: parte do nome do autor. Ex.: `?autor=fowler`
- `disponivel`: `true` ou `false`. Ex.: `?disponivel=true`

Corpo do POST/PUT:

```json
{
  "titulo": "Dom Casmurro",
  "autor": "Machado de Assis",
  "isbn": "9788535910663",
  "anoPublicacao": 1899,
  "quantidadeTotal": 3
}
```

Validações: título com até 150 caracteres; autor com até 100; ISBN com exatamente 13 dígitos; ano de publicação a partir de 1450; quantidade total maior que zero.

### Leitores — `/leitores`

| Método | Rota | Descrição | Sucesso |
|---|---|---|---|
| POST | `/leitores` | Cadastra um leitor | 201 |
| GET | `/leitores` | Lista leitores (aceita filtro) | 200 |
| GET | `/leitores/{id}` | Busca um leitor pelo ID | 200 |
| PUT | `/leitores/{id}` | Atualiza um leitor | 200 |
| DELETE | `/leitores/{id}` | Remove um leitor | 204 |

Filtro do `GET /leitores`:

- `tipo`: `ALUNO` ou `PROFESSOR`. Ex.: `?tipo=ALUNO`

Corpo do POST/PUT:

```json
{
  "nome": "Fernanda Alves",
  "email": "fernanda.alves@email.com",
  "tipoPessoa": "ALUNO"
}
```

Validações: nome obrigatório com até 100 caracteres; e-mail obrigatório em formato válido; `tipoPessoa` obrigatório (`ALUNO` ou `PROFESSOR`).

### Empréstimos — `/emprestimos`

| Método | Rota | Descrição | Sucesso |
|---|---|---|---|
| POST | `/emprestimos` | Registra um empréstimo | 201 |
| GET | `/emprestimos` | Lista empréstimos (aceita filtros) | 200 |
| GET | `/emprestimos/{id}` | Busca um empréstimo pelo ID | 200 |
| POST | `/emprestimos/{id}/devolucao` | Registra a devolução (calcula a multa, se houver atraso) | 200 |
| GET | `/emprestimos/atrasados` | Lista os empréstimos atrasados | 200 |

Filtros do `GET /emprestimos` (opcionais e combináveis):

- `leitorId`: ID do leitor. Ex.: `?leitorId=1`
- `ativos`: `true` (não devolvidos) ou `false` (devolvidos). Ex.: `?ativos=true`

Corpo do POST:

```json
{
  "livroId": 1,
  "leitorId": 3
}
```

As datas do empréstimo e da devolução prevista são calculadas pela API, de acordo com o tipo do leitor.

Exemplo de resposta:

```json
{
  "id": 10,
  "livro": { "id": 1, "titulo": "Clean Code" },
  "leitor": { "id": 3, "nome": "Carla Mendes" },
  "dataEmprestimo": "2026-11-22",
  "dataPrevistaDevolucao": "2026-11-29",
  "dataDevolucao": null,
  "multa": null
}
```

### Respostas de erro

Os erros seguem o formato `ProblemDetail`:

```json
{
  "type": "about:blank",
  "title": "Regra de negócio violada",
  "status": 409,
  "detail": "Leitor não pode ter mais um empréstimo, pois tem livros atrasados.",
  "instance": "/api/emprestimos"
}
```

| Status | Quando ocorre |
|---|---|
| 400 Bad Request | Corpo inválido ou falha na validação dos campos |
| 404 Not Found | Livro, leitor ou empréstimo não encontrado |
| 409 Conflict | Regra de negócio violada (ISBN/e-mail duplicado, limite de empréstimos, leitor com atraso, livro indisponível, remoção com empréstimo ativo etc.) |

## Como testar

O projeto inclui uma coleção pronta em `postman/postmanCollection.json`, com todas as rotas e filtros.

1. Inicie a aplicação.
2. No Postman, clique em **Import** e selecione `postman/postmanCollection.json`.
3. A coleção **biblioteca-spring-api** aparece com as pastas `leitores`, `livros` e `emprestimos`.
4. A variável `{{url}}` da coleção já aponta para `http://localhost:8080/api`. Se mudar a porta ou o caminho base, ajuste-a em *Variables*.
5. Execute as requisições.

## Estrutura do projeto

```
src/main/java/edu/ifes/sistema_bibliotecario
├── config
│   ├── carga        # CargaInicialDados: dados de exemplo
│   └── clock        # ClockConfig: data atual da aplicação
├── controller       # LivroController, LeitorController, EmprestimoController
├── domain           # entidades e especificações (filtros)
├── dto              # records de request, response, filtros e mappers
├── exception        # exceções de negócio e handler global
├── repository       # interfaces e implementações em memória
└── service          # regras de negócio e strategies de empréstimo
postman/
└── postmanCollection.json
```
