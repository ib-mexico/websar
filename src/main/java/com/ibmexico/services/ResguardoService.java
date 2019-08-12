package com.ibmexico.services;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.entities.ResguardoEntity;
import com.ibmexico.entities.ResguardoPartidaEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.libraries.DataTable;
import com.ibmexico.repositories.IResguardoRepository;

@Service("resguardoService")
public class ResguardoService {

	@Autowired
	@Qualifier("resguardoRepository")
	private IResguardoRepository resguardoRepository;
	
	@Autowired
	@Qualifier("resguardoPartidaService")
	private ResguardoPartidaService resguardoPartidaService;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	public DataTable<ResguardoEntity> dataTable(String search, int offset, int limit, String txtBootstrapTableDesde, String txtBootstrapTableHasta) {
		List<ResguardoEntity> lstResguardoEntity = null;
		long totalResguardos = 100;
		
		if(search != null) {				
			if(sessionService.hasRol("RESGUARDOS_ADMINISTRADOR")) {
				lstResguardoEntity = resguardoRepository.findForDataTable(search, DataTable.getPageRequest(offset, limit));
				totalResguardos = resguardoRepository.countForDataTable(search);
			} else {
				lstResguardoEntity = resguardoRepository.findForDataTable(sessionService.getCurrentUser().getIdUsuario() ,search, DataTable.getPageRequest(offset, limit));
				totalResguardos = resguardoRepository.countForDataTable(sessionService.getCurrentUser().getIdUsuario(), search);
			}			
		}

		DataTable<ResguardoEntity> returnDataTable = new DataTable<ResguardoEntity>(lstResguardoEntity, totalResguardos);
		return returnDataTable;
	}
	
	public ResguardoEntity findByIdResguardo(int idResguardo) {
		return resguardoRepository.findByIdResguardo(idResguardo);
	}
	
	@Transactional
	public void addPartidas(ResguardoEntity objResguardo, String[] arrCantidad, String[] arrMarca, String[] arrDescripcion, String[] arrNumeroSerie, String[] arrModelo) {
		
		if(objResguardo != null) {
			if(arrCantidad != null && arrMarca != null && arrDescripcion != null && arrNumeroSerie != null) {
				
				for(int i = 0; i < arrCantidad.length; i++) {
					
					ResguardoPartidaEntity objPartida = new ResguardoPartidaEntity();
					
					objPartida.setResguardo(objResguardo);
					objPartida.setCantidad(new Integer(arrCantidad[i]));
					objPartida.setMarca(arrMarca[i]);
					objPartida.setDescripcion(arrDescripcion[i]);
					objPartida.setModelo(arrModelo[i]);
					objPartida.setNumeroSerie(arrNumeroSerie[i]);
					
					resguardoPartidaService.create(objPartida);
				}
				
			} else {
				throw new ApplicationException(EnumException.ACTIVIDADES_CREATE_002);
			}	
		} else {
			throw new ApplicationException(EnumException.ACTIVIDADES_CREATE_001);
		}
	}
	
	@Transactional
	public void create(ResguardoEntity objResguardo, String[] arrCantidad, String[] arrMarca, String[] arrDescripcion, String[] arrNumeroSerie, String[] arrModelo) {
		
		if(objResguardo != null) {
			LocalDateTime ldtNow = LocalDateTime.now();
			UsuarioEntity objUsuarioCreacion = sessionService.getCurrentUser();
			objResguardo.setCreacionFecha(ldtNow);
			objResguardo.setCreacionUsuario(objUsuarioCreacion);
			objResguardo.setModificacionFecha(ldtNow);
			objResguardo.setModificacionUsuario(objUsuarioCreacion);
			resguardoRepository.save(objResguardo);
			
			if(arrCantidad != null) {
				addPartidas(objResguardo, arrCantidad, arrMarca, arrDescripcion, arrNumeroSerie, arrModelo);
			}
		}
		else {
			throw new ApplicationException(EnumException.RESGUARDOS_CREATE_001);
		}
	}
	
	@Transactional
	public void update(ResguardoEntity objResguardo, String[] arrCantidad, String[] arrMarca, String[] arrDescripcion, String[] arrNumeroSerie, String[] arrModelo) {
		
		if(objResguardo != null) {
			LocalDateTime ldtNow = LocalDateTime.now();
			UsuarioEntity objUsuarioCreacion = sessionService.getCurrentUser();
			objResguardo.setModificacionFecha(ldtNow);
			objResguardo.setModificacionUsuario(objUsuarioCreacion);
			
			resguardoRepository.save(objResguardo);
			resguardoPartidaService.deleteAll(objResguardo);
			
			if(arrCantidad != null) {
				addPartidas(objResguardo, arrCantidad, arrMarca, arrDescripcion, arrNumeroSerie, arrModelo);
			}
		}
		else {
			throw new ApplicationException(EnumException.RESGUARDOS_UPDATE_001);
		}
	}
	
	@Transactional
	public void update(ResguardoEntity objResguardo, String imgEntrega, String imgRecibe) {
		
		if(objResguardo != null) {						
			
			try {
				String ficheroEntregaNombre = UUID.randomUUID().toString() + ".png";
				String ficheroRecibeNombre = UUID.randomUUID().toString() + ".png";
				
				objResguardo.setUrlFirmaEntrega(ficheroEntregaNombre);
				objResguardo.setUrlFirmaRecibe(ficheroRecibeNombre);
				
				LocalDateTime ldtNow = LocalDateTime.now();
				UsuarioEntity objUsuarioModificacion = sessionService.getCurrentUser();
				objResguardo.setModificacionFecha(ldtNow);
				objResguardo.setModificacionUsuario(objUsuarioModificacion);
				
				addFileEntrega(objResguardo, imgEntrega, imgRecibe);
				
			} catch (ApplicationException | IOException exception) {
				System.out.println(exception.getMessage());
				throw new ApplicationException(EnumException.RESGUARDOS_FICHEROS_ADD_FILE_001);
			}			
		}
		else {
			throw new ApplicationException(EnumException.RESGUARDOS_UPDATE_001);
		}
	}
	
	@Transactional
	public void addFileEntrega(ResguardoEntity objResguardo, String ficheroEntrega, String ficheroRecibe) throws IOException {
		
		URL urlPath = this.getClass().getResource("/");
		byte[] bytesFichero = Base64.getDecoder().decode(ficheroEntrega);
		 
		if(objResguardo != null) {			
            	
			try{
				BufferedImage img = ImageIO.read(new ByteArrayInputStream(bytesFichero));
				File imgFile = new File(urlPath.getPath() +"static/ficheros/resguardos/" + objResguardo.getUrlFirmaEntrega());
				ImageIO.write(img, "png", imgFile);
				
				addFileRecibe(objResguardo, ficheroRecibe);
			
            } catch(Exception excepcion) {
            	throw new ApplicationException(EnumException.RESGUARDOS_FICHEROS_ADD_FILE_002);
            }        
		} else {
			throw new ApplicationException(EnumException.RESGUARDOS_UPDATE_001);
		}		
	}
	
	@Transactional
	public void addFileRecibe(ResguardoEntity objResguardo, String fichero) throws IOException {
		
		URL urlPath = this.getClass().getResource("/");
		 
		if(objResguardo != null) {
			byte[] bytesFichero = Base64.getDecoder().decode(fichero);
			
			try {
				
				BufferedImage img = ImageIO.read(new ByteArrayInputStream(bytesFichero));
				File imgFile = new File(urlPath.getPath() +"static/ficheros/resguardos/" + objResguardo.getUrlFirmaRecibe());
				ImageIO.write(img, "png", imgFile);
            	
				resguardoRepository.save(objResguardo);
            	
            } catch(Exception excepcion) {
            	throw new ApplicationException(EnumException.RESGUARDOS_FICHEROS_ADD_FILE_003);
            }          
		} else {
			throw new ApplicationException(EnumException.RESGUARDOS_UPDATE_001);
		}
		
	}
}
