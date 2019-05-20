package com.ibmexico.services;

import java.util.Arrays;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibmexico.libraries.notifications.ApplicationException;
import com.ibmexico.libraries.notifications.EnumException;
import com.ibmexico.entities.RolEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.entities.UsuarioRolEntity;
import com.ibmexico.repositories.IUsuarioRolRepository;

@Service("usuarioRolService")
public class UsuarioRolService {

	@Autowired
	@Qualifier("usuarioRolRepository")
	IUsuarioRolRepository usuarioRolRepository;
	
	@Autowired
	@Qualifier("rolesService")
	RolesService rolesService;
	
	@Transactional
	public void createAll(UsuarioEntity objUsuario, Integer[] intRoles) {

		if(objUsuario != null) {
			if(intRoles != null) {
				List<Integer> lstIntegers = Arrays.asList(intRoles);
				List<RolEntity> lstRoles = rolesService.findByIdRolIn(lstIntegers);
				
				usuarioRolRepository.removeUserPrivileges(objUsuario.getIdUsuario());
				
				lstRoles.forEach(itemRol -> {
					UsuarioRolEntity objUsuarioRol = new UsuarioRolEntity();
					objUsuarioRol.setRol(itemRol);
					objUsuarioRol.setUsuario(objUsuario);
					usuarioRolRepository.save(objUsuarioRol);
				});
			}
		} else {
			throw new ApplicationException(EnumException.USUARIOS_ROLES_001);
		}
	}
	
	public UsuarioRolEntity findRol(int idUsuario, int idRol) {
		return usuarioRolRepository.findByRol_IdRolAndUsuario_IdUsuario(idRol, idUsuario);
	}
	
	public List<UsuarioRolEntity> findByUsuario_IdUsuario(int idUsuario) {
		return usuarioRolRepository.findByUsuario_IdUsuario(idUsuario);
	}
	
	public UsuarioRolEntity findRol(UsuarioEntity objUsuario, RolEntity objRol) {
		UsuarioRolEntity objReturn = null;
		
		if(objUsuario != null && objRol != null) {
			objReturn = findRol(objUsuario.getIdUsuario(), objRol.getIdRol());
		}

		return objReturn;
	}
	
	public boolean hasRol(UsuarioEntity objUsuario, RolEntity objRol) {
		boolean boolReturn = false;
		
		if(objUsuario != null && objRol != null) {
			UsuarioRolEntity objUsuarioRolEntity = findRol(objUsuario.getIdUsuario(), objRol.getIdRol());
			if(objUsuarioRolEntity != null) {
				boolReturn = true;
			}
		}
		
		
		return boolReturn;
	}
	
	public boolean hasRol(int idUsuario, int idRol) {
		boolean boolReturn = false;
		
		UsuarioRolEntity objUsuarioRolEntity = findRol(idUsuario, idRol);
		if(objUsuarioRolEntity != null) {
			boolReturn = true;
		}
		
		return boolReturn;
	}
	
}
