package com.javacomunica.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.swing.text.html.parser.DTD;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.javacomunica.model.DetalleOrden;
import com.javacomunica.model.Orden;
import com.javacomunica.model.Producto;
import com.javacomunica.model.Usuario;
import com.javacomunica.service.IDetalleOrdenService;
import com.javacomunica.service.IOrdenService;
import com.javacomunica.service.ProductoService;
import com.javacomunica.service.UsuarioService;

@Controller
@RequestMapping("/")
public class HomeController {

	private final Logger log = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private IOrdenService ordenService;
	
	@Autowired
	private IDetalleOrdenService detalleOrdenService;

	// esto sirve para almacenar 1 o más detalles de la ORDEN
	List<DetalleOrden> detalles = new ArrayList<DetalleOrden>();

	// almacena los datos de la ORDEN
	Orden orden = new Orden();

	@GetMapping("")
	public String home(Model model) {

		model.addAttribute("productos", productoService.findAll());

		return "usuario/home";
	}

	@GetMapping("productohome/{id}")
	public String productoHome(@PathVariable Integer id, Model model) {

		log.info("el id de producto enviado como parametro {}", id);

		Producto producto = new Producto();

		Optional<Producto> produOptional = productoService.get(id);

		producto = produOptional.get();

		model.addAttribute("producto", producto);

		return "usuario/productohome";
	}

	@PostMapping("/cart")
	public String addCart(@RequestParam Integer id, @RequestParam Integer cantidad, Model model) {

		DetalleOrden detalleOrden = new DetalleOrden();
		Producto producto = new Producto();
		double sumaTotal = 0;

		Optional<Producto> produOptional = productoService.get(id);

		log.info("producto añadido: {}", produOptional.get());
		log.info("cantidad añadida: {}", cantidad);

		producto = produOptional.get();

		detalleOrden.setCantidad(cantidad);
		detalleOrden.setPrecio(producto.getPrecio());
		detalleOrden.setNombre(producto.getNombre());
		detalleOrden.setTotal(producto.getPrecio() * cantidad);
		detalleOrden.setProducto(producto);

		// validar que producto no se agregue repetido
		Integer idProducto = producto.getId();
		boolean ingresado = detalles.stream().anyMatch(p -> p.getProducto().getId() == idProducto);

		if (!ingresado) {
			detalles.add(detalleOrden);
		}

		sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();

		orden.setTotal(sumaTotal);

		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);

		return "usuario/carrito";
	}

	// remover un producto del carrito
	@GetMapping("/delete/cart/{id}")
	public String deleteProductCart(@PathVariable Integer id, Model model) {

		// lista nueva de productos
		List<DetalleOrden> ordenesNuevas = new ArrayList<DetalleOrden>();

		for (DetalleOrden detalleOrden : detalles) {
			if (detalleOrden.getProducto().getId() != id) {
				ordenesNuevas.add(detalleOrden);
			}
		}

		// poner la nueva lista con los productos que no fueron eliminados
		detalles = ordenesNuevas;

		double sumaTotal = 0;

		sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();

		orden.setTotal(sumaTotal);

		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);

		return "usuario/carrito";
	}

	@GetMapping("/getCart")
	public String getCart(Model model) {
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		return "/usuario/carrito";
	}
	
	@GetMapping("/order")
	public String order(Model model) {
		
		Usuario u=usuarioService.findById(1).get(); 
		
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		model.addAttribute("usuario", u);
		
		return "/usuario/resumenorden";
	}

	@GetMapping("/saveOrder")
	public String saveOrder() {
		Date fechaCreacion=new Date();
		
		//datos de la orden
		orden.setFechaCreacion(fechaCreacion);
		orden.setNumero(ordenService.generarNumeroOrden());
		
		//usuario o cliente
		Usuario u=usuarioService.findById(1).get();
		orden.setUsuario(u);
		ordenService.save(orden);
		
		//guardar los detalles
		for (DetalleOrden detalleOrden : detalles) {
			detalleOrden.setOrden(orden);
			detalleOrdenService.save(detalleOrden);
		}
		
		//limpiar o resetear los valores
		orden = new Orden();
		detalles.clear();
		
		return "redirect:/";
	}
	
	@PostMapping("/search")
	public String searchProduct(@RequestParam String nombre,Model model) {
		
		log.info("el nombre es {}",nombre);
		
		List<Producto> productos=productoService.findAll().stream().filter(p -> p.getNombre().contains(nombre)).collect(Collectors.toList());
		model.addAttribute("productos",productos);
		return "usuario/home";
	}
	
}
