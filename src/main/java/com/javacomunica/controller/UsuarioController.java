package com.javacomunica.controller;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.javacomunica.model.Usuario;
import com.javacomunica.service.UsuarioService;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	private final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping("/registro")
	public String registro() {
		return "usuario/registro";
	}

	@PostMapping("/save")
	public String saveUser(Usuario usuario) {
		logger.info("El usuario es {}", usuario);
		usuario.setTipo("USER");
		usuarioService.save(usuario);

		return "redirect:/";
	}

	@GetMapping("/login")
	public String login() {
		return "usuario/login";
	}
	
	@PostMapping("/acceder")
	public String acceder(Usuario usuario, HttpSession session) {
		logger.info("Accesos: {}",usuario);
		
		Optional<Usuario> usuariOptional=usuarioService.findByEmail(usuario.getEmail());
		
		//logger.info("Usuario de DB: {}",usuariOptional.get());
		
		if (usuariOptional.isPresent()) {
			session.setAttribute("idusuario", usuariOptional.get().getId());
			if (usuariOptional.get().getTipo().equals("ADMIN")) {
				return "redirect:/administrador";
			}else {
				return "redirect:/";
			}
		}else {
			logger.info("usuario no existe");
		}
		
		return "redirect:/";
	}
}
