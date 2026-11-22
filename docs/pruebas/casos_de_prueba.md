# Casos de prueba formales – BiblioUni (Unidad 2)

Documento asociado a `plan_de_pruebas.md` (PP-BIBLIOUNI-U2-001). Cada caso indica la técnica aplicada, su prioridad, el requisito que cubre, el escenario de la Unidad 1 del que proviene y el test automatizado que lo ejecuta.

## 1. Resumen

| Indicador | Valor |
|---|---|
| Casos de prueba | 24 |
| Con partición de equivalencia | 15 |
| Con valores límite | 9 |
| Caminos tristes | 13 |
| Prioridad Alta / Media | 15 / 9 |
| Automatizados | 24 de 24 |

## 2. Requisitos considerados

| ID | Requisito | Origen |
|---|---|---|
| RF-01 | Registrar material | Unidad 1 |
| RF-02 | Registrar préstamo (incluye límite de 3 préstamos activos) | Unidad 1 |
| RF-03 | Devolución y cálculo de multa (1 000 COP por día de retraso) | Unidad 1 |
| RF-07 | Validación de datos | Unidad 1 |
| RF-08 | Consultar material por ID | Unidad 2 |
| RF-09 | Registrar usuario con correo válido | Unidad 2 |

## 3. Análisis de las entradas

### 3.1 Matriz de clases de equivalencia

| Variable | Clase | Tipo | Representante | Casos |
|---|---|---|---|---|
| Título del material | No vacío | Válida | "Introducción a las pruebas de software" | CP-001 |
| Título del material | Vacío | Inválida | "" | CP-002 |
| Título del material | Nulo o en blanco | Inválida | `null`, "   " | CP-003 |
| ID de material | Existente | Válida | 10 | CP-007 |
| ID de material | Inexistente | Inválida | 999 | CP-008, CP-015 |
| Usuario del préstamo | Existente | Válida | 1 | CP-009 |
| Usuario del préstamo | Inexistente | Inválida | 999 | CP-014 |
| Días de retraso | Menor o igual a 0 | Válida (sin multa) | -1, 0 | CP-016, CP-017 |
| Días de retraso | Mayor a 0 | Válida (con multa) | 1, 5 | CP-018, CP-019 |
| Correo | usuario@dominio.extensión | Válida | ana.perez@uniempresarial.edu.co | CP-021 |
| Correo | Sin arroba o con espacios | Inválida | ana.perez.uniempresarial.edu.co | CP-022 |
| Correo | Parte local o dominio incompletos | Inválida | ana@, @mail.com, ana@mail | CP-023 |
| Correo | Nulo o vacío | Inválida | `null`, "" | CP-024 |

### 3.2 Valores límite

**Ejemplares de un material** (rango válido: 0 o más):

| Valor | Posición | Resultado | Caso |
|---|---|---|---|
| -1 | mín - 1 | Rechazado | CP-004 |
| 0 | mín | Aceptado, no disponible | CP-005 |
| 1 | mín + 1 | Aceptado, disponible | CP-006 |

**Préstamos activos previos del usuario** (se rechaza desde 3):

| Valor | Posición | Resultado | Caso |
|---|---|---|---|
| 0 | mín | Préstamo registrado | CP-009 |
| 2 | máx - 1 | Préstamo registrado (el 3.º) | CP-011 |
| 3 | máx | Rechazado | CP-012 |
| 4 | máx + 1 | Rechazado | CP-013 |

**Días de retraso en la devolución** (vencimiento 2026-11-10):

| Fecha de devolución | Días de retraso | Multa (COP) | Caso |
|---|---|---|---|
| 2026-11-09 | -1 | 0 | CP-016 |
| 2026-11-10 | 0 | 0 | CP-017 |
| 2026-11-11 | 1 | 1 000 | CP-018 |
| 2026-11-15 | 5 | 5 000 | CP-019 |

### 3.3 Tabla de decisión: registrar préstamo

| Condición / acción | R1 | R2 | R3 | R4 | R5 |
|---|---|---|---|---|---|
| Usuario existe | N | S | S | S | S |
| Préstamos activos menores a 3 | – | N | S | S | S |
| Material existe | – | – | N | S | S |
| Material con ejemplares | – | – | – | N | S |
| **Resultado** | Usuario no encontrado | Límite excedido | Material no encontrado | Material no disponible | Préstamo registrado |
| **Caso** | CP-014 | CP-012, CP-013 | CP-015 | CP-010 | CP-009, CP-011 |

## 4. Casos de prueba

### CP-001 – Registrar material

| Campo | Detalle |
|---|---|
| ID | CP-001 |
| Funcionalidad bajo prueba | Registrar material |
| Precondiciones | Repositorio de materiales simulado y disponible. |
| Datos de entrada | Título "Introducción a las pruebas de software"; autor "Ana Pérez"; ISBN "978-0000000001"; ejemplares 3. |
| Pasos a ejecutar | 1. Construir el material con los datos.<br>2. Invocar `MaterialService.crear(material)`. |
| Resultado esperado | Retorna el material guardado con 3 ejemplares disponibles y el repositorio recibe una llamada a `guardar`. |
| Postcondición | Material registrado y disponible para préstamo. |
| Técnica aplicada | Partición de equivalencia (clase válida: título no vacío y ejemplares ≥ 0) |
| Tipo | Feliz |
| Prioridad | Alta |
| Requisito | RF-01 |
| Escenario de la Unidad 1 | EP-001 |
| Test automatizado | `MaterialServiceTest#crear_cuandoDatosValidos_guardaYRetornaMaterial` |

### CP-002 – Registrar material

| Campo | Detalle |
|---|---|
| ID | CP-002 |
| Funcionalidad bajo prueba | Registrar material |
| Precondiciones | Repositorio de materiales simulado. |
| Datos de entrada | Título "" (cadena vacía); autor "Ana Pérez"; ejemplares 2. |
| Pasos a ejecutar | 1. Construir el material con título vacío.<br>2. Invocar `MaterialService.crear(material)`. |
| Resultado esperado | Lanza `IllegalArgumentException` y no llama a `guardar`. |
| Postcondición | No se registra ningún material. |
| Técnica aplicada | Partición de equivalencia (clase inválida: título vacío) |
| Tipo | Triste |
| Prioridad | Alta |
| Requisito | RF-01, RF-07 |
| Escenario de la Unidad 1 | EP-007 |
| Test automatizado | `MaterialServiceTest#crear_cuandoTituloVacioONulo_lanzaExcepcion` |

### CP-003 – Registrar material

| Campo | Detalle |
|---|---|
| ID | CP-003 |
| Funcionalidad bajo prueba | Registrar material |
| Precondiciones | Repositorio de materiales simulado. |
| Datos de entrada | Título `null` y, en otra ejecución, título "   " (solo espacios); ejemplares 2. |
| Pasos a ejecutar | 1. Construir el material con título nulo o en blanco.<br>2. Invocar `MaterialService.crear(material)`. |
| Resultado esperado | Lanza `IllegalArgumentException` y no llama a `guardar`. |
| Postcondición | No se registra ningún material. |
| Técnica aplicada | Partición de equivalencia (clase inválida: título nulo o en blanco) |
| Tipo | Triste |
| Prioridad | Media |
| Requisito | RF-01, RF-07 |
| Escenario de la Unidad 1 | EP-007 |
| Test automatizado | `MaterialServiceTest#crear_cuandoTituloVacioONulo_lanzaExcepcion` |

### CP-004 – Registrar material

| Campo | Detalle |
|---|---|
| ID | CP-004 |
| Funcionalidad bajo prueba | Registrar material |
| Precondiciones | Repositorio de materiales simulado. |
| Datos de entrada | Título "Álgebra lineal"; ejemplares -1. |
| Pasos a ejecutar | 1. Construir el material con ejemplares negativos.<br>2. Invocar `MaterialService.crear(material)`. |
| Resultado esperado | Lanza `IllegalArgumentException` y no llama a `guardar`. |
| Postcondición | No se registra ningún material. |
| Técnica aplicada | Valores límite (mín - 1 del rango de ejemplares, con mín = 0) |
| Tipo | Triste |
| Prioridad | Alta |
| Requisito | RF-01, RF-07 |
| Escenario de la Unidad 1 | EP-007 |
| Test automatizado | `MaterialServiceTest#crear_cuandoEjemplaresNegativos_lanzaExcepcion` |

### CP-005 – Registrar material

| Campo | Detalle |
|---|---|
| ID | CP-005 |
| Funcionalidad bajo prueba | Registrar material |
| Precondiciones | Repositorio de materiales simulado. |
| Datos de entrada | Título "Álgebra lineal"; ejemplares 0. |
| Pasos a ejecutar | 1. Construir el material con 0 ejemplares.<br>2. Invocar `MaterialService.crear(material)`. |
| Resultado esperado | Retorna el material guardado con 0 ejemplares y llama a `guardar` una vez. |
| Postcondición | Material registrado pero no disponible para préstamo. |
| Técnica aplicada | Valores límite (mín del rango de ejemplares) |
| Tipo | Feliz |
| Prioridad | Media |
| Requisito | RF-01 |
| Escenario de la Unidad 1 | EP-001 |
| Test automatizado | `MaterialServiceTest#crear_cuandoEjemplaresEnLimiteValido_guardaMaterial` |

### CP-006 – Registrar material

| Campo | Detalle |
|---|---|
| ID | CP-006 |
| Funcionalidad bajo prueba | Registrar material |
| Precondiciones | Repositorio de materiales simulado. |
| Datos de entrada | Título "Álgebra lineal"; ejemplares 1. |
| Pasos a ejecutar | 1. Construir el material con 1 ejemplar.<br>2. Invocar `MaterialService.crear(material)`. |
| Resultado esperado | Retorna el material guardado con 1 ejemplar disponible. |
| Postcondición | Material registrado y disponible. |
| Técnica aplicada | Valores límite (mín + 1 del rango de ejemplares) |
| Tipo | Feliz |
| Prioridad | Media |
| Requisito | RF-01 |
| Escenario de la Unidad 1 | EP-001 |
| Test automatizado | `MaterialServiceTest#crear_cuandoEjemplaresEnLimiteValido_guardaMaterial` |

### CP-007 – Consultar material por ID

| Campo | Detalle |
|---|---|
| ID | CP-007 |
| Funcionalidad bajo prueba | Consultar material por ID |
| Precondiciones | Existe el material con ID 10 en el repositorio simulado. |
| Datos de entrada | ID 10. |
| Pasos a ejecutar | 1. Configurar el repositorio para devolver el material con ID 10.<br>2. Invocar `MaterialService.buscarPorId(10L)`. |
| Resultado esperado | Retorna un `Optional` con el material de ID 10. |
| Postcondición | Sin cambios en los datos. |
| Técnica aplicada | Partición de equivalencia (clase válida: ID existente) |
| Tipo | Feliz |
| Prioridad | Alta |
| Requisito | RF-08 |
| Escenario de la Unidad 1 | – |
| Test automatizado | `MaterialServiceTest#buscarPorId_cuandoExiste_retornaMaterial` |

### CP-008 – Consultar material por ID

| Campo | Detalle |
|---|---|
| ID | CP-008 |
| Funcionalidad bajo prueba | Consultar material por ID |
| Precondiciones | No existe el material con ID 999. |
| Datos de entrada | ID 999. |
| Pasos a ejecutar | 1. Configurar el repositorio para devolver vacío.<br>2. Invocar `MaterialService.buscarPorId(999L)`. |
| Resultado esperado | Retorna un `Optional` vacío, sin lanzar excepción. |
| Postcondición | Sin cambios en los datos. |
| Técnica aplicada | Partición de equivalencia (clase inválida: ID inexistente) |
| Tipo | Triste |
| Prioridad | Alta |
| Requisito | RF-08 |
| Escenario de la Unidad 1 | – |
| Test automatizado | `MaterialServiceTest#buscarPorId_cuandoNoExiste_retornaVacio` |

### CP-009 – Registrar préstamo

| Campo | Detalle |
|---|---|
| ID | CP-009 |
| Funcionalidad bajo prueba | Registrar préstamo |
| Precondiciones | Usuario 1 existe con 0 préstamos activos; material 10 con 2 ejemplares. |
| Datos de entrada | Usuario 1; material 10; fecha de préstamo 2026-11-03. |
| Pasos a ejecutar | 1. Configurar los repositorios con el usuario y el material.<br>2. Invocar `PrestamoService.registrarPrestamo(1L, 10L, fecha)`. |
| Resultado esperado | Retorna un préstamo con vencimiento 2026-11-10; el material queda con 1 ejemplar y se guardan material y préstamo. |
| Postcondición | Préstamo activo registrado; stock reducido en 1. |
| Técnica aplicada | Partición de equivalencia (clase válida: usuario y material existentes, material disponible) |
| Tipo | Feliz |
| Prioridad | Alta |
| Requisito | RF-02 |
| Escenario de la Unidad 1 | EP-002 |
| Test automatizado | `PrestamoServiceTest#registrarPrestamo_cuandoDatosValidos_creaPrestamoYDescuentaEjemplar` |

### CP-010 – Registrar préstamo

| Campo | Detalle |
|---|---|
| ID | CP-010 |
| Funcionalidad bajo prueba | Registrar préstamo |
| Precondiciones | Usuario 1 existe; material 10 con 0 ejemplares. |
| Datos de entrada | Usuario 1; material 10; fecha 2026-11-03. |
| Pasos a ejecutar | 1. Configurar los repositorios con el material agotado.<br>2. Invocar `PrestamoService.registrarPrestamo(1L, 10L, fecha)`. |
| Resultado esperado | Lanza `MaterialNoDisponibleException`; no se guarda ningún préstamo ni cambia el stock. |
| Postcondición | Stock sin cambios; ningún préstamo nuevo. |
| Técnica aplicada | Valores límite (stock en 0, límite inferior de disponibilidad) |
| Tipo | Triste |
| Prioridad | Alta |
| Requisito | RF-02 |
| Escenario de la Unidad 1 | EP-004 |
| Test automatizado | `PrestamoServiceTest#registrarPrestamo_cuandoMaterialNoDisponible_lanzaExcepcion` |

### CP-011 – Registrar préstamo (límite de préstamos activos)

| Campo | Detalle |
|---|---|
| ID | CP-011 |
| Funcionalidad bajo prueba | Registrar préstamo (límite de préstamos activos) |
| Precondiciones | Usuario 1 existe con 2 préstamos activos; material 10 con 2 ejemplares. |
| Datos de entrada | Préstamos activos previos = 2. |
| Pasos a ejecutar | 1. Configurar el conteo de préstamos activos en 2.<br>2. Invocar `PrestamoService.registrarPrestamo(1L, 10L, fecha)`. |
| Resultado esperado | Registra el préstamo (es el 3.º, permitido). |
| Postcondición | El usuario queda con 3 préstamos activos. |
| Técnica aplicada | Valores límite (máx - 1 antes del rechazo) |
| Tipo | Feliz |
| Prioridad | Alta |
| Requisito | RF-02 |
| Escenario de la Unidad 1 | EP-003 |
| Test automatizado | `PrestamoServiceTest#registrarPrestamo_cuandoTieneDosPrestamosActivos_permiteTercero` |

### CP-012 – Registrar préstamo (límite de préstamos activos)

| Campo | Detalle |
|---|---|
| ID | CP-012 |
| Funcionalidad bajo prueba | Registrar préstamo (límite de préstamos activos) |
| Precondiciones | Usuario 1 existe con 3 préstamos activos. |
| Datos de entrada | Préstamos activos previos = 3. |
| Pasos a ejecutar | 1. Configurar el conteo de préstamos activos en 3.<br>2. Invocar `PrestamoService.registrarPrestamo(1L, 10L, fecha)`. |
| Resultado esperado | Lanza `LimitePrestamosExcedidoException`; no se guarda ningún préstamo. |
| Postcondición | El usuario conserva 3 préstamos activos. |
| Técnica aplicada | Valores límite (máx: primer valor rechazado) |
| Tipo | Triste |
| Prioridad | Alta |
| Requisito | RF-02, RF-07 |
| Escenario de la Unidad 1 | EP-003 |
| Test automatizado | `PrestamoServiceTest#registrarPrestamo_cuandoSuperaElLimiteDePrestamos_lanzaExcepcion` |

### CP-013 – Registrar préstamo (límite de préstamos activos)

| Campo | Detalle |
|---|---|
| ID | CP-013 |
| Funcionalidad bajo prueba | Registrar préstamo (límite de préstamos activos) |
| Precondiciones | Usuario 1 existe con 4 préstamos activos. |
| Datos de entrada | Préstamos activos previos = 4. |
| Pasos a ejecutar | 1. Configurar el conteo de préstamos activos en 4.<br>2. Invocar `PrestamoService.registrarPrestamo(1L, 10L, fecha)`. |
| Resultado esperado | Lanza `LimitePrestamosExcedidoException`; no se guarda ningún préstamo. |
| Postcondición | El usuario conserva sus 4 préstamos activos. |
| Técnica aplicada | Valores límite (máx + 1) |
| Tipo | Triste |
| Prioridad | Media |
| Requisito | RF-02, RF-07 |
| Escenario de la Unidad 1 | EP-003 |
| Test automatizado | `PrestamoServiceTest#registrarPrestamo_cuandoSuperaElLimiteDePrestamos_lanzaExcepcion` |

### CP-014 – Registrar préstamo

| Campo | Detalle |
|---|---|
| ID | CP-014 |
| Funcionalidad bajo prueba | Registrar préstamo |
| Precondiciones | No existe el usuario 999. |
| Datos de entrada | Usuario 999; material 10. |
| Pasos a ejecutar | 1. Configurar el repositorio de usuarios para devolver vacío.<br>2. Invocar `PrestamoService.registrarPrestamo(999L, 10L, fecha)`. |
| Resultado esperado | Lanza `RecursoNoEncontradoException` y no consulta el material ni guarda nada. |
| Postcondición | Sin cambios en los datos. |
| Técnica aplicada | Partición de equivalencia (clase inválida: usuario inexistente) |
| Tipo | Triste |
| Prioridad | Alta |
| Requisito | RF-02 |
| Escenario de la Unidad 1 | EP-005 |
| Test automatizado | `PrestamoServiceTest#registrarPrestamo_cuandoUsuarioNoExiste_lanzaExcepcion` |

### CP-015 – Registrar préstamo

| Campo | Detalle |
|---|---|
| ID | CP-015 |
| Funcionalidad bajo prueba | Registrar préstamo |
| Precondiciones | Usuario 1 existe; no existe el material 999. |
| Datos de entrada | Usuario 1; material 999. |
| Pasos a ejecutar | 1. Configurar el repositorio de materiales para devolver vacío.<br>2. Invocar `PrestamoService.registrarPrestamo(1L, 999L, fecha)`. |
| Resultado esperado | Lanza `RecursoNoEncontradoException` y no guarda ningún préstamo. |
| Postcondición | Sin cambios en los datos. |
| Técnica aplicada | Partición de equivalencia (clase inválida: material inexistente) |
| Tipo | Triste |
| Prioridad | Media |
| Requisito | RF-02 |
| Escenario de la Unidad 1 | EP-005 |
| Test automatizado | `PrestamoServiceTest#registrarPrestamo_cuandoMaterialNoExiste_lanzaExcepcion` |

### CP-016 – Calcular multa por devolución tardía

| Campo | Detalle |
|---|---|
| ID | CP-016 |
| Funcionalidad bajo prueba | Calcular multa por devolución tardía |
| Precondiciones | Préstamo 1 con vencimiento 2026-11-10. |
| Datos de entrada | Fecha de devolución 2026-11-09 (un día antes). |
| Pasos a ejecutar | 1. Configurar el repositorio con el préstamo.<br>2. Invocar `PrestamoService.calcularMulta(1L, 2026-11-09)`. |
| Resultado esperado | Retorna 0. |
| Postcondición | Sin cambios en los datos. |
| Técnica aplicada | Partición de equivalencia (clase válida: devolución a tiempo) |
| Tipo | Feliz |
| Prioridad | Media |
| Requisito | RF-03 |
| Escenario de la Unidad 1 | EP-006 |
| Test automatizado | `PrestamoServiceTest#calcularMulta_cuandoSeDevuelveEnUnaFecha_retornaLaMultaEsperada` |

### CP-017 – Calcular multa por devolución tardía

| Campo | Detalle |
|---|---|
| ID | CP-017 |
| Funcionalidad bajo prueba | Calcular multa por devolución tardía |
| Precondiciones | Préstamo 1 con vencimiento 2026-11-10. |
| Datos de entrada | Fecha de devolución 2026-11-10 (el día del vencimiento). |
| Pasos a ejecutar | 1. Configurar el repositorio con el préstamo.<br>2. Invocar `PrestamoService.calcularMulta(1L, 2026-11-10)`. |
| Resultado esperado | Retorna 0. |
| Postcondición | Sin cambios en los datos. |
| Técnica aplicada | Valores límite (último día sin multa) |
| Tipo | Feliz |
| Prioridad | Alta |
| Requisito | RF-03 |
| Escenario de la Unidad 1 | EP-006 |
| Test automatizado | `PrestamoServiceTest#calcularMulta_cuandoSeDevuelveEnUnaFecha_retornaLaMultaEsperada` |

### CP-018 – Calcular multa por devolución tardía

| Campo | Detalle |
|---|---|
| ID | CP-018 |
| Funcionalidad bajo prueba | Calcular multa por devolución tardía |
| Precondiciones | Préstamo 1 con vencimiento 2026-11-10. |
| Datos de entrada | Fecha de devolución 2026-11-11 (un día tarde). |
| Pasos a ejecutar | 1. Configurar el repositorio con el préstamo.<br>2. Invocar `PrestamoService.calcularMulta(1L, 2026-11-11)`. |
| Resultado esperado | Retorna 1 000. |
| Postcondición | Sin cambios en los datos. |
| Técnica aplicada | Valores límite (primer día con multa) |
| Tipo | Feliz |
| Prioridad | Alta |
| Requisito | RF-03 |
| Escenario de la Unidad 1 | EP-006 |
| Test automatizado | `PrestamoServiceTest#calcularMulta_cuandoSeDevuelveEnUnaFecha_retornaLaMultaEsperada` |

### CP-019 – Calcular multa por devolución tardía

| Campo | Detalle |
|---|---|
| ID | CP-019 |
| Funcionalidad bajo prueba | Calcular multa por devolución tardía |
| Precondiciones | Préstamo 1 con vencimiento 2026-11-10. |
| Datos de entrada | Fecha de devolución 2026-11-15 (5 días tarde). |
| Pasos a ejecutar | 1. Configurar el repositorio con el préstamo.<br>2. Invocar `PrestamoService.calcularMulta(1L, 2026-11-15)`. |
| Resultado esperado | Retorna 5 000 (5 días × 1 000). |
| Postcondición | Sin cambios en los datos. |
| Técnica aplicada | Partición de equivalencia (clase válida: devolución tardía de varios días) |
| Tipo | Feliz |
| Prioridad | Alta |
| Requisito | RF-03 |
| Escenario de la Unidad 1 | EP-006 |
| Test automatizado | `PrestamoServiceTest#calcularMulta_cuandoSeDevuelveEnUnaFecha_retornaLaMultaEsperada` |

### CP-020 – Calcular multa por devolución tardía

| Campo | Detalle |
|---|---|
| ID | CP-020 |
| Funcionalidad bajo prueba | Calcular multa por devolución tardía |
| Precondiciones | No existe el préstamo 999. |
| Datos de entrada | Préstamo 999; fecha 2026-11-15. |
| Pasos a ejecutar | 1. Configurar el repositorio para devolver vacío.<br>2. Invocar `PrestamoService.calcularMulta(999L, fecha)`. |
| Resultado esperado | Lanza `RecursoNoEncontradoException`. |
| Postcondición | Sin cambios en los datos. |
| Técnica aplicada | Partición de equivalencia (clase inválida: préstamo inexistente) |
| Tipo | Triste |
| Prioridad | Media |
| Requisito | RF-03 |
| Escenario de la Unidad 1 | EP-006 |
| Test automatizado | `PrestamoServiceTest#calcularMulta_cuandoPrestamoNoExiste_lanzaExcepcion` |

### CP-021 – Registrar usuario

| Campo | Detalle |
|---|---|
| ID | CP-021 |
| Funcionalidad bajo prueba | Registrar usuario |
| Precondiciones | Repositorio de usuarios simulado. |
| Datos de entrada | Nombre "Ana Pérez"; correo "ana.perez@uniempresarial.edu.co". |
| Pasos a ejecutar | 1. Construir el usuario.<br>2. Invocar `UsuarioService.registrar(usuario)`. |
| Resultado esperado | Retorna el usuario guardado y llama a `guardar` una vez. |
| Postcondición | Usuario registrado. |
| Técnica aplicada | Partición de equivalencia (clase válida: correo con formato usuario@dominio.extensión) |
| Tipo | Feliz |
| Prioridad | Alta |
| Requisito | RF-09, RF-07 |
| Escenario de la Unidad 1 | – |
| Test automatizado | `UsuarioServiceTest#registrar_cuandoCorreoValido_guardaUsuario` |

### CP-022 – Registrar usuario

| Campo | Detalle |
|---|---|
| ID | CP-022 |
| Funcionalidad bajo prueba | Registrar usuario |
| Precondiciones | Repositorio de usuarios simulado. |
| Datos de entrada | Correo "ana.perez.uniempresarial.edu.co" (sin arroba) y "ana perez@mail.com" (con espacio). |
| Pasos a ejecutar | 1. Construir el usuario con el correo inválido.<br>2. Invocar `UsuarioService.registrar(usuario)`. |
| Resultado esperado | Lanza `IllegalArgumentException` y no llama a `guardar`. |
| Postcondición | No se registra el usuario. |
| Técnica aplicada | Partición de equivalencia (clase inválida: sin arroba o con espacios) |
| Tipo | Triste |
| Prioridad | Alta |
| Requisito | RF-09, RF-07 |
| Escenario de la Unidad 1 | EP-007 |
| Test automatizado | `UsuarioServiceTest#registrar_cuandoCorreoInvalido_lanzaExcepcion` |

### CP-023 – Registrar usuario

| Campo | Detalle |
|---|---|
| ID | CP-023 |
| Funcionalidad bajo prueba | Registrar usuario |
| Precondiciones | Repositorio de usuarios simulado. |
| Datos de entrada | Correo "ana@", "@mail.com" y "ana@mail" (sin dominio o sin extensión). |
| Pasos a ejecutar | 1. Construir el usuario con el correo inválido.<br>2. Invocar `UsuarioService.registrar(usuario)`. |
| Resultado esperado | Lanza `IllegalArgumentException` y no llama a `guardar`. |
| Postcondición | No se registra el usuario. |
| Técnica aplicada | Partición de equivalencia (clase inválida: parte local o dominio incompletos) |
| Tipo | Triste |
| Prioridad | Media |
| Requisito | RF-09, RF-07 |
| Escenario de la Unidad 1 | EP-007 |
| Test automatizado | `UsuarioServiceTest#registrar_cuandoCorreoInvalido_lanzaExcepcion` |

### CP-024 – Registrar usuario

| Campo | Detalle |
|---|---|
| ID | CP-024 |
| Funcionalidad bajo prueba | Registrar usuario |
| Precondiciones | Repositorio de usuarios simulado. |
| Datos de entrada | Correo `null` y correo "". |
| Pasos a ejecutar | 1. Construir el usuario con correo nulo o vacío.<br>2. Invocar `UsuarioService.registrar(usuario)`. |
| Resultado esperado | Lanza `IllegalArgumentException` y no llama a `guardar`. |
| Postcondición | No se registra el usuario. |
| Técnica aplicada | Partición de equivalencia (clase inválida: correo nulo o vacío) |
| Tipo | Triste |
| Prioridad | Media |
| Requisito | RF-09, RF-07 |
| Escenario de la Unidad 1 | EP-007 |
| Test automatizado | `UsuarioServiceTest#registrar_cuandoCorreoInvalido_lanzaExcepcion` |

## 5. Trazabilidad

### 5.1 Requisito → casos de prueba

| Requisito | Casos |
|---|---|
| RF-01 | CP-001, CP-002, CP-003, CP-004, CP-005, CP-006 |
| RF-02 | CP-009, CP-010, CP-011, CP-012, CP-013, CP-014, CP-015 |
| RF-03 | CP-016, CP-017, CP-018, CP-019, CP-020 |
| RF-07 | CP-002, CP-003, CP-004, CP-012, CP-013, CP-021, CP-022, CP-023, CP-024 |
| RF-08 | CP-007, CP-008 |
| RF-09 | CP-021, CP-022, CP-023, CP-024 |

### 5.2 Escenario de la Unidad 1 → casos de prueba

| Escenario | Descripción | Casos |
|---|---|---|
| EP-001 | Registrar un material válido | CP-001, CP-005, CP-006 |
| EP-002 | Préstamo exitoso | CP-009 |
| EP-003 | Límite de 3 préstamos activos | CP-011, CP-012, CP-013 |
| EP-004 | Material no disponible | CP-010 |
| EP-005 | Usuario inexistente | CP-014, CP-015 |
| EP-006 | Devolución de material | CP-016, CP-017, CP-018, CP-019, CP-020 |
| EP-007 | Datos inválidos | CP-002, CP-003, CP-004, CP-022, CP-023, CP-024 |

Los casos CP-007, CP-008 y CP-021 no tienen escenario equivalente en la Unidad 1 (marcados con "–"). Los escenarios EP-008 a EP-012 de la Unidad 1 (consulta de usuario por API, gancho innovador, controladores y flujo completo) quedan fuera del alcance de esta unidad, según `plan_de_pruebas.md`.

### 5.3 Caso de prueba → test automatizado

| Test | Casos |
|---|---|
| `MaterialServiceTest#crear_cuandoDatosValidos_guardaYRetornaMaterial` | CP-001 |
| `MaterialServiceTest#crear_cuandoTituloVacioONulo_lanzaExcepcion` | CP-002, CP-003 |
| `MaterialServiceTest#crear_cuandoEjemplaresNegativos_lanzaExcepcion` | CP-004 |
| `MaterialServiceTest#crear_cuandoEjemplaresEnLimiteValido_guardaMaterial` | CP-005, CP-006 |
| `MaterialServiceTest#buscarPorId_cuandoExiste_retornaMaterial` | CP-007 |
| `MaterialServiceTest#buscarPorId_cuandoNoExiste_retornaVacio` | CP-008 |
| `PrestamoServiceTest#registrarPrestamo_cuandoDatosValidos_creaPrestamoYDescuentaEjemplar` | CP-009 |
| `PrestamoServiceTest#registrarPrestamo_cuandoMaterialNoDisponible_lanzaExcepcion` | CP-010 |
| `PrestamoServiceTest#registrarPrestamo_cuandoTieneDosPrestamosActivos_permiteTercero` | CP-011 |
| `PrestamoServiceTest#registrarPrestamo_cuandoSuperaElLimiteDePrestamos_lanzaExcepcion` | CP-012, CP-013 |
| `PrestamoServiceTest#registrarPrestamo_cuandoUsuarioNoExiste_lanzaExcepcion` | CP-014 |
| `PrestamoServiceTest#registrarPrestamo_cuandoMaterialNoExiste_lanzaExcepcion` | CP-015 |
| `PrestamoServiceTest#calcularMulta_cuandoSeDevuelveEnUnaFecha_retornaLaMultaEsperada` | CP-016, CP-017, CP-018, CP-019 |
| `PrestamoServiceTest#calcularMulta_cuandoPrestamoNoExiste_lanzaExcepcion` | CP-020 |
| `UsuarioServiceTest#registrar_cuandoCorreoValido_guardaUsuario` | CP-021 |
| `UsuarioServiceTest#registrar_cuandoCorreoInvalido_lanzaExcepcion` | CP-022, CP-023, CP-024 |
