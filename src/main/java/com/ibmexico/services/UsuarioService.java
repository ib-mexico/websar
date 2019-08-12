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

import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.libraries.DataTable;
import com.ibmexico.repositories.IUsuarioRepository;


@Service("usuarioService")
public class UsuarioService {

	@Autowired
	@Qualifier("usuarioRepository")
	private IUsuarioRepository usuarioRepository;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	@Autowired
	@Qualifier("usuarioRolService")
	private UsuarioRolService usuarioRolService;
	
	
	public UsuarioEntity getCurrentUser() {
		return sessionService.getCurrentUser();
	}
	
	public UsuarioEntity findByIdUsuario(int idUsuario) {
		return usuarioRepository.findByIdUsuario(idUsuario);
	}
	
	public UsuarioEntity findByCorreo(String correo) {
		return usuarioRepository.findByCorreo(correo);
	}
	
	public UsuarioEntity findByClave(String clave) {
		return usuarioRepository.findByClave(clave);
	}
		
	public List<UsuarioEntity> listUsuarios() {
		return usuarioRepository.findAll();
	}
	
	public List<UsuarioEntity> listUsuariosActivos() {
		return usuarioRepository.listUsuariosActivos();
	}
	
	public List<UsuarioEntity> listUsuariosActivos(int idUsuario) {
		return usuarioRepository.listUsuariosActivos(idUsuario);
	}

	public JsonObject jsonUsuariosActivos() {
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();

		List<UsuarioEntity> lstUsuarios = usuarioRepository.listUsuariosActivos();

		lstUsuarios.forEach((item)-> {
			jsonRows.add(Json.createObjectBuilder()
				.add("id_usuario", item.getIdUsuario())
				.add("nombre_completo", item.getNombreCompleto())
			);
		});

		jsonReturn.add("rows", jsonRows);
		
		return jsonReturn.build();
	}
	
	public List<UsuarioEntity> listUsuariosGruposActivos() {
		return usuarioRepository.listUsuariosGruposActivos();
	}
	
	public List<UsuarioEntity> listUsuariosGruposActivos(int idUsuarioGrupo) {
		return usuarioRepository.listUsuariosGruposActivos(idUsuarioGrupo);
	}
	
	public JsonObject jsonUsuariosGruposActivos() {
		JsonObjectBuilder jsonReturn = Json.createObjectBuilder();
		JsonArrayBuilder jsonRows = Json.createArrayBuilder();

		List<UsuarioEntity> lstUsuarios = usuarioRepository.listUsuariosGruposActivos();

		lstUsuarios.forEach((item)-> {
			jsonRows.add(Json.createObjectBuilder()
				.add("id_usuario", item.getIdUsuario())
				.add("nombre_completo", item.getNombreCompleto())
				.add("grupo", item.getUsuarioGrupo().getUsuarioGrupo())
			);
		});

		jsonReturn.add("rows", jsonRows);
		
		return jsonReturn.build();
	}
	
	public UsuarioEntity findByIdUsuarioNoEliminado(Integer idUsuario) {
		UsuarioEntity objReturn = null;
		
		objReturn = usuarioRepository.findByIdUsuario(idUsuario);
		if(objReturn != null) {
			if(objReturn.isEliminado()) {
				objReturn = null;
			}
		}
		
		return objReturn;
	}
	
	public void create(UsuarioEntity objUsuario) {		
		
		if(	objUsuario != null) {
			
			UsuarioEntity objUsuarioCorreo = usuarioRepository.findByCorreo(objUsuario.getCorreo());
			if(objUsuarioCorreo == null) {
				
				UsuarioEntity objUsuarioClave = usuarioRepository.findByClave(objUsuario.getClave());
				if(objUsuarioClave == null) {
					
					LocalDateTime ldtNow = LocalDateTime.now();
					UsuarioEntity objUsuarioCreacion = getCurrentUser();
					objUsuario.setCreacionFecha(ldtNow);
					objUsuario.setCreacionUsuario(objUsuarioCreacion);
					objUsuario.setModificacionFecha(ldtNow);
					objUsuario.setModificacionUsuario(objUsuarioCreacion);			
					
					usuarioRepository.save(objUsuario);
				} else {
					throw new ApplicationException(EnumException.USUARIOS_CREATE_003);
				}
			} else {
				throw new ApplicationException(EnumException.USUARIOS_CREATE_002);
			}
		} else {
			throw new ApplicationException(EnumException.USUARIOS_CREATE_001);
		}
		
	}
	
	public void update(UsuarioEntity objUsuario) {
		if(	objUsuario != null) {			
				
			LocalDateTime ldtNow = LocalDateTime.now();
			UsuarioEntity objUsuarioCreacion = getCurrentUser();
			objUsuario.setModificacionFecha(ldtNow);
			objUsuario.setModificacionUsuario(objUsuarioCreacion);
			if(exist(objUsuario)) {
				if(!isDeleted(objUsuario)) {
					usuarioRepository.save(objUsuario);
				} else {
					throw new ApplicationException(EnumException.USUARIOS_UPDATE_004);
				}
			} else {
				throw new ApplicationException(EnumException.USUARIOS_UPDATE_003);
			}
		} else {
			throw new ApplicationException(EnumException.USUARIOS_UPDATE_001);
		}
	}
	
	@Transactional
	public void update(UsuarioEntity objUsuario, MultipartFile objFile) throws IOException {
		if(	objUsuario != null) {			
				
			LocalDateTime ldtNow = LocalDateTime.now();
			UsuarioEntity objUsuarioCreacion = getCurrentUser();
			objUsuario.setModificacionFecha(ldtNow);
			objUsuario.setModificacionUsuario(objUsuarioCreacion);
			if(exist(objUsuario)) {
				if(!isDeleted(objUsuario)) {
					
					if(!objFile.getOriginalFilename().trim().equals("")) {
		            	String ficheroNombre = UUID.randomUUID().toString();
		            	String[] arrNombreFichero = objFile.getOriginalFilename().split("\\.");
		            	
		            	if(arrNombreFichero.length >= 2) {
		            		ficheroNombre = ficheroNombre + "." + arrNombreFichero[arrNombreFichero.length-1];
		            	}
		            	
		            	objUsuario.setImagen(ficheroNombre);
		            	
		            	addFile(objUsuario, objFile);
	            	}
				} else {
					throw new ApplicationException(EnumException.USUARIOS_UPDATE_004);
				}
			} else {
				throw new ApplicationException(EnumException.USUARIOS_UPDATE_003);
			}
		} else {
			throw new ApplicationException(EnumException.USUARIOS_UPDATE_001);
		}
	}
	
	@Transactional
	public void addFile(UsuarioEntity objUsuario, MultipartFile objFile) {
		
		URL urlPath = this.getClass().getResource("/");
		 
		if(objUsuario != null) {
			if(objUsuario.getImagen() != "") {				
				
	            try( BufferedOutputStream buffStream = new BufferedOutputStream(new FileOutputStream(new File(urlPath.getPath() +"static/ficheros/usuarios/" + objUsuario.getImagen()))) ) {
	            	
	            	byte[] bytesFichero = objFile.getBytes();
	            	buffStream.write(bytesFichero);
	            	
	            	usuarioRepository.save(objUsuario);
	            	
	            } catch(Exception excepcion) {
	            	throw new ApplicationException(EnumException.USUARIOS_ADD_FILE_003);
	            }
			} else {
				throw new ApplicationException(EnumException.USUARIOS_ADD_FILE_004);
			}
		} else {
			throw new ApplicationException(EnumException.USUARIOS_ADD_FILE_001);
		}
		
	}
	
	public void delete(UsuarioEntity objUsuario) {
		if(	objUsuario != null) {
			
			if( objUsuario.getCreacionFecha() != null
				&& objUsuario.getCreacionUsuario() != null
				&& objUsuario.getModificacionFecha() != null
				&& objUsuario.getModificacionUsuario() != null) {
				
				LocalDateTime ldtNow = LocalDateTime.now();
				UsuarioEntity objUsuarioCreacion = getCurrentUser();
				objUsuario.setModificacionFecha(ldtNow);
				objUsuario.setModificacionUsuario(objUsuarioCreacion);
				
				if(exist(objUsuario)) {
					if(!isDeleted(objUsuario)) {
						objUsuario.setEliminado(true);
						usuarioRepository.save(objUsuario);
					} else {
						throw new ApplicationException(EnumException.USUARIOS_DELETE_004);
					}
				} else {
					throw new ApplicationException(EnumException.USUARIOS_DELETE_003);
				}
			} else {
				throw new ApplicationException(EnumException.USUARIOS_DELETE_002);
			}
		} else {
			throw new ApplicationException(EnumException.USUARIOS_DELETE_001);
		}
	}
	
	@Transactional
	public void createPrivileges(UsuarioEntity objUsuario, Integer[] roles) {
		usuarioRolService.createAll(objUsuario, roles);
	}
	
	public DataTable<UsuarioEntity> dataTable(String search, int offset, int limit, String txtBootstrapTableDesde, String txtBootstrapTableHasta) {
		List<UsuarioEntity> lstUsuarioEntity = null;
		long totalUsuarios = 100;
		
		if(search != null) {			
			lstUsuarioEntity = usuarioRepository.findForDataTable(search, DataTable.getPageRequest(offset, limit));
			totalUsuarios = usuarioRepository.countForDataTable(search);
		} else {
			lstUsuarioEntity = usuarioRepository.findForDataTable(DataTable.getPageRequest(offset, limit));
			totalUsuarios = usuarioRepository.countForDataTable();
		}				

		DataTable<UsuarioEntity> returnDataTable = new DataTable<UsuarioEntity>(lstUsuarioEntity, totalUsuarios);
		
		return returnDataTable;
	}
	
	@Transactional
	public boolean existByCorreo(String correo) {
		boolean boolReturn = false;
		if(correo != null) {
			UsuarioEntity objUsuario = usuarioRepository.findByCorreo(correo);
			boolReturn = exist(objUsuario);
		}
		return boolReturn;
	}
	
	@Transactional
	public boolean exist(UsuarioEntity objUsuario) {
		boolean objReturn = false;
		if(objUsuario != null) {
			UsuarioEntity objValidateUsuario = usuarioRepository.findByIdUsuario(objUsuario.getIdUsuario());
			objReturn = objValidateUsuario != null;
			
		}
		return objReturn;
	}
	
	@Transactional
	public boolean isDeleted(UsuarioEntity objUsuario) {
		boolean boolReturn = false;
		if(objUsuario != null) {
			UsuarioEntity objValidateUsuario = usuarioRepository.findByIdUsuario(objUsuario.getIdUsuario());
			boolReturn = (objValidateUsuario == null || objValidateUsuario.isEliminado());
		}
		
		return boolReturn;
	}	
	

}
