package com.ibmexico.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibmexico.entities.ClienteGrupoEmpresarialEntity;
import com.ibmexico.repositories.IClienteGrupoEmpresarialRepository;

@Service("clienteGrupoEmpresarialService")
public class ClienteGrupoEmpresarialService {

	@Autowired
	@Qualifier("clienteGrupoEmpresarialRepository")
	private IClienteGrupoEmpresarialRepository clienteGrupoEmpresarialRepository;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	public ClienteGrupoEmpresarialEntity findByIdGrupoEmpresarial(int idGrupoEmpresarial) {
		return clienteGrupoEmpresarialRepository.findByIdGrupoEmpresarial(idGrupoEmpresarial);
	}
	
	public List<ClienteGrupoEmpresarialEntity> listGruposEmpresariales() {
		return clienteGrupoEmpresarialRepository.findAll();
	}
}
