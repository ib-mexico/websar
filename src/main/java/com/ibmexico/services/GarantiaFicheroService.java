package com.ibmexico.services;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.entities.GarantiaFicheroEntity;
import com.ibmexico.repositories.IGarantiaFicheroRepository;
import com.ibmexico.services.SessionService;

@Service("garantiaFicheroService")
public class GarantiaFicheroService {

	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	@Autowired
	@Qualifier("garantiaFicheroRepository")
	private IGarantiaFicheroRepository garantiaFicheroRepository;
	
	public List<GarantiaFicheroEntity> listGarantiaFicheros(int idGarantia) {
		return garantiaFicheroRepository.findByGarantia_IdGarantia(idGarantia);
	}
	
	@Transactional
	public void create(GarantiaFicheroEntity objGarantiaFichero, MultipartFile objFile) throws IOException {
		
		URL urlPath = this.getClass().getResource("/");
		 
		if(objGarantiaFichero != null) {
			if(objGarantiaFichero.getUrl() != "") {
				byte[] bytesFichero = objFile.getBytes();
				
	            try( BufferedOutputStream buffStream = new BufferedOutputStream(new FileOutputStream(new File(urlPath.getPath() +"static/ficheros/garantias/" + objGarantiaFichero.getUrl()))) ) {
	            	buffStream.write(bytesFichero);
	            	
	            	LocalDateTime ldtFecha = LocalDateTime.now();
	            	objGarantiaFichero.setCreacionUsuario(sessionService.getCurrentUser());
	            	objGarantiaFichero.setCreacionFecha(ldtFecha);
	            	garantiaFicheroRepository.save(objGarantiaFichero);
	            	
	            } catch(Exception excepcion) {
	            	throw new ApplicationException(EnumException.GARANTIAS_FICHEROS_ADD_FILE_003);
	            }
			} else {
				throw new ApplicationException(EnumException.GARANTIAS_FICHEROS_ADD_FILE_004);
			}
		} else {
			throw new ApplicationException(EnumException.GARANTIAS_FICHEROS_ADD_FILE_001);
		}
		
	}
	
}
