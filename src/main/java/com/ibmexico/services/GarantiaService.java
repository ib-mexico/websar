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
import org.springframework.web.multipart.MultipartFile;

import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.entities.GarantiaEntity;
import com.ibmexico.entities.GarantiaFicheroEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.libraries.DataTable;
import com.ibmexico.repositories.IGarantiaRepository;

@Service("garantiaService")
public class GarantiaService {

	@Autowired
	@Qualifier("garantiaRepository")
	private IGarantiaRepository garantiaRepository;
	
	@Autowired
	@Qualifier("garantiaFicheroService")
	private GarantiaFicheroService garantiaFicheroService;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	public List<GarantiaEntity> listGarantias() {
		return garantiaRepository.findAll();
	}
	
	public DataTable<GarantiaEntity> dataTable(String search, int offset, int limit, String txtBootstrapTableDesde, String txtBootstrapTableHasta) {
		List<GarantiaEntity> lstGarantiaEntity = null;
		long totalGarantias = 100;
		
		if(search != null) {			
			lstGarantiaEntity = garantiaRepository.findForDataTable(search, DataTable.getPageRequest(offset, limit));
			totalGarantias = garantiaRepository.countForDataTable(search);
		} else {
			lstGarantiaEntity = garantiaRepository.findForDataTable(DataTable.getPageRequest(offset, limit));
			totalGarantias = garantiaRepository.countForDataTable();
		}				

		DataTable<GarantiaEntity> returnDataTable = new DataTable<GarantiaEntity>(lstGarantiaEntity, totalGarantias);
		return returnDataTable;
	}
	
	public GarantiaEntity findByIdGarantia(int idGarantia) {
		return garantiaRepository.findByIdGarantia(idGarantia);
	}
	
	public void create(GarantiaEntity objGarantia, String ficheroRecibe, MultipartFile[] arrFiles) {
		
		if(objGarantia != null) {
			
			try {
				
				LocalDateTime ldtNow = LocalDateTime.now();
				UsuarioEntity objUsuarioCreacion = sessionService.getCurrentUser();
				objGarantia.setCreacionFecha(ldtNow);
				objGarantia.setCreacionUsuario(objUsuarioCreacion);
				objGarantia.setModificacionFecha(ldtNow);
				objGarantia.setModificacionUsuario(objUsuarioCreacion);
				
				String ficheroRecibeNombre = UUID.randomUUID().toString() + ".png";
				objGarantia.setUrlFirmaRecepcion(ficheroRecibeNombre);
				
				garantiaRepository.save(objGarantia);
				
				addFileRecibe(objGarantia, ficheroRecibe);
				
				if(arrFiles != null) {
					addFicheros(objGarantia, arrFiles);
				}
				
			} catch(ApplicationException | IOException exception) {
				System.out.println(exception.getMessage());
				throw new ApplicationException(EnumException.GARANTIAS_FICHEROS_ADD_FILE_001);
			}			
		}
		else {
			throw new ApplicationException(EnumException.GARANTIAS_CREATE_001);
		}
	}
	
	public void addFileRecibe(GarantiaEntity objGarantia, String ficheroRecibe) throws IOException {
		
		URL urlPath = this.getClass().getResource("/");
		byte[] bytesFichero = Base64.getDecoder().decode(ficheroRecibe);
		 
		if(objGarantia != null) {			
            	
			try{
				BufferedImage img = ImageIO.read(new ByteArrayInputStream(bytesFichero));
				File imgFile = new File(urlPath.getPath() +"static/ficheros/garantias/" + objGarantia.getUrlFirmaRecepcion());
				ImageIO.write(img, "png", imgFile);				
			
            } catch(Exception excepcion) {
            	throw new ApplicationException(EnumException.GARANTIAS_FICHEROS_ADD_FILE_002);
            }        
		} else {
			throw new ApplicationException(EnumException.GARANTIAS_CREATE_001);
		}
	}
	
	@Transactional
	public void addFicheros(GarantiaEntity objGarantia, MultipartFile[] arrFiles) throws IOException {
		
		if(objGarantia != null) {
			if(arrFiles != null) {
				for(MultipartFile objFile : arrFiles) {
			    	if (objFile != null) {
			            try {
			            	
			            	if(!objFile.getOriginalFilename().trim().equals("")) {
				            	String ficheroNombre = UUID.randomUUID().toString();
				            	String[] arrNombreFichero = objFile.getOriginalFilename().split("\\.");
				            	
				            	if(arrNombreFichero.length >= 2) {
				            		ficheroNombre = ficheroNombre + "." + arrNombreFichero[arrNombreFichero.length-1];
				            	}
				            	
				            	GarantiaFicheroEntity objGarantiaFichero = new GarantiaFicheroEntity();
				            	objGarantiaFichero.setUrl(ficheroNombre);
				            	objGarantiaFichero.setGarantia(objGarantia);
				            	
				            	garantiaFicheroService.create(objGarantiaFichero, objFile);
			            	}
			            } catch(ApplicationException exception) {
			            	throw new ApplicationException(EnumException.GARANTIAS_FICHEROS_ADD_FILE_001);
			            }
			    	} else {
			    		throw new ApplicationException(EnumException.GARANTIAS_FICHEROS_ADD_FILE_001);
			    	}
				}
			} else {
				throw new ApplicationException(EnumException.GARANTIAS_FICHEROS_ADD_FILE_001);
			}
		} else {
			throw new ApplicationException(EnumException.GARANTIAS_FICHEROS_ADD_FILE_001);
		}
	}
	
	public void update(GarantiaEntity objGarantia) {
		
		if(objGarantia != null) {
			LocalDateTime ldtNow = LocalDateTime.now();
			UsuarioEntity objUsuarioModificacion = sessionService.getCurrentUser();
			objGarantia.setModificacionFecha(ldtNow);
			objGarantia.setModificacionUsuario(objUsuarioModificacion);
			garantiaRepository.save(objGarantia);
		}
		else {
			throw new ApplicationException(EnumException.GARANTIAS_UPDATE_001);
		}
	}
}
