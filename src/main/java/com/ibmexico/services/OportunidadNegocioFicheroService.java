package com.ibmexico.services;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.entities.OportunidadNegocioFicheroEntity;
import com.ibmexico.repositories.IOportunidadNegocioFicheroRepository;

@Service("oportunidadNegocioFicheroService")
public class OportunidadNegocioFicheroService {

	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	@Autowired
	@Qualifier("oportunidadNegocioFicheroRepository")
	private IOportunidadNegocioFicheroRepository oportunidadNegocioFicheroRepository;
	
	public OportunidadNegocioFicheroEntity findIdCotizacionFichero(int idOportunidadFichero) {
		return oportunidadNegocioFicheroRepository.findByIdOportunidadNegocioFichero(idOportunidadFichero);
	}
	
	public List<OportunidadNegocioFicheroEntity> listOportunidadFicheros(int idOportunidad) {
		return oportunidadNegocioFicheroRepository.findByOportunidadNegocio_IdOportunidadNegocio(idOportunidad);
	}	
	
	public long countOpnFicheroCalidad(int idOpnCalidad){
		return oportunidadNegocioFicheroRepository.countOpnFicheroCalidad(idOpnCalidad);
	}
		
	//CREACIÓN DE FICHERO
	@Transactional
	public void addFile(OportunidadNegocioFicheroEntity objOportunidadFichero, MultipartFile file) {
		
		if(objOportunidadFichero != null) {
			if(file != null) {
	            try {
	            	
	            	if(!file.getOriginalFilename().trim().equals("")) {
		            	String ficheroNombre = UUID.randomUUID().toString();
		            	String[] arrNombreFichero = file.getOriginalFilename().split("\\.");
		            	
		            	if(arrNombreFichero.length >= 2) {
		            		ficheroNombre = ficheroNombre + "." + arrNombreFichero[arrNombreFichero.length-1];
		            	}
		            	
		            	objOportunidadFichero.setUrl(ficheroNombre);
		            	
		            	create(objOportunidadFichero, file);
	            	}
	                
	            } catch (Exception e) {
	            	throw new ApplicationException(EnumException.OPORTUNIDADES_FICHEROS_ADD_FILE_003);
	            }				
			} else {
				throw new ApplicationException(EnumException.OPORTUNIDADES_FICHEROS_ADD_FILE_002);
			}
		} else {
			throw new ApplicationException(EnumException.OPORTUNIDADES_FICHEROS_ADD_FILE_001);
		}
	}
	
	@Transactional
	public void create(OportunidadNegocioFicheroEntity objOportunidadFichero, MultipartFile objFile) throws IOException {
		
		URL urlPath = this.getClass().getResource("/");
		 
		if(objOportunidadFichero != null) {
			if(objOportunidadFichero.getUrl() != "") {
				byte[] bytesFichero = objFile.getBytes();
				  // Crear carpeta si no existe
				  File fileruta = new File(urlPath.getPath() + "static/ficheros/oportunidades");
				  if (!fileruta.exists()) {
					  fileruta.mkdirs();
				  }
	            try( BufferedOutputStream buffStream = new BufferedOutputStream(new FileOutputStream(new File(urlPath.getPath() +"static/ficheros/oportunidades/" + objOportunidadFichero.getUrl()))) ) {
	            	buffStream.write(bytesFichero);
	            	
	            	LocalDateTime ldtFecha = LocalDateTime.now();
	            	objOportunidadFichero.setCreacionUsuario(sessionService.getCurrentUser());
	            	objOportunidadFichero.setCreacionFecha(ldtFecha);
	            	oportunidadNegocioFicheroRepository.save(objOportunidadFichero);
	            	
	            } catch(Exception excepcion) {
	            	throw new ApplicationException(EnumException.OPORTUNIDADES_FICHEROS_ADD_FILE_003);
	            }
			} else {
				throw new ApplicationException(EnumException.OPORTUNIDADES_FICHEROS_ADD_FILE_004);
			}
		} else {
			throw new ApplicationException(EnumException.OPORTUNIDADES_FICHEROS_ADD_FILE_001);
		}
		
	}
	
	
	//MODIFICACIÓN DE FICHERO
	/*@Transactional
	public void updateFile(CotizacionFicheroEntity objCotizacionFichero, MultipartFile file) {
		
		if(objCotizacionFichero != null) {
			if(file != null) {
	            try {
	            	
	            	if(!file.getOriginalFilename().trim().equals("")) {
		            	String ficheroNombre = UUID.randomUUID().toString();
		            	String[] arrNombreFichero = file.getOriginalFilename().split("\\.");
		            	
		            	if(arrNombreFichero.length >= 2) {
		            		ficheroNombre = ficheroNombre + "." + arrNombreFichero[arrNombreFichero.length-1];
		            	}
		            	
		            	objCotizacionFichero.setUrl(ficheroNombre);
		            	
		            	update(objCotizacionFichero, file);
	            	}
	                
	            } catch (Exception e) {
	            	throw new ApplicationException(EnumException.COTIZACIONES_FICHEROS_ADD_FILE_003);
	            }				
			} else {
				cotizacionFicheroRepository.save(objCotizacionFichero);
			}
		} else {
			throw new ApplicationException(EnumException.COTIZACIONES_FICHEROS_ADD_FILE_001);
		}
	}
	
	@Transactional
	public void update(CotizacionFicheroEntity objCotizacionFichero, MultipartFile objFile) throws IOException {
		
		URL urlPath = this.getClass().getResource("/");
		 
		if(objCotizacionFichero != null) {
			if(objCotizacionFichero.getUrl() != "") {
				byte[] bytesFichero = objFile.getBytes();
				
	            try( BufferedOutputStream buffStream = new BufferedOutputStream(new FileOutputStream(new File(urlPath.getPath() +"static/ficheros/cotizaciones/" + objCotizacionFichero.getUrl()))) ) {
	            	buffStream.write(bytesFichero);
	            	
	            	LocalDateTime ldtFecha = LocalDateTime.now();
	            	objCotizacionFichero.setCreacionUsuario(sessionService.getCurrentUser());
	            	objCotizacionFichero.setCreacionFecha(ldtFecha);
	            	cotizacionFicheroRepository.save(objCotizacionFichero);
	            	
	            } catch(Exception excepcion) {
	            	throw new ApplicationException(EnumException.COTIZACIONES_FICHEROS_ADD_FILE_003);
	            }
			} else {
				throw new ApplicationException(EnumException.COTIZACIONES_FICHEROS_ADD_FILE_004);
			}
		} else {
			throw new ApplicationException(EnumException.COTIZACIONES_FICHEROS_ADD_FILE_001);
		}
		
	}
	
	*/
}
