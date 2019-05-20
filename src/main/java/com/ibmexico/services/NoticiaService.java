package com.ibmexico.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibmexico.entities.NoticiaEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.libraries.DataTable;
import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.repositories.INoticiaRepository;

@Service("noticiaService")
public class NoticiaService {

	@Autowired
	@Qualifier("noticiaRepository")
	private INoticiaRepository noticiaRepository;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	public NoticiaEntity findByIdNoticia(int idNoticia) {
		return noticiaRepository.findByIdNoticia(idNoticia);
	}
	
	public List<NoticiaEntity> listNoticias() {
		return noticiaRepository.listNoticias();
	}
	
	public List<NoticiaEntity> listNoticiasImportantes() {
		return noticiaRepository.listNoticiasImportantes();
	}
	
	public DataTable<NoticiaEntity> dataTable(String search, int offset, int limit, String txtBootstrapTableDesde, String txtBootstrapTableHasta) {
		List<NoticiaEntity> lstNoticiaEntity = null;
		long totalNoticias = 100;
					
		lstNoticiaEntity = noticiaRepository.findForDataTable(search, DataTable.getPageRequest(offset, limit));
		totalNoticias = noticiaRepository.countForDataTable(search);			

		DataTable<NoticiaEntity> returnDataTable = new DataTable<NoticiaEntity>(lstNoticiaEntity, totalNoticias);
		return returnDataTable;
	}
	
	public void create(NoticiaEntity objNoticia) {
		
		if(objNoticia != null) {
			LocalDateTime ldtNow = LocalDateTime.now();
			UsuarioEntity objUsuarioCreacion = sessionService.getCurrentUser();
			objNoticia.setCreacionFecha(ldtNow);
			objNoticia.setCreacionUsuario(objUsuarioCreacion);
			objNoticia.setModificacionFecha(ldtNow);
			objNoticia.setModificacionUsuario(objUsuarioCreacion);
			noticiaRepository.save(objNoticia);
		}
		else {
			throw new ApplicationException(EnumException.NOTICIAS_CREATE_001);
		}
	}
	
	public void update(NoticiaEntity objNoticia) {
		
		if(objNoticia != null) {
			LocalDateTime ldtNow = LocalDateTime.now();
			UsuarioEntity objUsuarioModificacion = sessionService.getCurrentUser();
			objNoticia.setModificacionFecha(ldtNow);
			objNoticia.setModificacionUsuario(objUsuarioModificacion);
			noticiaRepository.save(objNoticia);
		}
		else {
			throw new ApplicationException(EnumException.NOTICIAS_UPDATE_001);
		}
	}
}
