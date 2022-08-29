package com.javacomunica.controller;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.javacomunica.model.Producto;
import com.javacomunica.model.Usuario;
import com.javacomunica.service.ProductoService;
import com.javacomunica.service.UploadFileService;

@Controller
@RequestMapping("/productos")
public class ProductoController {

	private final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class);
	
	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private UploadFileService uploadFileService;
	
	
	@GetMapping("")
	public String show(Model model) {
		model.addAttribute("productos", productoService.findAll());
		return "productos/show";
	}
	
	@GetMapping("/create")
	public String create() {
		return "productos/create";
	}
	
	@PostMapping("/save")
	public String save(Producto producto,@RequestParam("img") MultipartFile file) throws IOException {
		LOGGER.info("Este es el objeto {}",producto);
		Usuario usuario = new Usuario(1, "", "", "", "", "", "", "");
		producto.setUsuario(usuario);
		productoService.save(producto);
		
		//image
		if (producto.getId()==null) {
			String nameImage=uploadFileService.saveImage(file);
			producto.setImagen(nameImage);
		}else {
			if (file.isEmpty()) {
				Producto p=new Producto();
				p=productoService.get(producto.getId()).get();
				
				producto.setImagen(p.getImagen());
				
			}else {
				String nameImage=uploadFileService.saveImage(file);
				producto.setImagen(nameImage);
			}
		}
		
		return "redirect:/productos";
	}
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Integer id, Model model) {
		Producto producto = new Producto();
		Optional<Producto> optionalProduct=productoService.get(id);
		producto = optionalProduct.get();
		
		LOGGER.info("Producto buscado: {}",producto);
		
		model.addAttribute("producto",producto);
		
		return "productos/edit";
	}
	
	@PostMapping("/update")
	public String update(Producto producto) {
		productoService.update(producto);
		return "redirect:/productos";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Integer id) {
		productoService.delete(id);
		return "redirect:/productos";
	}
}
