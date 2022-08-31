package com.javacomunica.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javacomunica.model.DetalleOrden;
import com.javacomunica.repository.IDetalleOrdenRepository;

@Service
public class DetalleOrdenServiceImpl implements IDetalleOrdenService{

	@Autowired
	private IDetalleOrdenRepository detalleOrdenRepository;
	
	@Override
	public DetalleOrden save(DetalleOrden detalle) {
		// TODO Auto-generated method stub
		return detalleOrdenRepository.save(detalle);
	}

}
