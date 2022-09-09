package com.javacomunica.service;

import java.util.List;
import java.util.Optional;

import com.javacomunica.model.Usuario;

public interface UsuarioService {

	Optional<Usuario> findById(Integer id);
	Usuario save(Usuario usuario);
	Optional<Usuario> findByEmail(String email);
	List<Usuario> findAll();
}
