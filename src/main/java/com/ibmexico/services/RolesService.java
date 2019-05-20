package com.ibmexico.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibmexico.entities.RolCategoriaEntity;
import com.ibmexico.entities.RolEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.entities.UsuarioRolEntity;
import com.ibmexico.repositories.IRolRepository;

@Service("rolesService")
public class RolesService {
	
	@Autowired
	@Qualifier("rolRepository")
	private IRolRepository rolRepository;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	
	public List<RolEntity> findByIdRolIn(List<Integer> lstIdRol) {
		return rolRepository.findByIdRolIn(lstIdRol);
	}
	
	public Map<RolCategoriaEntity, List<RolEntity>> getHierarchyFullRoles() {
	
		List<RolEntity> lstRoles = rolRepository.findAll();
		Map<RolCategoriaEntity, List<RolEntity>> mapHierarchiesRoles = new LinkedHashMap<RolCategoriaEntity, List<RolEntity>>();
		lstRoles.forEach(itemRol -> {
			if (mapHierarchiesRoles.containsKey(itemRol.getRolCategoria())) {
				mapHierarchiesRoles.get(itemRol.getRolCategoria()).add(itemRol);
			} else {
				mapHierarchiesRoles.put( itemRol.getRolCategoria(), new ArrayList<RolEntity>(Arrays.asList(itemRol)) );
			}
		});
		
		return mapHierarchiesRoles;
	}
	

	public Map<RolCategoriaEntity, List<RolEntity>> getHierarchyRoles(UsuarioEntity usuario) {
		
		List<UsuarioRolEntity> lstUsuarioRoles = usuario.getUsuarioRoles();
		Map<RolCategoriaEntity, List<RolEntity>> mapHierarchiesRoles = new LinkedHashMap<RolCategoriaEntity, List<RolEntity>>();
		lstUsuarioRoles.forEach(item -> {
			if (mapHierarchiesRoles.containsKey(item.getRol().getRolCategoria())) {
				mapHierarchiesRoles.get(item.getRol().getRolCategoria()).add(item.getRol());
			} else {
				mapHierarchiesRoles.put(item.getRol().getRolCategoria(), new ArrayList<RolEntity>(Arrays.asList(item.getRol())));
			}
		});
		
		return mapHierarchiesRoles;
	}

	public Map<RolCategoriaEntity, List<RolEntity>> getHierarchyRolesMenu(UsuarioEntity usuario) {

		List<UsuarioRolEntity> lstUsuarioRoles = usuario.getUsuarioRoles();
		Map<RolCategoriaEntity, List<RolEntity>> mapHierarchiesRoles = new HashMap<RolCategoriaEntity, List<RolEntity>>();
		lstUsuarioRoles.forEach(item -> {
			if (item.getRol().isMenu()) {
				if (mapHierarchiesRoles.containsKey(item.getRol().getRolCategoria())) {
					mapHierarchiesRoles.get(item.getRol().getRolCategoria()).add(item.getRol());
				} else {
					mapHierarchiesRoles.put(item.getRol().getRolCategoria(), new ArrayList<RolEntity>(Arrays.asList(item.getRol())));
				}
			}
		});

		return mapHierarchiesRoles;
	}
	
	public List<RolEntity> listRoles() {
		return rolRepository.findAll();
	}
	
	public RolEntity findByIdRol(int idRol) {
		return rolRepository.findByIdRol(idRol);
	}
	
	public RolEntity findByRol(String strRol) {
		return rolRepository.findByRol(strRol);
	}

}
