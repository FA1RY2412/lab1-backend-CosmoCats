# CosmoCats (Maven, Spring Boot 3.3, H2) — final build

- Package root: `org.example.cosmocats`
- REST base: `/api/products`
- Swagger UI: `http://localhost:8081/swagger-ui.html`
- H2 Console: `http://localhost:8081/h2-console` (jdbc:h2:mem:cosmocats, sa/password)

## Run
```
mvn clean spring-boot:run
```

## Create sample
```
curl -X POST http://localhost:8081/api/products -H "Content-Type: application/json" -d '{"name":"Space Tuna","description":"Blue supergiant cut","price":12.34}'
```
