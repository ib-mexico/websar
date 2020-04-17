package com.ibmexico.services;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.configurations.GeneralConfiguration;
import com.ibmexico.entities.CotizacionEntity;
import com.ibmexico.entities.CotizacionFicheroEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.repositories.ICotizacionFicheroRepository;

@Service("cotizacionFicheroService")
public class CotizacionFicheroService {

	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	@Autowired
	@Qualifier("cotizacionFicheroRepository")
	private ICotizacionFicheroRepository cotizacionFicheroRepository;
	
	public CotizacionFicheroEntity findIdCotizacionFichero(int idCotizacionFichero) {
		return cotizacionFicheroRepository.findByIdCotizacionFichero(idCotizacionFichero);
	}
	
	public CotizacionFicheroEntity findByIdGasto(int idGasto){
		return cotizacionFicheroRepository.findByGastoIdServicioProveedorMant(idGasto);
	}

	public List<CotizacionFicheroEntity> CotizacionFIcheroByGastoID(int idGasto){
		return cotizacionFicheroRepository.findCotizacionFicheroByGastoID(idGasto);
	}

	public List<CotizacionFicheroEntity> listCotizacionFicheros(int idCotizacion) {
		return cotizacionFicheroRepository.findByCotizacion_IdCotizacion(idCotizacion);
	}
	
	public List<CotizacionFicheroEntity> listDocumentosFacturas(int idCotizacion) {
		return cotizacionFicheroRepository.listTipoDocumento(idCotizacion, 4);
	}
	
	public long countCotizacionFicheroCalidad(int idCotizacion){
		return cotizacionFicheroRepository.countCotizacionFicheroCalidad(idCotizacion);
	}
	public String totalImporteFacturas(int idCotizacion) {
		
		BigDecimal total = null;
		
		total = cotizacionFicheroRepository.sumTipoDocumento(idCotizacion, 4);
				
		return GeneralConfiguration.getInstance().getNumberFormat().format(total);
	}
	
	public List<CotizacionFicheroEntity> listDocumentosTipo(int idCotizacion, int idFicheroTipo) {
		return cotizacionFicheroRepository.listTipoDocumento(idCotizacion, idFicheroTipo);
	}
	
	public List<CotizacionFicheroEntity> listDocumentosTipoPeriodo(int idFicheroTipo, LocalDate ldFechaInicio, LocalDate ldFechaFin) {
		return cotizacionFicheroRepository.listTipoDocumentoPeriodo(idFicheroTipo, ldFechaInicio, ldFechaFin, sessionService.getCurrentUser().getIdUsuario());
	}
	
	public BigDecimal totalImporteDocumentoTipo(int idCotizacion, int idFicheroTipo) {				
		return cotizacionFicheroRepository.sumTipoDocumento(idCotizacion, idFicheroTipo);				
	}
	
	public BigDecimal totalImporteDocumentoTipoPeriodo(int idFicheroTipo, LocalDate ldFechaInicio, LocalDate ldFechaFin) {				
		return cotizacionFicheroRepository.sumTipoDocumentoPeriodo(idFicheroTipo, ldFechaInicio, ldFechaFin, sessionService.getCurrentUser().getIdUsuario());				
	}
	
	public BigDecimal totalImporteDocumentoTipoCotizacionPeriodo(int idFicheroTipo, CotizacionEntity objCotizacion, LocalDate ldFechaInicio, LocalDate ldFechaFin, UsuarioEntity objUsuario) {				
		return cotizacionFicheroRepository.sumTipoDocumentoPeriodo(idFicheroTipo, objCotizacion.getIdCotizacion(), ldFechaInicio, ldFechaFin, objUsuario.getIdUsuario());				
	}
	
	
	
	
	//CREACIÓN DE FICHERO
	@Transactional
	public void addFile(CotizacionFicheroEntity objCotizacionFichero, MultipartFile file) {
		if(objCotizacionFichero != null) {
			if(file != null && !file.isEmpty()) {
	            try {
	            	if(!file.getOriginalFilename().trim().equals("")) {
		            	String ficheroNombre = UUID.randomUUID().toString();
		            	String[] arrNombreFichero = file.getOriginalFilename().split("\\.");
		            	
		            	if(arrNombreFichero.length >= 2) {
		            		ficheroNombre = ficheroNombre + "." + arrNombreFichero[arrNombreFichero.length-1];
		            	}
		            	
		            	objCotizacionFichero.setUrl(ficheroNombre);
		            	
		            	create(objCotizacionFichero, file);
	            	}
	                
	            } catch (Exception e) {
	            	throw new ApplicationException(EnumException.COTIZACIONES_FICHEROS_ADD_FILE_003);
	            }
			} else {
				LocalDateTime ldtFecha = LocalDateTime.now();
				objCotizacionFichero.setCreacionUsuario(sessionService.getCurrentUser());
				objCotizacionFichero.setCreacionFecha(ldtFecha);

				cotizacionFicheroRepository.save(objCotizacionFichero);
			}
		} else {
			throw new ApplicationException(EnumException.COTIZACIONES_FICHEROS_ADD_FILE_001);
		}
	}
	
	@Transactional
	public void create(CotizacionFicheroEntity objCotizacionFichero, MultipartFile objFile) throws IOException {
		
		URL urlPath = this.getClass().getResource("/");
		 
		if(objCotizacionFichero != null) {
			if(objCotizacionFichero.getUrl() != "") {

				System.err.println("hola escribiendo files");
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
	
	
	//MODIFICACIÓN DE FICHERO
	@Transactional
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
	
	public void delete(CotizacionFicheroEntity objCotizacionFichero) {
		if(objCotizacionFichero != null) {
			cotizacionFicheroRepository.delete(objCotizacionFichero);
		} else {
			throw new ApplicationException(EnumException.COTIZACIONES_FICHEROS_DELETE_001);
		}
	}

	public JsonObject GetFicheroEdit(int idGasto){
		JsonObjectBuilder jsonReturn= Json.createObjectBuilder();
		JsonArrayBuilder jsonRow=Json.createArrayBuilder();
		List<CotizacionFicheroEntity> lstFichero=cotizacionFicheroRepository.findByIdGasto(idGasto);

		lstFichero.forEach((item)->{
			jsonRow.add(Json.createObjectBuilder()
			.add("idCotizacionFichero", item.getIdCotizacionFichero())
			.add("name", item.getCotizacion().getFolio())
			.add("id_cotizacion", item.getCotizacion().getIdCotizacion())
			.add("subTotal", item.getImporte())
			.add("id_gasto", item.getGasto().getIdServicioProveedorMant())
			);
		});

		jsonReturn.add("rows", jsonRow);
		return jsonReturn.build();
	}
}
