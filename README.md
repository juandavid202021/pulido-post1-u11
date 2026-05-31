## Catálogo de Productos — Post-Contenido 1, Unidad 11
Refactorización con SOLID (SRP + DIP), patrones DAO/DTO, Factory y manejo centralizado de excepciones con @RestControllerAdvice.

## Arquitectura en capas 

┌─────────────────────────────────────────────────────────────┐
│                        Cliente (HTTP)                        │
│              curl / Postman / Navegador                      │
└────────────────────────┬────────────────────────────────────┘
                         │ JSON (RequestDTO / ResponseDTO)
                         ▼
┌─────────────────────────────────────────────────────────────┐
│                    CAPA CONTROLLER                           │
│  ProductoController  (@RestController, @RequestMapping)      │
│  • Recibe HTTP, delega en ProductoService                    │
│  • Aplica @Valid sobre ProductoRequestDTO                    │
└────────────────────────┬────────────────────────────────────┘
                         │ depende de la INTERFAZ (DIP)
                         ▼
┌─────────────────────────────────────────────────────────────┐
│                    CAPA SERVICE                              │
│  «interface» ProductoService                                 │
│       ▲ implementada por                                     │
│  ProductoServiceImpl  (@Service)                             │
│  • Solo lógica de negocio (SRP)                              │
│  • Usa ProductoFactory para convertir entidad ↔ DTO          │
│  • Lanza RecursoNoEncontradoException si id no existe        │
└──────────┬────────────────────────────┬─────────────────────┘
           │                            │
           ▼                            ▼
┌──────────────────────┐   ┌────────────────────────────────┐
│   CAPA REPOSITORY    │   │         CAPA FACTORY           │
│  (DAO)               │   │  ProductoFactory (@Component)  │
│  ProductoRepository  │   │  • toEntity(RequestDTO)        │
│  extiende            │   │  • toResponseDTO(Producto)     │
│  JpaRepository       │   └────────────────────────────────┘
│  • findByActivoTrue()│
└──────────┬───────────┘
           │ JPA / H2
           ▼
┌─────────────────────────────────────────────────────────────┐
│                    CAPA ENTITY                               │
│  Producto  (@Entity)                                         │
│  • id, nombre, precio, categoria, activo                     │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│                 CAPA EXCEPTION (transversal)                 │
│  GlobalExceptionHandler  (@RestControllerAdvice)             │
│  • RecursoNoEncontradoException  → 404 ApiError             │
│  • MethodArgumentNotValidException → 400 ApiError           │
│  • Exception genérica            → 500 ApiError             │
│                                                              │
│  ApiError { status, error, mensaje, timestamp, path }        │
└─────────────────────────────────────────────────────────────┘

## Relacion entre componentes 

| Origen | Relación | Destino |
|--------|----------|---------|
| ProductoController | depende de interfaz (DIP) | ProductoService |
| ProductoServiceImpl | implementa | ProductoService |
| ProductoServiceImpl | usa | ProductoRepository |
| ProductoServiceImpl | usa | ProductoFactory |
| ProductoFactory | convierte | Producto ↔ DTOs |
| ProductoRepository | extiende | JpaRepository\<Producto\> |
| GlobalExceptionHandler | intercepta excepciones de | todos los controllers |

## Clonar el repositorio

```bash
git clone https://github.com/juandavid202021/pulido-post1-u11.git
cd pulido-post1-u11/catalogo
```

## Prerrequisitos 

Java 17 o superior
Maven 3.9.x
IDE: IntelliJ IDEA o VS Code con Extension Pack for Java

## Instrucciones de ejecución 

Compilar:  mvn compile (salida esperada --> BUILD SUCCESS)

Iniciar aplicacion: mvn spring-boot:run (La aplicación arranca en http://localhost:8080. La base de datos H2 se crea en memoria automáticamente y se reinicia con cada arranque.)

## Endpoints en posmant

| Método | URL | Descripción | Status |
|--------|-----|-------------|--------|
| GET | /api/productos | Lista todos los activos | 200 |
| GET | /api/productos/{id} | Busca por ID | 200 / 404 |
| POST | /api/productos | Crea nuevo producto | 201 |
| DELETE | /api/productos/{id} | Elimina por ID | 204 / 404 |

