# Evaluación de características de calidad ISO/IEC 25010 – BiblioUni

Referencia: ISO/IEC 25010:2011 (modelo de calidad del producto). Nota: la norma define 8 características; "Funcionalidad" corresponde a *Adecuación funcional*, "Rendimiento" a *Eficiencia de desempeño* y "Fiabilidad" a *Fiabilidad*.

## 1. Tabla de evaluación

| Característica | Relevancia | Subcaracterísticas relevantes | Justificación | Ejemplo de medición en BiblioUni | Técnica de prueba | Umbral aceptable |
|---|---|---|---|---|---|---|
| **Funcionalidad** (adecuación funcional) | **Alta** | Completitud, corrección | El sistema existe para gestionar materiales y préstamos; si las reglas fallan, el sistema no cumple su propósito. | % de requisitos funcionales que pasan sus pruebas; un estudiante no puede tener más de 3 préstamos activos. | Unitarias, integración, API (caja negra) | ≥ 95 % de casos en verde |
| **Rendimiento** (eficiencia de desempeño) | **Media** | Comportamiento temporal | Uso concurrente bajo (comunidad universitaria), pero la búsqueda debe ser ágil. | Tiempo de respuesta de `GET /api/materiales` con 1 000 registros. | Prueba de API con medición de tiempos | ≤ 500 ms |
| **Compatibilidad** | **Baja** | Interoperabilidad | Es una API REST autocontenida; solo debe devolver JSON estándar. | Un cliente (Postman/front) consume la API sin adaptaciones. | Pruebas de API | Respuestas JSON válidas 100 % |
| **Usabilidad** | **Media** | Capacidad de reconocimiento, protección ante errores | No hay interfaz gráfica en este alcance, pero los mensajes de error de la API deben ser claros. | Los errores devuelven código HTTP correcto y mensaje comprensible. | Pruebas de API de camino triste | 100 % de errores con mensaje |
| **Fiabilidad** | **Alta** | Madurez, tolerancia a fallos | Un préstamo mal registrado o un material duplicado genera inconsistencias en el inventario. | Tras un préstamo fallido no cambia el stock del material. | Integración con BD (transacciones) | 0 inconsistencias de datos |
| **Seguridad** | **Alta** | Confidencialidad, integridad | Se manejan datos personales de usuarios (estudiantes). | Datos inválidos o inyecciones simples (`' OR 1=1`) son rechazados; no se exponen datos sensibles. | Pruebas de validación de entradas | 0 vulnerabilidades básicas detectadas |
| **Mantenibilidad** | **Media** | Modularidad, capacidad de ser probado | Habrá cambios entre unidades; el código debe poder probarse y modificarse fácilmente. | Cobertura de pruebas y complejidad ciclomática por método. | Análisis estático, JaCoCo | Cobertura ≥ 70 %, complejidad ≤ 10 |
| **Portabilidad** | **Baja** | Instalabilidad | Se ejecuta con Maven y Java en cualquier equipo; no hay requisito multiplataforma especial. | `mvn test` pasa en otra máquina del equipo. | Ejecución en distinto entorno | Pasa en 2 de 2 equipos |

## 2. Las 3 características más críticas

### 1) Funcionalidad (Alta)
- **Cómo se aborda:** pruebas unitarias de las reglas de negocio de `PrestamoService` y `MaterialService`, y pruebas de API para cada endpoint.
- **Técnicas:** partición de equivalencia y valores límite (por ejemplo, 2, 3 y 4 préstamos activos).

### 2) Fiabilidad (Alta)
- **Cómo se aborda:** pruebas de integración con base de datos H2 verificando que ante un error no queden datos a medias (rollback).
- **Técnicas:** escenarios de camino triste (material no disponible, usuario inexistente).

### 3) Seguridad (Alta)
- **Cómo se aborda:** pruebas de API con entradas inválidas o maliciosas y verificación de que no se exponen datos personales.
- **Alcance:** validación básica de entradas; la seguridad avanzada (pentesting) queda fuera.

## 3. Matriz de criticidad (impacto × probabilidad de falla)

| Característica | Impacto (1-3) | Probabilidad (1-3) | Puntaje | Prioridad |
|---|---|---|---|---|
| Funcionalidad | 3 | 3 | 9 | 1 |
| Fiabilidad | 3 | 2 | 6 | 2 |
| Seguridad | 3 | 2 | 6 | 3 |
| Rendimiento | 2 | 2 | 4 | 4 |
| Mantenibilidad | 2 | 2 | 4 | 5 |
| Usabilidad | 2 | 1 | 2 | 6 |
| Compatibilidad | 1 | 1 | 1 | 7 |
| Portabilidad | 1 | 1 | 1 | 8 |

## 4. Trade-offs entre características
- **Seguridad vs. Rendimiento:** validar cada entrada agrega milisegundos, aceptable dentro del umbral de 500 ms.
- **Fiabilidad vs. Rendimiento:** las transacciones garantizan consistencia pero añaden costo; se prioriza la consistencia.
- **Mantenibilidad vs. Funcionalidad:** más funciones aumentan la complejidad; se controla con cobertura y revisiones.

## 5. Referencia
ISO/IEC 25010:2011. *Systems and software engineering – Systems and software Quality Requirements and Evaluation (SQuaRE) – System and software quality models.*
