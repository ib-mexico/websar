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
	
	/**listar puestos sin considerar desactivados. */
	public List<PuestoEntity> listPuestosAll() {
		return puestoRepository.findAll();
	}

	/**Considera lista de puestos unicamente habilitados */
	public List<PuestoEntity> listPuesto() {
		return puestoRepository.puestosAll();
    }
	
	/**Busqueda por ID */
    public PuestoEntity findIdPuesto(int idPuesto){
        return puestoRepository.findByIdPuesto(idPuesto);
    }

}