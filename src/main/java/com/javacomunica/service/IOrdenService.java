package com.javacomunica.service;

import java.util.List;
import java.util.Optional;

import com.javacomunica.model.Orden;
import com.javacomunica.model.Usuario;

public interface IOrdenService {
	Orden save(Orden orden);
	
	Optional<Orden> findById(Integer id);
	
	List<Orden> findAll();
	
	String generarNumeroOrden();
	
	List<Orden> findByUsuario(Usuario usuario);
}
