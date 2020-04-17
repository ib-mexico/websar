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

import com.ibmexico.repositories.ISucursalRepository;
import com.ibmexico.services.SessionService;
import com.ibmexico.entities.SucursalEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.libraries.DataTable;
import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;

@Service("sucursalService")
public class SucursalService {

	@Autowired
	@Qualifier("sucursalRepository")
	private ISucursalRepository sucursalRepository;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	public List<SucursalEntity> listSucursales() {
		return sucursalRepository.findAll();
	}
	
	public DataTable<SucursalEntity> dataTable(String search, int offset, int limit, String txtBootstrapTableDesde, String txtBootstrapTableHasta) {
		List<SucursalEntity> lstSucursalEntity = null;
		long totalSucursales = 100;
		
		lstSucursalEntity = sucursalRepository.findForDataTable(DataTable.getPageRequest(offset, limit));
		totalSucursales = sucursalRepository.countForDataTable();

		DataTable<SucursalEntity> returnDataTable = new DataTable<SucursalEntity>(lstSucursalEntity, totalSucursales);
		return returnDataTable;
	}
	
	public SucursalEntity findByIdSucursal(int idSucursal) {
		return sucursalRepository.findByIdSucursal(idSucursal);
	}

	//Listado de sucursal 
	public JsonObject jsonSucursal(){
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();

		List<SucursalEntity> lstSucursal =  sucursalRepository.findAll();

		lstSucursal.forEach((item)-> {
			jsonRows.add(Json.createObjectBuilder()
				.add("id_sucursal", item.getIdSucursal())
				.add("nombre_sucursal", item.getSucursal())
			);
		});

		jsonReturn.add("rows", jsonRows);
		
		return jsonReturn.build();
	}

	public void save(SucursalEntity objSucursal) {
		if (objSucursal != null){
			LocalDateTime ldtNow = LocalDateTime.now();
			UsuarioEntity objUsuarioCreacion = sessionService.getCurrentUser();
			objSucursal.setCreacionFecha(ldtNow);
			objSucursal.setModificacionFecha(ldtNow);
			objSucursal.setCreacionUsuario(objUsuarioCreacion);
			objSucursal.setModificacionUsuario(objUsuarioCreacion);
			sucursalRepository.save(objSucursal);
		}
	}

	public void delete(int id) {
		try {
			sucursalRepository.delete(id);
		} catch (Exception e) {
			throw new ApplicationException(EnumException.SUCURSAL_DELETE_001);
		}
	}

	public void update(SucursalEntity objSucursal){
		if (objSucursal != null) {
			LocalDateTime ldtNow=LocalDateTime.now();
			objSucursal.setModificacionFecha(ldtNow);
			sucursalRepository.save(objSucursal);
		} else {
			throw new ApplicationException(EnumException.SUCURSAL_UPDATE_001);
		}

	}

	public JsonObject jsonSucursalById(int idSucursal) {
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();
		SucursalEntity objSucursal = sucursalRepository.findByIdSucursal(idSucursal);
		
		jsonRows.add(Json.createObjectBuilder()
					.add("idSucursal", objSucursal.getIdSucursal())
					.add("idEmpresa", objSucursal.getEmpresa() == null ? 1 : objSucursal.getEmpresa().getIdEmpresa())
					.add("nombreSucursal", objSucursal.getSucursal().isEmpty() ? "" : objSucursal.getSucursal())
					.add("codigoPostal", objSucursal.getCodigoPostal().isEmpty() ? "" : objSucursal.getCodigoPostal())
					.add("telefono", objSucursal.getTelefono().isEmpty() ? "" : objSucursal.getTelefono())
					.add("domicilioSucursal", objSucursal.getDomicilio().isEmpty() ? "" : objSucursal.getDomicilio())
					.add("coloniaSucursal", objSucursal.getColonia().isEmpty() ? "" : objSucursal.getColonia())
					.add("municipio", objSucursal.getMunicipio().isEmpty() ? "" : objSucursal.getMunicipio())
					.add("ciudad", objSucursal.getCiudad().isEmpty() ? "" : objSucursal.getCiudad())
					.add("estado", objSucursal.getEstado().isEmpty() ? "" : objSucursal.getEstado())
					);
		jsonReturn.add("rows", jsonRows);
		return jsonReturn.build();
	}
}
