package com.bibliouni.servicio;

import com.bibliouni.excepcion.MaterialNoDisponibleException;
import com.bibliouni.excepcion.RecursoNoEncontradoException;
import com.bibliouni.modelo.Material;
import com.bibliouni.modelo.Prestamo;
import com.bibliouni.modelo.Usuario;
import com.bibliouni.repositorio.MaterialRepository;
import com.bibliouni.repositorio.PrestamoRepository;
import com.bibliouni.repositorio.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PrestamoServiceTest {

    private static final LocalDate FECHA_PRESTAMO = LocalDate.of(2026, 11, 3);

    @Mock
    private PrestamoRepository prestamoRepository;

    @Mock
    private MaterialRepository materialRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private PrestamoService prestamoService;

    private Usuario usuario;

    @BeforeEach
    void prepararDatosBase() {
        usuario = new Usuario(1L, "Ana Pérez", "ana.perez@uniempresarial.edu.co");
    }

    @Test
    void registrarPrestamo_cuandoDatosValidos_creaPrestamoYDescuentaEjemplar() {
        // Arrange
        Material material = new Material(10L, "Cálculo diferencial", "Marta Ruiz", "978-0000000003", 2);
        when(usuarioRepository.buscarPorId(1L)).thenReturn(Optional.of(usuario));
        when(materialRepository.buscarPorId(10L)).thenReturn(Optional.of(material));
        when(prestamoRepository.guardar(any(Prestamo.class))).thenAnswer(invocacion -> invocacion.getArgument(0));

        // Act
        Prestamo resultado = prestamoService.registrarPrestamo(1L, 10L, FECHA_PRESTAMO);

        // Assert
        ArgumentCaptor<Prestamo> captor = ArgumentCaptor.forClass(Prestamo.class);
        verify(prestamoRepository).guardar(captor.capture());
        assertEquals(resultado, captor.getValue());
        assertEquals(LocalDate.of(2026, 11, 10), resultado.getFechaVencimiento());
        assertEquals(1, material.getEjemplaresDisponibles());
        verify(materialRepository).guardar(material);
    }

    @Test
    void registrarPrestamo_cuandoMaterialNoDisponible_lanzaExcepcion() {
        // Arrange
        Material agotado = new Material(10L, "Cálculo diferencial", "Marta Ruiz", "978-0000000003", 0);
        when(usuarioRepository.buscarPorId(1L)).thenReturn(Optional.of(usuario));
        when(materialRepository.buscarPorId(10L)).thenReturn(Optional.of(agotado));

        // Act + Assert
        assertThrows(MaterialNoDisponibleException.class,
                () -> prestamoService.registrarPrestamo(1L, 10L, FECHA_PRESTAMO));
        verify(prestamoRepository, never()).guardar(any());
        verify(materialRepository, never()).guardar(any());
    }

    @Test
    void registrarPrestamo_cuandoUsuarioNoExiste_lanzaExcepcion() {
        // Arrange
        when(usuarioRepository.buscarPorId(999L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(RecursoNoEncontradoException.class,
                () -> prestamoService.registrarPrestamo(999L, 10L, FECHA_PRESTAMO));
        verify(materialRepository, never()).buscarPorId(any());
        verify(prestamoRepository, never()).guardar(any());
    }

    @Test
    void registrarPrestamo_cuandoMaterialNoExiste_lanzaExcepcion() {
        // Arrange
        when(usuarioRepository.buscarPorId(1L)).thenReturn(Optional.of(usuario));
        when(materialRepository.buscarPorId(999L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(RecursoNoEncontradoException.class,
                () -> prestamoService.registrarPrestamo(1L, 999L, FECHA_PRESTAMO));
        verify(prestamoRepository, never()).guardar(any());
    }

    @ParameterizedTest
    @CsvSource({
            "2026-11-09, 0",
            "2026-11-10, 0",
            "2026-11-11, 1000",
            "2026-11-15, 5000"
    })
    void calcularMulta_cuandoSeDevuelveEnUnaFecha_retornaLaMultaEsperada(LocalDate fechaDevolucion, long multaEsperada) {
        // Arrange
        Prestamo prestamo = new Prestamo(1L, 1L, 10L, FECHA_PRESTAMO, LocalDate.of(2026, 11, 10));
        when(prestamoRepository.buscarPorId(1L)).thenReturn(Optional.of(prestamo));

        // Act
        long multa = prestamoService.calcularMulta(1L, fechaDevolucion);

        // Assert
        assertEquals(multaEsperada, multa);
    }

    @Test
    void calcularMulta_cuandoPrestamoNoExiste_lanzaExcepcion() {
        // Arrange
        when(prestamoRepository.buscarPorId(999L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(RecursoNoEncontradoException.class,
                () -> prestamoService.calcularMulta(999L, LocalDate.of(2026, 11, 15)));
    }
}
