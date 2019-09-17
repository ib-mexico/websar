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

import com.ibmexico.entities.CatalogoActivoEntity;
import com.ibmexico.libraries.DataTable;
import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.repositories.ICatalogoActivoRepository;

@Service("catalogoActivoService")
public class CatalogoActivoService  {
	
	@Autowired
	@Qualifier("catalogoActivoRepository")
	private ICatalogoActivoRepository catActivoRep;
	
	public CatalogoActivoEntity findByIdCatalogoActivo(int idCatalogo){
		return catActivoRep.findByIdCatalogoActivo(idCatalogo);
	}
	
	public List<CatalogoActivoEntity> listCatalogo(){
		return catActivoRep.findAll();
	}
	
	public void create(CatalogoActivoEntity catalogoActivo) {
		if (catalogoActivo!=null) {
			LocalDateTime ldtNow = LocalDateTime.now();
			catalogoActivo.setFechaRegistro(ldtNow);
			catalogoActivo.setEliminado(false);
			catalogoActivo.setFechaModificacion(ldtNow);
			catActivoRep.save(catalogoActivo);
		}
		else {
			throw new ApplicationException(EnumException.ACTIVO_CREATE_001);
		}
		
	}
	
	public DataTable<CatalogoActivoEntity> dataTable(String search, int offset, int limit, String txtBootstrapTableDesde, String txtBootstrapTableHasta) {
		List<CatalogoActivoEntity> lstCatalogoActivoEntity = null;
		long totalSucursales = 100;
		
		lstCatalogoActivoEntity = catActivoRep.findForDataTable(DataTable.getPageRequest(offset, limit));
		totalSucursales = catActivoRep.countForDataTable();

		DataTable<CatalogoActivoEntity> returnDataTable = new DataTable<CatalogoActivoEntity>(lstCatalogoActivoEntity, totalSucursales);
		return returnDataTable;
	}
	
	public void delete(int id) {
		catActivoRep.delete(id);
	}

	public void update(CatalogoActivoEntity objActivo){
		if (objActivo!=null) {
			LocalDateTime ldtNow=LocalDateTime.now();
			objActivo.setFechaModificacion(ldtNow);
			catActivoRep.save(objActivo);
		} else {
			throw new ApplicationException(EnumException.ACTIVO_DELETE_001);
		}

	}

	public JsonObject jsonCatalogoActivo() {
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();

		List<CatalogoActivoEntity> lstCatalogoAct = catActivoRep.listCatalogoActivo();

		lstCatalogoAct.forEach((item)-> {
			jsonRows.add(Json.createObjectBuilder()
				.add("id_catalogo_activo", item.getIdCatalogoActivo())
				.add("nombre", item.getNombre())
			);
		});

		jsonReturn.add("rows", jsonRows);
		
		return jsonReturn.build();
	}

}
