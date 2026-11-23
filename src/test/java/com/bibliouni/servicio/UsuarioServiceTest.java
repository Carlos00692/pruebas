package com.bibliouni.servicio;

import com.bibliouni.modelo.Usuario;
import com.bibliouni.repositorio.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void registrar_cuandoCorreoValido_guardaUsuario() {
        // Arrange
        Usuario usuario = new Usuario(null, "Ana Pérez", "ana.perez@uniempresarial.edu.co");
        when(usuarioRepository.guardar(usuario)).thenReturn(usuario);

        // Act
        Usuario resultado = usuarioService.registrar(usuario);

        // Assert
        assertSame(usuario, resultado);
        verify(usuarioRepository).guardar(usuario);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {
            "ana.perez.uniempresarial.edu.co",
            "ana perez@mail.com",
            "ana@",
            "@mail.com",
            "ana@mail"
    })
    void registrar_cuandoCorreoInvalido_lanzaExcepcion(String correo) {
        // Arrange
        Usuario usuario = new Usuario(null, "Ana Pérez", correo);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> usuarioService.registrar(usuario));
        verify(usuarioRepository, never()).guardar(any());
    }
}
