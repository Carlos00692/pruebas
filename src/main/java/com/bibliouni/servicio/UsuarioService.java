package com.bibliouni.servicio;

import com.bibliouni.modelo.Usuario;
import com.bibliouni.repositorio.UsuarioRepository;

public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario registrar(Usuario usuario) {
        return usuarioRepository.guardar(usuario);
    }
}
