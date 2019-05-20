package com.ibmexico.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibmexico.entities.ClienteGiroEntity;
import com.ibmexico.repositories.IClienteGiroRepository;

@Service("clienteGiroService")
public class ClienteGiroService {

	@Autowired
	@Qualifier("clienteGiroRepository")
	private IClienteGiroRepository clienteGiroRepository;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	public ClienteGiroEntity findByIdClienteGiro(int idClienteGiro) {
		return clienteGiroRepository.findByIdClienteGiro(idClienteGiro);
	}
	
	public List<ClienteGiroEntity> listClientesGiros() {
		return clienteGiroRepository.findAll();
	}
}
