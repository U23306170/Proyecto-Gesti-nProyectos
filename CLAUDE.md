# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Run the application (port 8081)
./gradlew bootRun

# Build
./gradlew build

# Run tests
./gradlew test

# Run a single test class
./gradlew test --tests "edu.pe.utp.marcodesarrolloweb.hotelmirador.HotelMiradorApplicationTests"
```

On Windows use `gradlew.bat` instead of `./gradlew`.

## Architecture

Spring Boot 4.0.6 + Java 25 + Gradle. Base package: `edu.pe.utp.marcodesarrolloweb.hotelmirador`.

**Two types of web responses coexist:**

- **MVC controllers** (`@Controller` returning a view name) — `HomeController` at `/home` passes model attributes to Thymeleaf templates in `src/main/resources/templates/`.
- **REST/inline response controllers** — `HomeRestController` (`@RestController`) at `/homerest` returns JSON. `ProductoController` and `PedidoController` use `@Controller` + `@ResponseBody` on each method to return plain text directly.

**Two `index.html` files serve different purposes:**

- `src/main/resources/static/index.html` — the full Hotel El Mirador Bootstrap 5 UI (rooms, reservations, guests, modals). Served as a static file; no Spring routing needed.
- `src/main/resources/templates/index.html` — a minimal Thymeleaf template rendered by `HomeController.inicio()` with model attributes (`nombreTienda`, `anio`, `totalProductos`).

**DTO layer:** `dto/Producto.java` (nombre, stock, precio) is used only by `HomeRestController` to demonstrate JSON serialization.

**Routes summary:**

| Prefix | Controller | Response type |
|--------|-----------|---------------|
| `/home` | `HomeController` | Thymeleaf templates |
| `/homerest` | `HomeRestController` | JSON |
| `/productos` | `ProductoController` | Plain text (`@ResponseBody`) |
| `/pedidos` | `PedidoController` | Plain text (`@ResponseBody`) |

Static assets and the main hotel UI are served from `src/main/resources/static/`. Thymeleaf templates live in `src/main/resources/templates/` and use the `xmlns:th` namespace.
