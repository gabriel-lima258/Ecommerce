# 🛒 Projeto E-commerce - API REST com Spring Boot

Sistema completo de e-commerce desenvolvido com **Spring Boot**, incluindo autenticação OAuth2, controle de acesso baseado em roles, APIs RESTful e persistência de dados com JPA/Hibernate.

---

## 📚 Índice

1. [Sobre o Projeto](#sobre-o-projeto)
2. [Funcionalidades](#funcionalidades)
3. [Tecnologias Utilizadas](#tecnologias-utilizadas)
4. [Estrutura do Projeto](#estrutura-do-projeto)
5. [Modelo de Dados](#modelo-de-dados)
6. [Autenticação e Autorização](#autenticação-e-autorização)
7. [API REST - Endpoints](#api-rest---endpoints)
8. [Como Executar](#como-executar)
9. [Configuração](#configuração)
10. [Estrutura do Banco de Dados](#estrutura-do-banco-de-dados)

---

## 🎯 Sobre o Projeto

Este projeto implementa um sistema completo de e-commerce com as seguintes funcionalidades:

- ✅ **Autenticação OAuth2** com JWT (JSON Web Tokens)
- ✅ **Controle de acesso** baseado em roles (ADMIN, CLIENT)
- ✅ **CRUD completo** de produtos, categorias, pedidos e usuários
- ✅ **Sistema de pedidos** com controle de status
- ✅ **Paginação e busca** de produtos
- ✅ **Validação de dados** com Bean Validation
- ✅ **Tratamento de exceções** customizado
- ✅ **CORS configurado** para integração com frontend
- ✅ **DTOs** para transferência de dados
- ✅ **Relacionamentos JPA** complexos (OneToMany, ManyToOne, ManyToMany, OneToOne)

---

## ✨ Funcionalidades

### Autenticação e Segurança
- Autenticação via OAuth2 com grant type customizado (password)
- Geração de tokens JWT com claims customizados
- Controle de acesso baseado em roles (`ROLE_ADMIN`, `ROLE_CLIENT`)
- Proteção de endpoints com `@PreAuthorize`
- Criptografia de senhas com BCrypt

### Gestão de Produtos
- Listagem paginada de produtos
- Busca de produtos por nome
- CRUD completo (apenas para administradores)
- Associação de produtos com categorias

### Gestão de Pedidos
- Criação de pedidos (apenas para clientes autenticados)
- Consulta de pedidos próprios
- Controle de status de pedidos
- Sistema de pagamento integrado

### Gestão de Usuários
- Consulta de dados do usuário autenticado
- Sistema de roles e permissões
- Validação de acesso (self ou admin)

---

## 🛠️ Tecnologias Utilizadas

### Backend
- **Java 21** - Linguagem de programação
- **Spring Boot 3.5.7** - Framework principal
- **Spring Data JPA** - Para persistência de dados
- **Hibernate** - ORM (Object-Relational Mapping)
- **Spring Security** - Segurança e autenticação
- **OAuth2 Authorization Server** - Servidor de autorização
- **OAuth2 Resource Server** - Servidor de recursos protegidos
- **JWT (Nimbus JOSE + JWT)** - Tokens de autenticação
- **Bean Validation** - Validação de dados
- **H2 Database** - Banco de dados em memória (desenvolvimento/teste)
- **Maven** - Gerenciador de dependências

### Principais Dependências
```xml
- spring-boot-starter-web
- spring-boot-starter-data-jpa
- spring-boot-starter-security
- spring-boot-starter-validation
- spring-security-oauth2-authorization-server
- spring-boot-starter-oauth2-resource-server
- h2 (runtime)
- spring-boot-starter-test
- spring-security-test
```

---

## 📁 Estrutura do Projeto

```
src/
├── main/
│   ├── java/
│   │   └── com/gtech/Ecommerce/
│   │       ├── EcommerceApplication.java
│   │       ├── config/
│   │       │   ├── AuthorizationServerConfig.java
│   │       │   ├── ResourceServerConfig.java
│   │       │   └── customgrant/
│   │       │       ├── CustomPasswordAuthenticationConverter.java
│   │       │       ├── CustomPasswordAuthenticationProvider.java
│   │       │       ├── CustomPasswordAuthenticationToken.java
│   │       │       └── CustomUserAuthorities.java
│   │       ├── controllers/
│   │       │   ├── CategoryController.java
│   │       │   ├── OrderController.java
│   │       │   ├── ProductController.java
│   │       │   ├── UserController.java
│   │       │   └── handlers/
│   │       │       └── ControllerExceptionHandler.java
│   │       ├── dto/
│   │       │   ├── CustomErrorDTO.java
│   │       │   ├── FieldMessageDTO.java
│   │       │   ├── ValidationErrorDTO.java
│   │       │   ├── order/
│   │       │   │   ├── ClientDTO.java
│   │       │   │   ├── OrderDTO.java
│   │       │   │   ├── OrderItemDTO.java
│   │       │   │   └── PaymentDTO.java
│   │       │   ├── product/
│   │       │   │   ├── CategoryDTO.java
│   │       │   │   ├── ProductDTO.java
│   │       │   │   └── ProductMinDTO.java
│   │       │   └── user/
│   │       │       └── UserDTO.java
│   │       ├── entities/
│   │       │   ├── Category.java
│   │       │   ├── Order.java
│   │       │   ├── OrderItem.java
│   │       │   ├── OrderItemPK.java
│   │       │   ├── OrderStatus.java (enum)
│   │       │   ├── Payment.java
│   │       │   ├── Product.java
│   │       │   ├── Role.java
│   │       │   └── User.java
│   │       ├── projections/
│   │       │   └── UserDetailsProjection.java
│   │       ├── repositories/
│   │       │   ├── CategoryRepository.java
│   │       │   ├── OrderItemRepository.java
│   │       │   ├── OrderRepository.java
│   │       │   ├── ProductRepository.java
│   │       │   └── UserRepository.java
│   │       └── services/
│   │           ├── AuthService.java
│   │           ├── CategoryService.java
│   │           ├── OrderService.java
│   │           ├── ProductService.java
│   │           ├── UserService.java
│   │           └── exceptions/
│   │               ├── DatabaseException.java
│   │               ├── ForbiddenException.java
│   │               └── ResourceNotFoundException.java
│   └── resources/
│       ├── application.properties
│       ├── application-test.properties
│       └── import.sql
└── test/
    └── java/
        └── com/gtech/Ecommerce/
            └── EcommerceApplicationTests.java
```

---

## 🗄️ Modelo de Dados

![Diagrama UML do Modelo de Domínio](public/Uml-Ecommerce.png)

### Entidades Principais

#### 1. **User (Usuário/Cliente)**
Representa os usuários do sistema (clientes e administradores).

**Atributos:**
- `id` - Chave primária (Long)
- `name` - Nome do usuário
- `email` - Email (único) - usado como username
- `password` - Senha (criptografada com BCrypt)
- `phone` - Telefone
- `birthDate` - Data de nascimento (LocalDate)

**Relacionamentos:**
- `OneToMany` com `Order` (um usuário pode ter vários pedidos)
- `ManyToMany` com `Role` (um usuário pode ter várias roles)

**Implementa:** `UserDetails` (Spring Security)

#### 2. **Role (Papel/Permissão)**
Define os papéis/permissões dos usuários no sistema.

**Atributos:**
- `id` - Chave primária (Long)
- `authority` - Nome da role (ex: "ROLE_ADMIN", "ROLE_CLIENT")

**Relacionamentos:**
- `ManyToMany` com `User`

**Implementa:** `GrantedAuthority` (Spring Security)

#### 3. **Product (Produto)**
Representa os produtos disponíveis para venda.

**Atributos:**
- `id` - Chave primária (Long)
- `name` - Nome do produto
- `description` - Descrição (TEXT)
- `price` - Preço (Double)
- `imgUrl` - URL da imagem

**Relacionamentos:**
- `ManyToMany` com `Category` (um produto pode ter várias categorias)
- `OneToMany` com `OrderItem` (um produto pode aparecer em vários itens de pedido)

#### 4. **Category (Categoria)**
Organiza os produtos em categorias.

**Atributos:**
- `id` - Chave primária (Long)
- `name` - Nome da categoria

**Relacionamentos:**
- `ManyToMany` com `Product` (uma categoria pode conter vários produtos)

#### 5. **Order (Pedido)**
Representa um pedido feito por um cliente.

**Atributos:**
- `id` - Chave primária (Long)
- `moment` - Data/hora do pedido (Instant)
- `status` - Status do pedido (enum OrderStatus)

**Relacionamentos:**
- `ManyToOne` com `User` (vários pedidos pertencem a um cliente)
- `OneToOne` com `Payment` (um pedido tem um pagamento)
- `OneToMany` com `OrderItem` (um pedido tem vários itens)

#### 6. **OrderItem (Item do Pedido)**
Representa um item específico dentro de um pedido (produto + quantidade + preço).

**Atributos:**
- `id` - Chave primária composta (`OrderItemPK`)
- `quantity` - Quantidade do produto
- `price` - Preço do item

**Relacionamentos:**
- Usa chave primária composta (`@EmbeddedId`) contendo:
  - `Order` (ManyToOne)
  - `Product` (ManyToOne)

#### 7. **Payment (Pagamento)**
Representa o pagamento de um pedido.

**Atributos:**
- `id` - Chave primária (Long) - **compartilhado com Order** (`@MapsId`)
- `moment` - Data/hora do pagamento (Instant)

**Relacionamentos:**
- `OneToOne` com `Order` (um pagamento pertence a um pedido)

#### 8. **OrderStatus (Enum)**
Define os possíveis status de um pedido:
- `WAITING_PAYMENT` - Aguardando pagamento
- `PAID` - Pago
- `SHIPPED` - Enviado
- `DELIVERED` - Entregue
- `CANCELED` - Cancelado

---

## 🔐 Autenticação e Autorização

### Sistema OAuth2 com JWT

O projeto utiliza **OAuth2 Authorization Server** do Spring Security com um **grant type customizado** baseado em senha (password).

### Fluxo de Autenticação

1. **Obter Token de Acesso:**
   ```http
   POST /oauth2/token
   Content-Type: application/x-www-form-urlencoded
   
   grant_type=password&username=seu-email@exemplo.com&password=sua-senha&client_id=myclientid&client_secret=myclientsecret
   ```

2. **Resposta:**
   ```json
   {
     "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
     "token_type": "Bearer",
     "expires_in": 86400
   }
   ```

3. **Usar Token nas Requisições:**
   ```http
   GET /products
   Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...
   ```

### Roles e Permissões

- **ROLE_ADMIN**: Acesso completo ao sistema (CRUD de produtos, visualização de todos os pedidos)
- **ROLE_CLIENT**: Acesso limitado (criação de pedidos, visualização de próprios pedidos)

### Custom Grant Type

O projeto implementa um grant type customizado (`password`) que permite autenticação direta com email e senha, incluindo:
- `CustomPasswordAuthenticationConverter` - Converte a requisição em token de autenticação
- `CustomPasswordAuthenticationProvider` - Valida as credenciais e autentica o usuário
- `CustomUserAuthorities` - Carrega as authorities do usuário

### Configuração de Segurança

- **AuthorizationServerConfig**: Configura o servidor de autorização OAuth2
- **ResourceServerConfig**: Configura o servidor de recursos protegidos
- **CORS**: Configurado para permitir requisições de origens específicas

---

## 🌐 API REST - Endpoints

### Autenticação

#### Obter Token
```http
POST /oauth2/token
Content-Type: application/x-www-form-urlencoded

grant_type=password&username={email}&password={senha}&client_id={client_id}&client_secret={client_secret}
```

### Produtos

#### Listar Produtos (Paginado)
```http
GET /products?name={nome}&page={page}&size={size}&sort={campo,direção}
```
**Permissão:** Público

**Parâmetros:**
- `name` (opcional): Filtrar por nome
- `page` (opcional): Número da página (padrão: 0)
- `size` (opcional): Itens por página (padrão: 20)
- `sort` (opcional): Ordenação (ex: `name,asc`)

#### Buscar Produto por ID
```http
GET /products/{id}
```
**Permissão:** Público

#### Criar Produto
```http
POST /products
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "Nome do Produto",
  "description": "Descrição do produto",
  "price": 99.90,
  "imgUrl": "https://exemplo.com/imagem.jpg",
  "categories": [{"id": 1}]
}
```
**Permissão:** `ROLE_ADMIN`

#### Atualizar Produto
```http
PUT /products/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "Nome Atualizado",
  "description": "Nova descrição",
  "price": 89.90,
  "imgUrl": "https://exemplo.com/nova-imagem.jpg",
  "categories": [{"id": 1}, {"id": 2}]
}
```
**Permissão:** `ROLE_ADMIN`

#### Deletar Produto
```http
DELETE /products/{id}
Authorization: Bearer {token}
```
**Permissão:** `ROLE_ADMIN`

### Categorias

#### Listar Categorias
```http
GET /categories
```
**Permissão:** Público

### Pedidos

#### Buscar Pedido por ID
```http
GET /orders/{id}
Authorization: Bearer {token}
```
**Permissão:** `ROLE_ADMIN` ou `ROLE_CLIENT` (apenas próprios pedidos)

#### Criar Pedido
```http
POST /orders
Authorization: Bearer {token}
Content-Type: application/json

{
  "items": [
    {
      "quantity": 2,
      "productId": 1
    },
    {
      "quantity": 1,
      "productId": 3
    }
  ],
  "payment": {
    "moment": "2024-01-15T10:30:00Z"
  }
}
```
**Permissão:** `ROLE_CLIENT`

### Usuários

#### Obter Dados do Usuário Autenticado
```http
GET /users/me
Authorization: Bearer {token}
```
**Permissão:** `ROLE_ADMIN` ou `ROLE_CLIENT`

---

## 🚀 Como Executar

### Pré-requisitos

- **Java 21** ou superior
- **Maven 3.6+** (ou use o Maven Wrapper incluído)

### Passos

1. **Clone o repositório:**
   ```bash
   git clone <url-do-repositorio>
   cd Ecommerce
   ```

2. **Configure as variáveis de ambiente (opcional):**
   
   Crie um arquivo `.env` ou configure as variáveis:
   ```bash
   export CLIENT_ID=myclientid
   export CLIENT_SECRET=myclientsecret
   export JWT_DURATION=86400
   export CORS_ORIGINS=http://localhost:3000,http://localhost:5173
   ```

3. **Execute a aplicação:**

   **Usando Maven Wrapper (Windows):**
   ```bash
   .\mvnw.cmd spring-boot:run
   ```

   **Usando Maven Wrapper (Linux/Mac):**
   ```bash
   ./mvnw spring-boot:run
   ```

   **Ou usando Maven instalado:**
   ```bash
   mvn spring-boot:run
   ```

4. **Acesse o console H2:**

   Após iniciar a aplicação, acesse:
   - URL: `http://localhost:8080/h2-console`
   - JDBC URL: `jdbc:h2:mem:testdb`
   - Usuário: `sa`
   - Senha: (deixe em branco)

### População de Dados

O arquivo `import.sql` é executado automaticamente na inicialização e popula o banco com:
- Categorias de exemplo
- Produtos de exemplo
- Usuários de exemplo (com roles)
- Pedidos de exemplo
- Itens de pedido
- Pagamentos

---

## ⚙️ Configuração

### application.properties

```properties
spring.application.name=Ecommerce
spring.profiles.active=test
spring.jpa.open-in-view=false

security.client-id=${CLIENT_ID:myclientid}
security.client-secret=${CLIENT_SECRET:myclientsecret}
security.jwt.duration=${JWT_DURATION:86400}

cors.origins=${CORS_ORIGINS:http://localhost:3000,http://localhost:5173}
```

### application-test.properties

```properties
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.defer-datasource-initialization=true
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

### Variáveis de Ambiente

- `CLIENT_ID`: ID do cliente OAuth2 (padrão: `myclientid`)
- `CLIENT_SECRET`: Secret do cliente OAuth2 (padrão: `myclientsecret`)
- `JWT_DURATION`: Duração do token JWT em segundos (padrão: `86400` = 24 horas)
- `CORS_ORIGINS`: Origens permitidas para CORS, separadas por vírgula (padrão: `http://localhost:3000,http://localhost:5173`)

---

## 📊 Estrutura do Banco de Dados

### Tabelas

```
tb_user
├── id (PK)
├── name
├── email (UNIQUE)
├── password
├── phone
└── birth_date

tb_role
├── id (PK)
└── authority

tb_user_role (tabela de junção)
├── user_id (FK)
└── role_id (FK)

tb_category
├── id (PK)
└── name

tb_product
├── id (PK)
├── name
├── description (TEXT)
├── price
└── img_url

tb_product_category (tabela de junção)
├── product_id (FK)
└── category_id (FK)

tb_order
├── id (PK)
├── moment (TIMESTAMP)
├── status (INTEGER)
└── client_id (FK → tb_user)

tb_order_item
├── order_id (FK → tb_order) [PK composta]
├── product_id (FK → tb_product) [PK composta]
├── quantity
└── price

tb_payment
├── id (PK/FK → tb_order.id)
└── moment (TIMESTAMP)
```

### Diagrama de Relacionamentos

```
User (1) ────< (N) Order (1) ──── (1) Payment
  │                              │
  │                              │
  └─── (N) >───< (N) Role       └───< (N) OrderItem (N) >─── (1) Product
                                                                     │
                                                                     │
                                                                     └─── (N) >───< (N) Category
```

---

## 🔗 Relacionamentos JPA

### 1. Relacionamento @ManyToOne (User ↔ Order)

**Em Order:**
```java
@ManyToOne
@JoinColumn(name = "client_id")
private User client;
```

**Em User:**
```java
@OneToMany(mappedBy = "client")
private List<Order> orders = new ArrayList<>();
```

### 2. Relacionamento @ManyToMany (Product ↔ Category)

**Em Product:**
```java
@ManyToMany
@JoinTable(name = "tb_product_category",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id"))
Set<Category> categories = new HashSet<>();
```

**Em Category:**
```java
@ManyToMany(mappedBy = "categories")
Set<Product> products = new HashSet<>();
```

### 3. Relacionamento @OneToOne (Order ↔ Payment)

**Em Order:**
```java
@OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
private Payment payment;
```

**Em Payment:**
```java
@OneToOne
@MapsId
private Order order;
```

### 4. Relacionamento @ManyToMany (User ↔ Role)

**Em User:**
```java
@ManyToMany
@JoinTable(name = "tb_user_role",
           joinColumns = @JoinColumn(name = "user_id"),
           inverseJoinColumns = @JoinColumn(name = "role_id"))
private Set<Role> roles = new HashSet<>();
```

### 5. Chave Primária Composta (@EmbeddedId)

**OrderItemPK (Chave Composta):**
```java
@Embeddable
public class OrderItemPK {
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
```

**OrderItem:**
```java
@Entity
public class OrderItem {
    @EmbeddedId
    private OrderItemPK id = new OrderItemPK();
    // ...
}
```

---

## 🎓 Conceitos Avançados Implementados

### 1. **Autenticação OAuth2 Customizada**
- Grant type customizado (password)
- Geração de tokens JWT com claims personalizados
- Integração com Spring Security

### 2. **Controle de Acesso Baseado em Roles**
- `@PreAuthorize` para proteção de endpoints
- Validação de acesso (self ou admin)
- Authorities customizadas

### 3. **DTOs (Data Transfer Objects)**
- Separação entre entidades de domínio e DTOs
- DTOs específicos para diferentes operações (ProductDTO, ProductMinDTO)
- Validação com Bean Validation

### 4. **Tratamento de Exceções**
- `@ControllerAdvice` para tratamento global
- Exceções customizadas (ResourceNotFoundException, DatabaseException, ForbiddenException)
- DTOs de erro padronizados

### 5. **Paginação e Busca**
- Spring Data paginação
- Busca dinâmica por nome
- Ordenação customizável

### 6. **CORS Configurado**
- Configuração de origens permitidas
- Suporte a credenciais
- Headers e métodos permitidos

---

## 📝 Notas Importantes

- Este projeto usa **H2 Database** (banco em memória) para desenvolvimento/teste
- Os dados são **perdidos** quando a aplicação é encerrada
- Para produção, configure um banco de dados persistente (PostgreSQL, MySQL, etc.)
- O arquivo `application.properties` está configurado para perfil `test`
- A propriedade `spring.jpa.open-in-view=false` melhora o desempenho
- Tokens JWT têm duração configurável (padrão: 24 horas)

---

## 🧪 Testes

Execute os testes com:
```bash
./mvnw test
```

---

## 📚 Recursos Adicionais

- [Documentação Spring Boot](https://spring.io/projects/spring-boot)
- [Documentação Spring Security](https://spring.io/projects/spring-security)
- [Documentação Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [OAuth2 Authorization Server](https://docs.spring.io/spring-authorization-server/reference/)
- [Documentação Hibernate](https://hibernate.org/orm/documentation/)
- [JPA Specification](https://jakarta.ee/specifications/persistence/)

---

## 👨‍💻 Autor

Projeto desenvolvido para fins educacionais, demonstrando conceitos avançados de:
- Spring Boot
- Spring Security e OAuth2
- JPA/Hibernate
- APIs RESTful
- Autenticação e Autorização

---

**Bons estudos! 🚀**
