package com.javacomunica.service;

import java.util.List;

import com.javacomunica.model.Orden;

public interface IOrdenService {
	Orden save(Orden orden);
	
	List<Orden> findAll();
	
	String generarNumeroOrden();
}
