# Plan de pruebas – BiblioUni (basado en IEEE 829)

## 1. Identificación del plan

| Campo | Valor |
|---|---|
| Identificador | PP-BIBLIOUNI-U2-001 |
| Nombre | Plan de pruebas formal y primeros tests unitarios de BiblioUni |
| Versión | 1.0 |
| Fecha | 2026-11-21 |
| Responsable del plan | [Nombre del estudiante] |
| Aprueba | [Nombre del docente] |
| Repositorio | https://github.com/Carlos00692/pruebas |
| Documentos relacionados | `estrategia.md` y `escenarios.md` (Unidad 1), `casos_de_prueba.md` (Unidad 2) |

## 2. Introducción

BiblioUni es un sistema de gestión de préstamos de material bibliográfico construido con Spring Boot. Este plan concreta la estrategia de la Unidad 1 en pruebas ejecutables: define qué se prueba, cómo, con qué criterios se empieza y se termina, y qué riesgos pueden afectar el resultado. Se aplica a la capa de servicio (`MaterialService`, `PrestamoService`, `UsuarioService`), aislada de los repositorios mediante Mockito.

## 3. Ítems a probar

| Ítem | Clase | Versión |
|---|---|---|
| Servicio de materiales | `com.bibliouni.servicio.MaterialService` | rama `feature/plan-tests` |
| Servicio de préstamos | `com.bibliouni.servicio.PrestamoService` | rama `feature/plan-tests` |
| Servicio de usuarios | `com.bibliouni.servicio.UsuarioService` | rama `feature/plan-tests` |

## 4. Características a probar y no probar

### 4.1 Se probarán

| ID | Funcionalidad | Reglas de negocio verificadas |
|---|---|---|
| RF-01 | Registrar material | Título obligatorio; ejemplares no negativos |
| RF-02 | Registrar préstamo | Usuario y material existentes; material disponible; máximo 3 préstamos activos; vencimiento a 7 días |
| RF-03 | Devolución y multa | Multa de 1 000 COP por cada día de retraso; sin multa a tiempo |
| RF-07 | Validación de datos | Rechazo de entradas inválidas |
| RF-08 | Consultar material por ID | Retorna vacío si no existe |
| RF-09 | Registrar usuario | Correo con formato válido |

Los identificadores RF-01, RF-02, RF-03 y RF-07 provienen de la matriz de trazabilidad de la Unidad 1. RF-08 y RF-09 se agregan en esta unidad.

### 4.2 No se probarán

| Elemento | Justificación |
|---|---|
| Controladores REST y pruebas de API | Se abordan en la Unidad 3; aún no existen endpoints de préstamos. |
| Persistencia real (JPA, H2) | Los repositorios son interfaces simuladas; la base de datos se integra después. |
| Gancho innovador (recomendaciones y alertas) | Depende de los repositorios reales; se planifica para la Unidad 3. |
| Rendimiento, carga y seguridad avanzada | Excluidos desde la estrategia de la Unidad 1. |

## 5. Enfoque de pruebas

| Nivel | Qué se verifica | Técnica | Herramientas |
|---|---|---|---|
| Unitario | Reglas de negocio de cada servicio, con el repositorio simulado | Caja negra: partición de equivalencia y valores límite | JUnit 5, Mockito |
| Unitario con TDD | Límite de préstamos activos y validación de correo | Ciclos Red-Green-Refactor con commits separados | JUnit 5, Mockito, Git |
| Integración | Servicio con repositorio real | Planificado para la Unidad 3 | Spring Boot Test, H2 |
| API | Endpoints REST | Planificado para la Unidad 3 | MockMvc, Postman |

Convenciones de las pruebas:

- Patrón AAA (Arrange, Act, Assert) en cada test.
- Nombres descriptivos en español con el formato `metodo_cuandoCondicion_entoncesResultado`.
- Aislamiento con `@ExtendWith(MockitoExtension.class)`, `@Mock` e `@InjectMocks`.
- Pruebas parametrizadas (`@ParameterizedTest`) para valores límite y clases inválidas.
- Convención de commits: `test(red)`, `feat(green)` y `refactor` para los ciclos TDD; `docs`, `test` y `feat` para el resto.

## 6. Criterios de entrada

| # | Condición | Cómo se verifica |
|---|---|---|
| E1 | El proyecto compila | `mvn compile` termina con `BUILD SUCCESS` |
| E2 | Existe una prueba base que pasa | `mvn test` con `BibliouniApplicationTests` en verde |
| E3 | Los casos de prueba están documentados | `casos_de_prueba.md` con al menos 15 casos |
| E4 | El entorno está disponible | JDK instalado y Maven 3.9 o superior |
| E5 | Existe la rama de trabajo | `feature/plan-tests` creada desde `develop` |

## 7. Criterios de salida y de aprobación o fallo

| # | Criterio | Meta cuantificable |
|---|---|---|
| S1 | Casos de prueba ejecutados | 100 % de los casos automatizables (24 de 24) |
| S2 | Pruebas unitarias aprobadas | 100 % en verde; mínimo 6 tests |
| S3 | Cobertura de líneas en servicios | Mayor a 60 % (JaCoCo, ver riesgo R2) |
| S4 | Defectos críticos abiertos | 0 |
| S5 | Densidad de defectos abiertos | No más de 5 por cada 1 000 líneas de código |
| S6 | Ciclos TDD evidenciados | Al menos 2, con commits `test(red)`, `feat(green)` y `refactor` |
| S7 | Entrega | Pull Request hacia `develop` con captura de `mvn test` |

Un caso aprueba cuando el resultado obtenido coincide con el esperado. Falla cuando difiere, o cuando el test no compila o lanza una excepción no prevista.

## 8. Criterios de suspensión y reanudación

| Se suspenden las pruebas si… | Se reanudan cuando… |
|---|---|
| El código no compila | `mvn compile` vuelve a terminar con éxito |
| Más del 30 % de los tests fallan por un mismo defecto | El defecto se corrige y la suite vuelve a ejecutarse completa |
| El entorno no resuelve dependencias de Maven | La conexión o el repositorio local se restablecen |

## 9. Entregables

- `docs/pruebas/plan_de_pruebas.md` (este documento).
- `docs/pruebas/casos_de_prueba.md` con 24 casos formales.
- `MaterialServiceTest`, `PrestamoServiceTest` y `UsuarioServiceTest`.
- Historial de commits con los dos ciclos TDD.
- Captura de `mvn test` y Pull Request hacia `develop`.

## 10. Recursos

### 10.1 Técnicos

| Recurso | Uso |
|---|---|
| Java 17 o superior (probado con Java 26) | Compilación y ejecución |
| Maven 3.9.9 | Construcción y ejecución de pruebas |
| Spring Boot 3.3.5 (`spring-boot-starter-test`) | Incluye JUnit 5, Mockito y AssertJ |
| JaCoCo | Medición de cobertura (pendiente de activar, ver R2) |
| Postman y H2 | Pruebas de API y base en memoria (Unidad 3) |
| Git y GitHub | Control de versiones y Pull Request |
| Visual Studio Code | Editor y terminal |

### 10.2 Humanos

| Rol | Responsable | Dedicación estimada |
|---|---|---|
| Diseñador y ejecutor de pruebas | [Nombre del estudiante] | 10 horas |
| Revisor | [Nombre del docente] | Revisión del Pull Request |

## 11. Tareas, esfuerzo y cronograma

| Fase | Tarea | Esfuerzo (h) | Entregable |
|---|---|---|---|
| 1 | Elaborar el plan de pruebas | 2,0 | `plan_de_pruebas.md` |
| 2 | Diseñar los casos de prueba | 2,5 | `casos_de_prueba.md` |
| 3 | Implementar pruebas unitarias con Mockito | 3,0 | Tests de `MaterialService` y `PrestamoService` |
| 4 | Aplicar TDD en dos funcionalidades | 1,5 | 6 commits Red-Green-Refactor |
| 5 | Verificar, subir y abrir el Pull Request | 1,0 | PR hacia `develop` |
| | **Total** | **10,0** | |

## 12. Riesgos y contingencias

Prioridad = probabilidad (1 a 3) × impacto (1 a 3). Un puntaje de 6 o más es riesgo alto.

| ID | Riesgo | Prob. | Impacto | Prioridad | Mitigación | Contingencia | Monitoreo |
|---|---|---|---|---|---|---|---|
| R1 | Mockito y Byte Buddy no soportan Java 26 | 3 | 3 | 9 | Configurar Surefire con `-Dnet.bytebuddy.experimental=true` | Ejecutar con JDK 21 | Resultado de cada `mvn test` |
| R2 | JaCoCo incompatible con Java 26 | 3 | 2 | 6 | Activarlo solo con JDK 17 o 21 | Estimar cobertura por casos mapeados a tests | Revisión previa a la entrega |
| R3 | Maven no descarga dependencias | 2 | 3 | 6 | Ejecutar `mvn test` una vez con conexión para llenar `~/.m2` | Usar un repositorio local o espejo | Primera ejecución |
| R4 | Stubs innecesarios hacen fallar tests por modo estricto de Mockito | 2 | 2 | 4 | Simular solo lo que cada camino usa | Usar `lenient()` de forma puntual | Mensajes `UnnecessaryStubbingException` |
| R5 | Regla del límite de préstamos ambigua | 2 | 2 | 4 | Fijarla en el plan: se rechaza desde el 4.º préstamo activo | Consultar con el docente | Revisión del plan |
| R6 | Falta de tiempo (10 horas) | 2 | 2 | 4 | Priorizar casos de prioridad Alta | Diferir casos de prioridad Baja | Esfuerzo por fase |
| R7 | Trabajo individual sin revisión cruzada | 3 | 1 | 3 | Autorrevisión con la lista de criterios de salida | Solicitar revisión del docente | Estado del PR |
| R8 | Pérdida de trabajo local | 1 | 3 | 3 | `git push` después de cada ciclo TDD | Recuperar desde GitHub | Historial remoto |

## 13. Control de cambios del plan

| Versión | Fecha | Cambio | Autor |
|---|---|---|---|
| 1.0 | 2026-11-21 | Versión inicial | [Nombre del estudiante] |

Todo cambio en alcance, criterios o riesgos se registra en esta tabla y se confirma con un commit con prefijo `docs:`.

## 14. Aprobaciones

| Rol | Nombre | Firma | Fecha |
|---|---|---|---|
| Responsable | [Nombre del estudiante] | | |
| Aprueba | [Nombre del docente] | | |
