# Evidencia de los ciclos TDD (Red-Green-Refactor)

Se aplicó TDD a dos funcionalidades que no existían al inicio. En cada ciclo el test se escribió antes que el código y cada fase quedó en un commit separado.

## Ciclo 1: límite de préstamos activos

Regla: un usuario no puede tener más de 3 préstamos activos (RF-02). Casos: CP-011, CP-012 y CP-013.

| Fase | Commit | Mensaje | Qué cambia |
|---|---|---|---|
| Red | `2ec6683` | `test(red): test para verificar límite de préstamos activos` | Solo `PrestamoServiceTest`. Los tests nuevos deben fallar. |
| Green | `e2f1d99` | `feat(green): implementar verificación de límite de préstamos` | Solo `PrestamoService`. Código mínimo: una condición con el valor 3. |
| Refactor | `62624ef` | `refactor: extraer constante y método verificarLimiteDePrestamos` | Solo `PrestamoService`. Constante `LIMITE_PRESTAMOS_ACTIVOS` y método privado; mismo comportamiento. |

Fallo esperado en Red: `registrarPrestamo_cuandoSuperaElLimiteDePrestamos_lanzaExcepcion` espera `LimitePrestamosExcedidoException`, pero el código base continúa y lanza `RecursoNoEncontradoException` (el material simulado no existe). `registrarPrestamo_cuandoTieneDosPrestamosActivos_permiteTercero` falla por `UnnecessaryStubbingException`, porque el código base nunca consulta el conteo de préstamos activos.

## Ciclo 2: validación del formato de correo

Regla: el correo del usuario debe tener la forma `usuario@dominio.extensión` (RF-09). Casos: CP-021, CP-022, CP-023 y CP-024.

| Fase | Commit | Mensaje | Qué cambia |
|---|---|---|---|
| Red | `8a2a12e` | `test(red): test para validar formato de correo en UsuarioService` | Solo `UsuarioServiceTest`. Los casos de correo inválido deben fallar. |
| Green | `a6c3100` | `feat(green): implementar validación de formato de correo` | Solo `UsuarioService`. Código mínimo: expresión regular en línea. |
| Refactor | `04ac4cb` | `refactor: extraer patrón de correo y método esCorreoValido` | Solo `UsuarioService`. `Pattern` precompilado y método privado; mismo comportamiento. |

Fallo esperado en Red: `registrar_cuandoCorreoInvalido_lanzaExcepcion` espera `IllegalArgumentException`, pero el código base guarda el usuario sin validar (`assertThrows` falla con "Expected IllegalArgumentException to be thrown, but nothing was thrown"). `registrar_cuandoCorreoValido_guardaUsuario` ya pasa en Red.

## Cómo reproducir cada fase

```
git checkout <commit>
mvn test
```

- En el commit Red de cada ciclo, `mvn test` debe terminar en `BUILD FAILURE` con los tests descritos arriba.
- En el commit Green y en el Refactor, `mvn test` debe terminar en `BUILD SUCCESS`.

Al terminar, volver a la rama de trabajo con `git checkout feature/plan-tests`.

## Historial completo

Se puede ver en GitHub: repositorio, rama `feature/plan-tests`, botón "Commits".
