# 📚 Gestor de Libros

Aplicación de gestión personal de libros estilo Goodreads, desarrollada en Java con SQLite.

## Funcionalidades

- ✅ **Libros leídos** — registro con fecha de inicio/fin y puntuación
- ⭐ **Favoritos** — tanto libros como autores marcables como favoritos
- 📊 **Progreso de lectura** — página actual vs total, porcentaje calculado automáticamente
- 📝 **Notas** — anotaciones por página asociadas a cada libro
- 💬 **Citas** — fragmentos destacados guardados por libro
- 👤 **Autores favoritos** — gestión de autores con ficha biográfica

## Tecnologías

| Tecnología | Uso |
|-----------|-----|
| Java 17   | Lenguaje principal |
| SQLite    | Base de datos local (`./database/gestor_libros.db`) |
| Maven     | Gestión de dependencias |

## Arquitectura

```
gestor.libros
├── database/sqlite/
│   ├── SQLiteConnectionManager.java   ← Gestión de conexión
│   └── DatabaseInitializer.java       ← Creación de tablas
│
└── app/
    ├── model/
    │   ├── Autor.java
    │   ├── Libro.java
    │   ├── Lectura.java    ← Estado de lectura (PENDIENTE/LEYENDO/LEIDO/ABANDONADO)
    │   ├── Nota.java
    │   └── Cita.java
    │
    ├── repository/
    │   ├── interfaces/     ← IAutorRepository, ILibroRepository, etc.
    │   ├── AutorRepository.java
    │   ├── LibroRepository.java
    │   ├── LecturaRepository.java
    │   ├── NotaRepository.java
    │   └── CitaRepository.java
    │
    ├── service/
    │   ├── interfaces/     ← IAutorService, ILibroService, etc.
    │   ├── AutorService.java
    │   ├── LibroService.java
    │   ├── LecturaService.java   ← Lógica de negocio (progreso, favoritos)
    │   ├── NotaService.java
    │   └── CitaService.java
    │
    └── Main.java           ← Demo de prueba en consola
```

## Estados de lectura

```
PENDIENTE → LEYENDO → LEIDO
                  ↘ ABANDONADO
```

El paso de `PENDIENTE` a `LEYENDO` ocurre automáticamente al actualizar el progreso.
El paso a `LEIDO` ocurre automáticamente al llegar a la última página.

## Cómo ejecutar

### Requisitos
- Java 17+
- Maven 3.6+

### Compilar y ejecutar

```bash
mvn compile
mvn exec:java -Dexec.mainClass="gestor.libros.app.Main"
```

### Con JavaFX (cuando añadas la UI)

```bash
mvn javafx:run
```

## Base de datos

Se crea automáticamente en `./database/gestor_libros.db` al iniciar la app.  
Puedes inspeccionar el archivo con [DB Browser for SQLite](https://sqlitebrowser.org/).

## Extensiones sugeridas para VS Code

- **Extension Pack for Java** (Microsoft)
- **SQLite Viewer** (Florian Klampfer) — para ver la BD sin salir del editor
- **Maven for Java** (Microsoft)
