package com.ibmexico.services;

import java.util.List;

import com.ibmexico.entities.PuestoEntity;
import com.ibmexico.repositories.IPuestoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("puestoService")
public class PuestoService {

    @Autowired
	@Qualifier("puesto_repository")
	private IPuestoRepository puestoRepository;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	public List<PuestoEntity> listPuestosAll() {
		return puestoRepository.findAll();
	}
	
	public List<PuestoEntity> listPuesto() {
		return puestoRepository.puestosAll();
    }
    
    public PuestoEntity findIdPuesto(int idPuesto){
        return puestoRepository.findByIdPuesto(idPuesto);
    }


}