# Escenarios de prueba narrativos – BiblioUni

Formato: **Dado** (precondición) – **Cuando** (acción) – **Entonces** (resultado esperado).

| ID | Componente | Escenario | Tipo | Prioridad | Camino |
|---|---|---|---|---|---|
| EP-001 | MaterialService | **Dado** un material con título, autor y ISBN válidos, **cuando** se registra, **entonces** queda guardado como disponible con stock inicial. | Unitaria | Alta | Feliz |
| EP-002 | PrestamoService | **Dado** un estudiante existente y un material disponible, **cuando** solicita un préstamo, **entonces** se crea el préstamo activo y el stock baja en 1. | Unitaria | Alta | Feliz |
| EP-003 | PrestamoService | **Dado** un estudiante con 3 préstamos activos, **cuando** solicita un cuarto, **entonces** el sistema lo rechaza con un mensaje de límite excedido (valor límite). | Unitaria | Alta | Triste |
| EP-004 | PrestamoService | **Dado** un material sin ejemplares disponibles, **cuando** un estudiante intenta prestarlo, **entonces** se rechaza con "material no disponible" y el stock no cambia. | Unitaria | Alta | Triste |
| EP-005 | PrestamoService | **Dado** un identificador de usuario que no existe, **cuando** se intenta registrar un préstamo, **entonces** el sistema responde "usuario no encontrado". | Unitaria | Alta | Triste |
| EP-006 | PrestamoService | **Dado** un préstamo activo, **cuando** se registra la devolución, **entonces** el préstamo pasa a "devuelto" y el stock aumenta en 1. | Integración | Alta | Feliz |
| EP-007 | MaterialController | **Dado** una solicitud `POST /api/materiales` con título vacío o ISBN inválido, **cuando** se envía, **entonces** responde 400 con el detalle del error. | API | Media | Triste |
| EP-008 | UsuarioController | **Dado** un usuario registrado, **cuando** se consulta `GET /api/usuarios/{id}`, **entonces** responde 200 con sus datos y sin información sensible. | API | Media | Feliz |
| EP-009 | RecomendacionService *(gancho innovador)* | **Dado** un usuario con historial de préstamos de un género, **cuando** pide recomendaciones, **entonces** recibe materiales disponibles de ese género que no tiene prestados. | Unitaria | Media | Feliz |
| EP-010 | AlertaVencimientoService *(gancho innovador)* | **Dado** un préstamo activo que vence mañana, **cuando** se ejecuta la verificación diaria, **entonces** se genera una alerta para ese usuario; los préstamos devueltos no generan alerta. | Unitaria | Media | Feliz |
| EP-011 | RecomendacionController *(gancho innovador)* | **Dado** un usuario inexistente, **cuando** se llama `GET /api/recomendaciones/{id}`, **entonces** responde 404 con mensaje claro. | API | Media | Triste |
| EP-012 | PrestamoController | **Dado** un flujo completo, **cuando** se registra usuario, material, préstamo y devolución en secuencia, **entonces** todos los pasos responden correctamente y el inventario queda consistente. | Integración | Alta | Feliz |

## Cobertura
- **Total:** 12 escenarios (mínimo exigido: 10).
- **Gancho innovador:** EP-009, EP-010, EP-011 (mínimo 2).
- **Camino triste:** EP-003, EP-004, EP-005, EP-007, EP-011 (mínimo 2).
- **Técnicas:** valores límite (EP-003), clases de equivalencia (EP-007: título válido/vacío).

## Datos de prueba sugeridos
- Usuario válido: `id=1, nombre="Ana Pérez"`; usuario inexistente: `id=999`.
- Material disponible: `stock=2`; material agotado: `stock=0`.
- Límite de préstamos: 2, 3 y 4 préstamos activos.
