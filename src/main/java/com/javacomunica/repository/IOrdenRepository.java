package com.javacomunica.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.javacomunica.model.Orden;
import com.javacomunica.model.Usuario;

@Repository
public interface IOrdenRepository extends JpaRepository<Orden, Integer>{

	List<Orden> findByUsuario(Usuario usuario );
}
