package com.ibmexico.services;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
	
	@Transactional
	public void addFile(CatalogoActivoEntity objCatalogoFichero, MultipartFile file){
		if (objCatalogoFichero!=null) {
			if (file!=null && !file.isEmpty() ) {
				try {
					if(!file.getOriginalFilename().trim().equals("")){
						String ficheroNombre = UUID.randomUUID().toString();
						String[] arrNombreFichero = file.getOriginalFilename().split("\\.");
						if(arrNombreFichero.length>=2){
							ficheroNombre =ficheroNombre +"."+arrNombreFichero[arrNombreFichero.length-1];
						}
						System.out.println("file no esta vacio");
						objCatalogoFichero.setUrl(ficheroNombre);
						create(objCatalogoFichero, file);
					}
				} catch (Exception e) {
					throw new ApplicationException(EnumException.ACTIVO_FICHEROS_ADD_FILE_003);
				}
			} else {
				LocalDateTime ldtFecha=LocalDateTime.now();
				objCatalogoFichero.setFechaRegistro(ldtFecha);
				objCatalogoFichero.setEliminado(false);
				objCatalogoFichero.setFechaModificacion(ldtFecha);
				catActivoRep.save(objCatalogoFichero);
			}
		} else {
			throw new ApplicationException(EnumException.ACTIVO_FICHEROS_ADD_FILE_001);
		}

	}



	//method of create objcatalogo and file
	@Transactional
	public void create(CatalogoActivoEntity objCatalogoActivo, MultipartFile objFile)throws IOException {
		URL urlPath = this.getClass().getResource("/");

		if (objCatalogoActivo!=null) {
			if(objCatalogoActivo.getUrl() !=""){
				byte[] bytesFichero=objFile.getBytes();
				try (BufferedOutputStream buffStream=new BufferedOutputStream(new FileOutputStream(new File(urlPath.getPath()+"static/ficheros/catalogo/"+objCatalogoActivo.getUrl())))) {
					buffStream.write(bytesFichero);
					LocalDateTime ldtNow = LocalDateTime.now();
					objCatalogoActivo.setFechaRegistro(ldtNow);
					objCatalogoActivo.setEliminado(false);
					objCatalogoActivo.setFechaModificacion(ldtNow);
					catActivoRep.save(objCatalogoActivo);
				} catch (Exception e) {
					throw new ApplicationException(EnumException.ACTIVO_FICHEROS_ADD_FILE_003);
				}
			}else{
				throw new ApplicationException(EnumException.ACTIVO_FICHEROS_ADD_FILE_004);
			}
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
