package com.javacomunica.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.javacomunica.model.Orden;
import com.javacomunica.model.Usuario;
import com.javacomunica.service.IOrdenService;
import com.javacomunica.service.UsuarioService;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	private final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private IOrdenService ordenService;  

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
	
	@GetMapping("/compras")
	public String obtenerCompras(Model model,HttpSession session) {
		
		model.addAttribute("sesion", session.getAttribute("idusuario"));
		
		Usuario usuario=usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
		
		List<Orden> ordenes = ordenService.findByUsuario(usuario);
		
		model.addAttribute("ordenes", ordenes);
		
		return "usuario/compras";
	}
	
	@GetMapping("/detalle/{id}")
	public String detalleCompra(@PathVariable Integer id, HttpSession session,Model model) {
		
		logger.info("Id de la orden es: {} " + id);
		
		Optional<Orden> orden = ordenService.findById(id);
		
		model.addAttribute("sesion", session.getAttribute("idusuario"));
		model.addAttribute("detalles", orden.get().getDetalle());
		
		return "usuario/detallecompra";
	}
	
	
}
