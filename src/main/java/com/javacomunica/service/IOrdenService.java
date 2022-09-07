package com.javacomunica.service;

import java.util.List;

import com.javacomunica.model.Orden;
import com.javacomunica.model.Usuario;

public interface IOrdenService {
	Orden save(Orden orden);
	
	List<Orden> findAll();
	
	String generarNumeroOrden();
	
	List<Orden> findByUsuario(Usuario usuario);
}
