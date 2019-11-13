package com.ibmexico.services;

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
import com.ibmexico.libraries.DataTable;

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
}
