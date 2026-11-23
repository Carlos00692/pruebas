package com.bibliouni.servicio;

import com.bibliouni.modelo.Usuario;
import com.bibliouni.repositorio.UsuarioRepository;

import java.util.regex.Pattern;

public class UsuarioService {

    private static final Pattern PATRON_CORREO = Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario registrar(Usuario usuario) {
        if (!esCorreoValido(usuario.getCorreo())) {
            throw new IllegalArgumentException("El correo no tiene un formato válido");
        }
        return usuarioRepository.guardar(usuario);
    }

    private boolean esCorreoValido(String correo) {
        return correo != null && PATRON_CORREO.matcher(correo).matches();
    }
}
