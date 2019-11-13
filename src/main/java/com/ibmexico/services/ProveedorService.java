package com.ibmexico.services;

import java.time.LocalDateTime;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.entities.ProveedorEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.libraries.DataTable;
import com.ibmexico.repositories.IProveedorRepository;


@Service("proveedorService")
public class ProveedorService {
	
	@Autowired
	@Qualifier("proveedorRepository")
	private IProveedorRepository proveedorRepository;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	public List<ProveedorEntity> listProveedores() {
		return proveedorRepository.findAll();
	}
	
	public List<ProveedorEntity> listProveedoreesActivos() {
		return proveedorRepository.findActivos();
	}
	
	public DataTable<ProveedorEntity> dataTable(String search, int offset, int limit, String txtBootstrapTableDesde, String txtBootstrapTableHasta) {
		List<ProveedorEntity> lstProveedorEntity = null;
		long totalProveedores = 100;
		
		if(search != null) {			
			lstProveedorEntity = proveedorRepository.findForDataTable(search, DataTable.getPageRequest(offset, limit));
			totalProveedores = proveedorRepository.countForDataTable(search);
		} else {
			lstProveedorEntity = proveedorRepository.findForDataTable(DataTable.getPageRequest(offset, limit));
			totalProveedores = proveedorRepository.countForDataTable();
		}				

		DataTable<ProveedorEntity> returnDataTable = new DataTable<ProveedorEntity>(lstProveedorEntity, totalProveedores);
		return returnDataTable;
	}
	
	public ProveedorEntity findByIdProveedor(int idProveedor) {
		return proveedorRepository.findByIdProveedor(idProveedor);
	}
	
	public void create(ProveedorEntity objProveedor) {
		
		if(objProveedor != null) {
			LocalDateTime ldtNow = LocalDateTime.now();
			UsuarioEntity objUsuarioCreacion = sessionService.getCurrentUser();
			objProveedor.setCreacionFecha(ldtNow);
			objProveedor.setCreacionUsuario(objUsuarioCreacion);
			objProveedor.setModificacionFecha(ldtNow);
			objProveedor.setModificacionUsuario(objUsuarioCreacion);
			proveedorRepository.save(objProveedor);
		}
		else {
			throw new ApplicationException(EnumException.PROVEEDORES_CREATE_001);
		}
	}
	
	public void update(ProveedorEntity objProveedor) {
		
		if(objProveedor != null) {
			LocalDateTime ldtNow = LocalDateTime.now();
			UsuarioEntity objUsuarioModificacion = sessionService.getCurrentUser();
			objProveedor.setModificacionFecha(ldtNow);
			objProveedor.setModificacionUsuario(objUsuarioModificacion);
			proveedorRepository.save(objProveedor);
		}
		else {
			throw new ApplicationException(EnumException.PROVEEDORES_UPDATE_001);
		}
	}

	public JsonObject jsonProveedores(){
		JsonObjectBuilder jsonReturn=Json.createObjectBuilder();
		JsonArrayBuilder jsonRows=Json.createArrayBuilder();
		List<ProveedorEntity> lstProveedores= proveedorRepository.findActivos();

		lstProveedores.forEach((item)->{
			jsonRows.add(Json.createObjectBuilder()
				.add("id_proveedor", item.getIdProveedor())
				.add("razon_social", item.getRazonSocial())
				.add("proveedor", item.getProveedor())
			);
		});
		jsonReturn.add("proveedores", jsonRows);
		return jsonReturn.build();
	}
}
