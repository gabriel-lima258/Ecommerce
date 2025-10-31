# 🛒 Projeto E-commerce - Aula Completa sobre Spring Boot e JPA

Este projeto é uma aplicação Spring Boot de **e-commerce** desenvolvida para demonstrar conceitos avançados de **JPA (Java Persistence API)** e **Hibernate**, incluindo mapeamento de relacionamentos complexos entre entidades.

---

## 📚 Índice

1. [Sobre o Projeto](#sobre-o-projeto)
2. [Tecnologias Utilizadas](#tecnologias-utilizadas)
3. [Estrutura do Projeto](#estrutura-do-projeto)
4. [Modelo de Dados](#modelo-de-dados)
5. [Relacionamentos JPA](#relacionamentos-jpa)
6. [Conceitos Avançados](#conceitos-avançados)
7. [Como Executar](#como-executar)
8. [Estrutura do Banco de Dados](#estrutura-do-banco-de-dados)

---

## 🎯 Sobre o Projeto

Este projeto implementa um sistema básico de e-commerce com as seguintes funcionalidades:

- ✅ Cadastro de produtos e categorias
- ✅ Gerenciamento de usuários
- ✅ Sistema de pedidos (orders)
- ✅ Controle de itens de pedido
- ✅ Sistema de pagamento
- ✅ Controle de status de pedidos

O projeto foi desenvolvido para **ensinar conceitos fundamentais e avançados** de JPA/Hibernate, incluindo:

- Mapeamento de entidades (`@Entity`, `@Table`)
- Relacionamentos: `@OneToMany`, `@ManyToOne`, `@ManyToMany`, `@OneToOne`
- Chaves primárias compostas (`@EmbeddedId`)
- Mapeamento de enums
- Tipos de dados especiais (TIMESTAMP, TEXT)

---

## 🛠️ Tecnologias Utilizadas

### Backend
- **Java 21** - Linguagem de programação
- **Spring Boot 3.5.7** - Framework principal
- **Spring Data JPA** - Para persistência de dados
- **Hibernate** - ORM (Object-Relational Mapping)
- **H2 Database** - Banco de dados em memória (desenvolvimento/teste)
- **Maven** - Gerenciador de dependências

### Principais Dependências
```xml
- spring-boot-starter-web
- spring-boot-starter-data-jpa
- h2 (runtime)
- spring-boot-starter-test
```

---

## 📁 Estrutura do Projeto

```
src/
├── main/
│   ├── java/
│   │   └── com/gtech/Ecommerce/
│   │       ├── EcommerceApplication.java
│   │       └── entities/
│   │           ├── Category.java
│   │           ├── Order.java
│   │           ├── OrderItem.java
│   │           ├── OrderItemPK.java
│   │           ├── OrderStatus.java (enum)
│   │           ├── Payment.java
│   │           ├── Product.java
│   │           └── User.java
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

### Entidades Principais

#### 1. **User (Usuário/Cliente)**
Representa os clientes do e-commerce.

**Atributos:**
- `id` - Chave primária (Long)
- `name` - Nome do usuário
- `email` - Email (único)
- `password` - Senha
- `phone` - Telefone
- `birthDate` - Data de nascimento (LocalDate)

**Relacionamentos:**
- `OneToMany` com `Order` (um usuário pode ter vários pedidos)

#### 2. **Product (Produto)**
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

#### 3. **Category (Categoria)**
Organiza os produtos em categorias.

**Atributos:**
- `id` - Chave primária (Long)
- `name` - Nome da categoria

**Relacionamentos:**
- `ManyToMany` com `Product` (uma categoria pode conter vários produtos)

#### 4. **Order (Pedido)**
Representa um pedido feito por um cliente.

**Atributos:**
- `id` - Chave primária (Long)
- `moment` - Data/hora do pedido (Instant)
- `status` - Status do pedido (enum OrderStatus)

**Relacionamentos:**
- `ManyToOne` com `User` (vários pedidos pertencem a um cliente)
- `OneToOne` com `Payment` (um pedido tem um pagamento)
- `OneToMany` com `OrderItem` (um pedido tem vários itens)

#### 5. **OrderItem (Item do Pedido)**
Representa um item específico dentro de um pedido (produto + quantidade + preço).

**Atributos:**
- `id` - Chave primária composta (`OrderItemPK`)
- `quantity` - Quantidade do produto
- `price` - Preço do item

**Relacionamentos:**
- Usa chave primária composta (`@EmbeddedId`) contendo:
  - `Order` (ManyToOne)
  - `Product` (ManyToOne)

#### 6. **Payment (Pagamento)**
Representa o pagamento de um pedido.

**Atributos:**
- `id` - Chave primária (Long) - **compartilhado com Order** (`@MapsId`)
- `moment` - Data/hora do pagamento (Instant)

**Relacionamentos:**
- `OneToOne` com `Order` (um pagamento pertence a um pedido)

#### 7. **OrderStatus (Enum)**
Define os possíveis status de um pedido:
- `WAITING_PAYMENT` - Aguardando pagamento
- `PAID` - Pago
- `SHIPPED` - Enviado
- `DELIVERED` - Entregue
- `CANCELED` - Cancelado

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

**Explicação:**
- Um `Order` pertence a um `User` (ManyToOne)
- Um `User` pode ter vários `Order` (OneToMany)
- O lado `@ManyToOne` tem o `@JoinColumn` (lado proprietário)
- O lado `@OneToMany` usa `mappedBy` (lado inverso)

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

**Explicação:**
- Um `Product` pode ter várias `Category`
- Uma `Category` pode conter vários `Product`
- Usa tabela de junção `tb_product_category`
- O lado com `@JoinTable` é o proprietário
- Usa `Set` para evitar duplicatas

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

**Explicação:**
- Um `Order` tem um `Payment`
- O `@MapsId` faz com que o `id` do `Payment` seja o mesmo do `Order`
- `cascade = CascadeType.ALL` permite operações em cascata

### 4. Chave Primária Composta (@EmbeddedId)

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

**Explicação:**
- `OrderItem` usa chave primária composta (Order + Product)
- `@Embeddable` marca a classe como incorporável
- `@EmbeddedId` usa a chave composta
- Permite relacionamento ManyToMany com atributos extras (quantity, price)

---

## 🎓 Conceitos Avançados

### 1. **@Column com definições especiais**

```java
@Column(columnDefinition = "TEXT")
private String description;
```

Define o tipo de coluna no banco como `TEXT` ao invés do padrão `VARCHAR`.

### 2. **TIMESTAMP sem timezone**

```java
@Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
private Instant moment;
```

Garante que o timestamp seja armazenado sem timezone.

### 3. **Geração de valores**

```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
```

Usa auto-incremento do banco de dados.

### 4. **Métodos auxiliares com Stream API**

```java
public List<Order> getOrders() {
    return items.stream().map(x -> x.getOrder()).toList();
}
```

Usa Stream API para transformar `OrderItem` em `Order`.

### 5. **Evitar Set em Listas**

**Regra importante:** Nunca faça `set` em listas de relacionamentos! Apenas `get`:

```java
// ✅ Correto
public List<Order> getOrders() {
    return orders;
}

// ❌ Errado - não fazer set
public void setOrders(List<Order> orders) {
    this.orders = orders;
}
```

---

## 🚀 Como Executar

### Pré-requisitos

- Java 21 ou superior
- Maven 3.6+ (ou use o Maven Wrapper incluído)

### Passos

1. **Clone o repositório:**
```bash
git clone <url-do-repositorio>
cd Ecommerce
```

2. **Execute a aplicação:**

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

3. **Acesse o console H2:**

Após iniciar a aplicação, acesse:
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Usuário: `sa`
- Senha: (deixe em branco)

### População de Dados

O arquivo `import.sql` é executado automaticamente na inicialização e popula o banco com:
- 3 categorias (Livros, Eletrônicos, Computadores)
- 25 produtos
- 2 usuários de exemplo
- 3 pedidos de exemplo
- Itens de pedido
- 2 pagamentos

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
                        │
                        │
                        └───< (N) OrderItem (N) >─── (1) Product
                                                         │
                                                         │
                                                         └─── (N) >───< (N) Category
```

---

## 💡 Dicas de Estudo

1. **Estude os relacionamentos em ordem:**
   - Comece com `ManyToOne` e `OneToMany` (mais simples)
   - Depois `OneToOne`
   - Por último `ManyToMany`

2. **Entenda a diferença entre:**
   - Lado **proprietário** (`@JoinColumn` ou `@JoinTable`)
   - Lado **inverso** (`mappedBy`)

3. **Pratique criando queries:**
   - Use `getOrders()` e `getProducts()` para ver como navegar pelos relacionamentos

4. **Experimente:**
   - Adicione novos relacionamentos
   - Crie métodos auxiliares com Stream API
   - Modifique o `import.sql` para adicionar mais dados

---

## 📝 Notas Importantes

- Este projeto usa **H2 Database** (banco em memória)
- Os dados são **perdidos** quando a aplicação é encerrada
- Para persistência real, configure um banco como PostgreSQL ou MySQL
- O arquivo `application.properties` está configurado para perfil `test`
- A propriedade `spring.jpa.open-in-view=false` melhora o desempenho

---

## 🎯 Próximos Passos

Agora que você entendeu o projeto, que tal:

- ✨ Criar **Repositories** (interfaces que estendem `JpaRepository`)
- ✨ Criar **Services** para lógica de negócio
- ✨ Criar **Controllers** REST para expor APIs
- ✨ Implementar **DTOs** (Data Transfer Objects)
- ✨ Adicionar **validações** com Bean Validation
- ✨ Implementar **tratamento de exceções**
- ✨ Adicionar **testes unitários e de integração**

---

## 📚 Recursos Adicionais

- [Documentação Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Documentação Hibernate](https://hibernate.org/orm/documentation/)
- [JPA Specification](https://jakarta.ee/specifications/persistence/)

---

## 👨‍💻 Autor

Projeto desenvolvido para fins educacionais, demonstrando conceitos avançados de JPA/Hibernate no Spring Boot.

---

**Bons estudos! 🚀**

