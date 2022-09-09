package com.javacomunica.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.javacomunica.model.Producto;
import com.javacomunica.model.Usuario;
import com.javacomunica.service.ProductoService;
import com.javacomunica.service.UsuarioService;

@Controller
@RequestMapping("/administrador")
public class AdministradorController {
	
	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@GetMapping("")
	public String home(Model model) {
		List<Producto> productos=productoService.findAll();
		model.addAttribute("productos", productos);
		return "administrador/home";
	}
	
	@GetMapping("/usuarios")
	public String usuarios(Model model) {
		
		model.addAttribute("usuarios", usuarioService.findAll());
		
		return "administrador/usuarios";
	}
	
}
