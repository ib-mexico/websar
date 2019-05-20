package com.ibmexico.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.entities.ProveedorContactoEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.libraries.DataTable;
import com.ibmexico.repositories.IProveedorContactoRepository;

@Service("proveedorContactoService")
public class ProveedorContactoService {
	
	@Autowired
	@Qualifier("proveedorContactoRepository")
	private IProveedorContactoRepository proveedorContactoRepository;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	public List<ProveedorContactoEntity> listProveedorContactos() {
		return proveedorContactoRepository.findAll();
	}
	
	public List<ProveedorContactoEntity> listClienteContactosActivos(int idProveedor) {
		return proveedorContactoRepository.findContactosActivos(idProveedor);
	}
	
	public DataTable<ProveedorContactoEntity> dataTable(int idProveedor, String search, int offset, int limit, String txtBootstrapTableDesde, String txtBootstrapTableHasta) {
		List<ProveedorContactoEntity> lstProveedorContactoEntity = null;
		long totalContactos = 100;
		
		if(search != null) {			
			lstProveedorContactoEntity = proveedorContactoRepository.findForDataTable(idProveedor, search, DataTable.getPageRequest(offset, limit));
			totalContactos = proveedorContactoRepository.countForDataTable(idProveedor, search);
		} else {
			lstProveedorContactoEntity = proveedorContactoRepository.findForDataTable(idProveedor, DataTable.getPageRequest(offset, limit));
			totalContactos = proveedorContactoRepository.countForDataTable(idProveedor);
		}

		DataTable<ProveedorContactoEntity> returnDataTable = new DataTable<ProveedorContactoEntity>(lstProveedorContactoEntity, totalContactos);
		return returnDataTable;
	}
	
	public ProveedorContactoEntity findByIdProveedorContacto(int idProveedorContacto) {
		return proveedorContactoRepository.findByIdProveedorContacto(idProveedorContacto);
	}
	
	public void create(ProveedorContactoEntity objContacto) {
		
		if(objContacto != null) {
			LocalDateTime ldtNow = LocalDateTime.now();
			UsuarioEntity objUsuarioCreacion = sessionService.getCurrentUser();
			objContacto.setCreacionFecha(ldtNow);
			objContacto.setCreacionUsuario(objUsuarioCreacion);
			objContacto.setModificacionFecha(ldtNow);
			objContacto.setModificacionUsuario(objUsuarioCreacion);
			proveedorContactoRepository.save(objContacto);
		}
		else {
			throw new ApplicationException(EnumException.PROVEEDORES_CONTACTO_CREATE_001);
		}
	}
	
public void update(ProveedorContactoEntity objContacto) {
		
		if(objContacto != null) {
			LocalDateTime ldtNow = LocalDateTime.now();
			UsuarioEntity objUsuarioModificacion = sessionService.getCurrentUser();
			objContacto.setModificacionFecha(ldtNow);
			objContacto.setModificacionUsuario(objUsuarioModificacion);
			proveedorContactoRepository.save(objContacto);						
		}
		else {
			throw new ApplicationException(EnumException.PROVEEDORES_CONTACTO_CREATE_001);
		}
	}

}
