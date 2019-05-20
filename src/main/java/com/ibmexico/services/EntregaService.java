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
import com.ibmexico.entities.EntregaEntity;
import com.ibmexico.entities.EntregaProductoEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.libraries.DataTable;
import com.ibmexico.repositories.IEntregaRepository;

@Service("entregaService")
public class EntregaService {

	@Autowired
	@Qualifier("entregaRepository")
	private IEntregaRepository entregaRepository;
	
	@Autowired
	@Qualifier("entregaProductoService")
	private EntregaProductoService entregaProductoService;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	public DataTable<EntregaEntity> dataTable(String search, int offset, int limit, String txtBootstrapTableDesde, String txtBootstrapTableHasta) {
		List<EntregaEntity> lstEntregaEntity = null;
		long totalEntregas = 100;
		
		if(search != null) {				
			if(sessionService.hasRol("ENTREGAS_ADMINISTRADOR")) {
				lstEntregaEntity = entregaRepository.findForDataTable(search, DataTable.getPageRequest(offset, limit));
				totalEntregas = entregaRepository.countForDataTable(search);
			} else {
				lstEntregaEntity = entregaRepository.findForDataTable(sessionService.getCurrentUser().getIdUsuario() ,search, DataTable.getPageRequest(offset, limit));
				totalEntregas = entregaRepository.countForDataTable(sessionService.getCurrentUser().getIdUsuario(), search);
			}			
		}

		DataTable<EntregaEntity> returnDataTable = new DataTable<EntregaEntity>(lstEntregaEntity, totalEntregas);
		return returnDataTable;
	}
	
	public EntregaEntity findByIdEntrega(int idEntrega) {
		return entregaRepository.findByIdEntrega(idEntrega);
	}
	
	@Transactional
	public void addProducts(EntregaEntity objEntrega, String[] arrCantidad, String[] arrNumeroParte, String[] arrDescripcion, String[] arrNumeroSerie) {
		
		if(objEntrega != null) {
			if(arrCantidad != null && arrNumeroParte != null && arrDescripcion != null && arrNumeroSerie != null) {
				
				for(int i = 0; i < arrCantidad.length; i++) {
					
					EntregaProductoEntity objProducto = new EntregaProductoEntity();
					
					objProducto.setEntrega(objEntrega);
					objProducto.setCantidad(new Integer(arrCantidad[i]));
					objProducto.setNumeroParte(arrNumeroParte[i]);
					objProducto.setDescripcion(arrDescripcion[i]);
					objProducto.setNumeroSerie(arrNumeroSerie[i]);
					
					entregaProductoService.create(objProducto);
				}
				
			} else {
				throw new ApplicationException(EnumException.ACTIVIDADES_CREATE_002);
			}	
		} else {
			throw new ApplicationException(EnumException.ACTIVIDADES_CREATE_001);
		}
	}
	
	@Transactional
	public void create(EntregaEntity objEntrega, String[] arrCantidad, String[] arrNumeroParte, String[] arrDescripcion, String[] arrNumeroSerie) {
		
		if(objEntrega != null) {
			LocalDateTime ldtNow = LocalDateTime.now();
			UsuarioEntity objUsuarioCreacion = sessionService.getCurrentUser();
			objEntrega.setCreacionFecha(ldtNow);
			objEntrega.setCreacionUsuario(objUsuarioCreacion);
			objEntrega.setModificacionFecha(ldtNow);
			objEntrega.setModificacionUsuario(objUsuarioCreacion);
			entregaRepository.save(objEntrega);
			
			if(arrCantidad != null) {
				addProducts(objEntrega, arrCantidad, arrNumeroParte, arrDescripcion, arrNumeroSerie);
			}
		}
		else {
			throw new ApplicationException(EnumException.ENTREGAS_CREATE_001);
		}
	}
	
	@Transactional
	public void update(EntregaEntity objEntrega, String[] arrCantidad, String[] arrNumeroParte, String[] arrDescripcion, String[] arrNumeroSerie) {
		
		if(objEntrega != null) {
			LocalDateTime ldtNow = LocalDateTime.now();
			UsuarioEntity objUsuarioCreacion = sessionService.getCurrentUser();
			objEntrega.setModificacionFecha(ldtNow);
			objEntrega.setModificacionUsuario(objUsuarioCreacion);
			
			entregaRepository.save(objEntrega);
			entregaProductoService.deleteAll(objEntrega);
			
			if(arrCantidad != null) {
				addProducts(objEntrega, arrCantidad, arrNumeroParte, arrDescripcion, arrNumeroSerie);
			}
		}
		else {
			throw new ApplicationException(EnumException.ENTREGAS_UPDATE_001);
		}
	}
	
	@Transactional
	public void update(EntregaEntity objEntrega, String imgEntrega, String imgRecibe) {
		
		if(objEntrega != null) {						
			
			try {
				String ficheroEntregaNombre = UUID.randomUUID().toString() + ".png";
				String ficheroRecibeNombre = UUID.randomUUID().toString() + ".png";
				
				objEntrega.setUrlFirmaEntrega(ficheroEntregaNombre);
				objEntrega.setUrlFirmaRecibe(ficheroRecibeNombre);
				
				LocalDateTime ldtNow = LocalDateTime.now();
				UsuarioEntity objUsuarioModificacion = sessionService.getCurrentUser();
				objEntrega.setModificacionFecha(ldtNow);
				objEntrega.setModificacionUsuario(objUsuarioModificacion);
				
				addFileEntrega(objEntrega, imgEntrega, imgRecibe);
				
			} catch (ApplicationException | IOException exception) {
				System.out.println(exception.getMessage());
				throw new ApplicationException(EnumException.ENTREGAS_FICHEROS_ADD_FILE_001);
			}			
		}
		else {
			throw new ApplicationException(EnumException.ENTREGAS_UPDATE_001);
		}
	}
	
	@Transactional
	public void addFileEntrega(EntregaEntity objEntrega, String ficheroEntrega, String ficheroRecibe) throws IOException {
		
		URL urlPath = this.getClass().getResource("/");
		byte[] bytesFichero = Base64.getDecoder().decode(ficheroEntrega);
		 
		if(objEntrega != null) {			
            	
			try{
				BufferedImage img = ImageIO.read(new ByteArrayInputStream(bytesFichero));
				File imgFile = new File(urlPath.getPath() +"static/ficheros/entregas/" + objEntrega.getUrlFirmaEntrega());
				ImageIO.write(img, "png", imgFile);
				
				addFileRecibe(objEntrega, ficheroRecibe);
			
            } catch(Exception excepcion) {
            	throw new ApplicationException(EnumException.ENTREGAS_FICHEROS_ADD_FILE_002);
            }        
		} else {
			throw new ApplicationException(EnumException.ENTREGAS_UPDATE_001);
		}		
	}
	
	@Transactional
	public void addFileRecibe(EntregaEntity objEntrega, String fichero) throws IOException {
		
		URL urlPath = this.getClass().getResource("/");
		 
		if(objEntrega != null) {
			byte[] bytesFichero = Base64.getDecoder().decode(fichero);
			
			try {
				
				BufferedImage img = ImageIO.read(new ByteArrayInputStream(bytesFichero));
				File imgFile = new File(urlPath.getPath() +"static/ficheros/entregas/" + objEntrega.getUrlFirmaRecibe());
				ImageIO.write(img, "png", imgFile);
            	
            	entregaRepository.save(objEntrega);
            	
            } catch(Exception excepcion) {
            	throw new ApplicationException(EnumException.ENTREGAS_FICHEROS_ADD_FILE_003);
            }          
		} else {
			throw new ApplicationException(EnumException.ENTREGAS_UPDATE_001);
		}
		
	}
}
