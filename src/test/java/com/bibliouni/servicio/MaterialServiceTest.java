package com.bibliouni.servicio;

import com.bibliouni.modelo.Material;
import com.bibliouni.repositorio.MaterialRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MaterialServiceTest {

    @Mock
    private MaterialRepository materialRepository;

    @InjectMocks
    private MaterialService materialService;

    @Test
    void crear_cuandoDatosValidos_guardaYRetornaMaterial() {
        // Arrange
        Material material = new Material(null, "Introducción a las pruebas de software",
                "Ana Pérez", "978-0000000001", 3);
        when(materialRepository.guardar(material)).thenReturn(material);

        // Act
        Material resultado = materialService.crear(material);

        // Assert
        assertSame(material, resultado);
        assertEquals(3, resultado.getEjemplaresDisponibles());
        verify(materialRepository).guardar(material);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    void crear_cuandoTituloVacioONulo_lanzaExcepcion(String titulo) {
        // Arrange
        Material material = new Material(null, titulo, "Ana Pérez", "978-0000000001", 2);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> materialService.crear(material));
        verify(materialRepository, never()).guardar(any());
    }

    @Test
    void crear_cuandoEjemplaresNegativos_lanzaExcepcion() {
        // Arrange
        Material material = new Material(null, "Álgebra lineal", "Luis Gómez", "978-0000000002", -1);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> materialService.crear(material));
        verify(materialRepository, never()).guardar(any());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void crear_cuandoEjemplaresEnLimiteValido_guardaMaterial(int ejemplares) {
        // Arrange
        Material material = new Material(null, "Álgebra lineal", "Luis Gómez", "978-0000000002", ejemplares);
        when(materialRepository.guardar(material)).thenReturn(material);

        // Act
        Material resultado = materialService.crear(material);

        // Assert
        assertEquals(ejemplares, resultado.getEjemplaresDisponibles());
        assertEquals(ejemplares > 0, resultado.estaDisponible());
        verify(materialRepository).guardar(material);
    }

    @Test
    void buscarPorId_cuandoExiste_retornaMaterial() {
        // Arrange
        Material material = new Material(10L, "Cálculo diferencial", "Marta Ruiz", "978-0000000003", 2);
        when(materialRepository.buscarPorId(10L)).thenReturn(Optional.of(material));

        // Act
        Optional<Material> resultado = materialService.buscarPorId(10L);

        // Assert
        assertTrue(resultado.isPresent());
        assertSame(material, resultado.get());
    }

    @Test
    void buscarPorId_cuandoNoExiste_retornaVacio() {
        // Arrange
        when(materialRepository.buscarPorId(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Material> resultado = materialService.buscarPorId(999L);

        // Assert
        assertFalse(resultado.isPresent());
    }
}
