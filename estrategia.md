# Estrategia de pruebas – BiblioUni

> **Nota:** el "gancho innovador" se asume como un **módulo de recomendación de materiales y alertas de vencimiento de préstamos**

## 1. Objetivo y alcance
Definir qué, cómo y con qué nivel de profundidad se probará BiblioUni (sistema de gestión de préstamos de material bibliográfico, construido con Spring Boot y API REST), para asegurar la calidad antes de las Unidades 2 y 3.

## 2. ¿Qué se va a probar?
| Componente | Descripción | Responsable |
|---|---|---|
| Modelo (entidades) | `Material`, `Usuario`, `Prestamo` y sus validaciones | Equipo de desarrollo |
| Servicios | `MaterialService`, `PrestamoService`, `UsuarioService` (reglas de negocio) | Equipo de desarrollo |
| Controladores REST | `MaterialController`, `PrestamoController`, `UsuarioController` | Equipo de desarrollo |
| Base de datos | Repositorios JPA y consultas | Equipo de desarrollo |
| Gancho innovador | `RecomendacionService` y `AlertaVencimientoService` | Equipo de desarrollo |

## 3. Tipos de pruebas y justificación
| Tipo | Aplica | Por qué |
|---|---|---|
| Unitarias | Sí | Validar reglas de negocio de los servicios de forma aislada (Mockito). |
| Integración | Sí | Verificar la comunicación controlador–servicio–repositorio. |
| Funcionales | Sí | Comprobar que los requisitos (préstamo, devolución, búsqueda) se cumplen. |
| De API | Sí | Validar códigos HTTP, JSON y manejo de errores (MockMvc). |
| No funcionales | Parcial | Se verifican tiempos de respuesta básicos y validación de entradas. |

## 4. Niveles de prueba
| Nivel | Qué se prueba | Herramientas | Criterio de entrada | Criterio de salida |
|---|---|---|---|---|
| Unitaria | Servicios y reglas de negocio | JUnit 5, Mockito | Código compila | ≥ 70 % cobertura en servicios, 0 fallos |
| Integración | Controladores + servicios + BD | Spring Boot Test, MockMvc, H2 | Pruebas unitarias en verde | Endpoints principales responden correctamente |
| Sistema | Flujos completos (registro → préstamo → devolución) | Postman / pruebas E2E | Integración en verde | Flujos críticos sin defectos de severidad alta |
| Aceptación | Validación contra requisitos del usuario | Revisión manual con historias de usuario | Sistema en verde | Aprobación del docente / cliente |

## 5. Gancho innovador: cómo se probará
- **Unitarias:** `RecomendacionService` con datos controlados (usuario con historial de un género → recomienda materiales del mismo género; usuario sin historial → devuelve los más prestados).
- **Unitarias:** `AlertaVencimientoService` con fechas simuladas (reloj inyectable) para préstamos que vencen hoy, mañana y ya vencidos.
- **Integración/API:** `GET /api/recomendaciones/{usuarioId}` devuelve 200 con lista, 404 si el usuario no existe.
- **Criterios de prueba:** no recomendar materiales no disponibles; no repetir materiales ya prestados activos; las alertas se generan solo para préstamos activos.

## 6. ¿Qué NO se probará y por qué?
| Excluido | Justificación |
|---|---|
| Pruebas de carga y estrés | Fuera del alcance académico; no hay infraestructura de producción. |
| Seguridad avanzada (pentesting) | Requiere herramientas y conocimientos especializados; solo se validan entradas básicas. |
| Compatibilidad multi-navegador | BiblioUni expone una API REST; no hay interfaz web en este alcance. |
| Frameworks externos (Spring, Hibernate) | Ya cuentan con sus propias pruebas. |

## 7. Análisis de riesgos por componente
| Componente | Riesgo | Impacto | Probabilidad | Mitigación |
|---|---|---|---|---|
| PrestamoService | Superar el límite de préstamos activos | Alto | Media | Pruebas unitarias con valores límite (2, 3, 4 préstamos). |
| MaterialService | Prestar material no disponible | Alto | Media | Pruebas de camino triste. |
| Controladores | Datos inválidos sin validar | Medio | Alta | Pruebas de API con entradas inválidas. |
| Repositorios | Consultas incorrectas | Medio | Baja | Pruebas de integración con H2. |
| Gancho innovador | Recomendaciones erróneas | Medio | Media | Datos de prueba controlados. |

## 8. Matriz de trazabilidad requisito–prueba
| Requisito (ajusta a tus HU) | Escenarios |
|---|---|
| RF-01 Registrar material | EP-001, EP-007 |
| RF-02 Registrar préstamo | EP-002, EP-003, EP-004, EP-005 |
| RF-03 Devolver material | EP-006 |
| RF-04 Consultar usuario | EP-008 |
| RF-05 Recomendaciones (gancho) | EP-009 |
| RF-06 Alertas de vencimiento (gancho) | EP-010 |
| RF-07 Validación de datos | EP-007, EP-011 |

## 9. Métricas de cobertura esperadas
- Servicios: ≥ 70 % de cobertura de líneas (JaCoCo).
- Controladores: ≥ 60 %.
- 100 % de los escenarios de prioridad Alta automatizados en la Unidad 2.
