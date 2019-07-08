package com.ibmexico.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.entities.EquipoProduccionEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.libraries.DataTable;
import com.ibmexico.repositories.IEquipoProduccionRepository;

@Service("equipoProduccionService")
public class EquipoProduccionService {

	@Autowired
	@Qualifier("equipoProduccionRepository")
	private IEquipoProduccionRepository equipoProduccionRepository;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	public List<EquipoProduccionEntity> listEquipos() {
		return equipoProduccionRepository.findAll();
	}
	
	public List<EquipoProduccionEntity> listEquiposProduccionVencimiento(LocalDate ldFecha) {
		return equipoProduccionRepository.listEquiposProduccionVencimiento(ldFecha);
	}
	
	public DataTable<EquipoProduccionEntity> dataTable(String search, int offset, int limit, String txtBootstrapTableDesde, String txtBootstrapTableHasta) {
		List<EquipoProduccionEntity> lstEquipoEntity = null;
		long totalEquipos = 100;
		
		if(search != null) {			
			lstEquipoEntity = equipoProduccionRepository.findForDataTable(search, DataTable.getPageRequest(offset, limit));
			totalEquipos = equipoProduccionRepository.countForDataTable(search);
		} else {
			lstEquipoEntity = equipoProduccionRepository.findForDataTable(DataTable.getPageRequest(offset, limit));
			totalEquipos = equipoProduccionRepository.countForDataTable();
		}				

		DataTable<EquipoProduccionEntity> returnDataTable = new DataTable<EquipoProduccionEntity>(lstEquipoEntity, totalEquipos);
		return returnDataTable;
	}
	
	public EquipoProduccionEntity findByIdEquipoProduccion(int idEquipoProduccion) {
		return equipoProduccionRepository.findByIdEquipoProduccion(idEquipoProduccion);
	}
	
	public void create(EquipoProduccionEntity objEquipoProduccion) {
		
		if(objEquipoProduccion != null) {			
			LocalDateTime ldtNow = LocalDateTime.now();
			UsuarioEntity objUsuarioCreacion = sessionService.getCurrentUser();
			objEquipoProduccion.setCreacionFecha(ldtNow);
			objEquipoProduccion.setCreacionUsuario(objUsuarioCreacion);
			objEquipoProduccion.setModificacionFecha(ldtNow);
			objEquipoProduccion.setModificacionUsuario(objUsuarioCreacion);
							
			equipoProduccionRepository.save(objEquipoProduccion);						
		}
		else {
			throw new ApplicationException(EnumException.EQUIPOS_PRODUCCION_CREATE_001);
		}
	}
	
	public void update(EquipoProduccionEntity objEquipoProduccion) {
		
		if(objEquipoProduccion != null) {
			LocalDateTime ldtNow = LocalDateTime.now();
			UsuarioEntity objUsuarioModificacion = sessionService.getCurrentUser();
			objEquipoProduccion.setModificacionFecha(ldtNow);
			objEquipoProduccion.setModificacionUsuario(objUsuarioModificacion);
			
			equipoProduccionRepository.save(objEquipoProduccion);
		}
		else {
			throw new ApplicationException(EnumException.EQUIPOS_PRODUCCION_UPDATE_001);
		}
	}
}
