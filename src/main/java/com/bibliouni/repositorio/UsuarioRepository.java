package com.bibliouni.repositorio;

import com.bibliouni.modelo.Usuario;

import java.util.Optional;

public interface UsuarioRepository {

    Usuario guardar(Usuario usuario);

    Optional<Usuario> buscarPorId(Long id);
}
