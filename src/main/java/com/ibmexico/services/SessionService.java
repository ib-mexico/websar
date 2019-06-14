package com.ibmexico.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ibmexico.entities.RolCategoriaEntity;
import com.ibmexico.entities.RolEntity;
import com.ibmexico.entities.UsuarioEntity;
import com.ibmexico.entities.UsuarioRolEntity;
import com.ibmexico.repositories.IUsuarioRepository;
import com.ibmexico.repositories.IUsuarioRolRepository;

@Service("sessionService")
public class SessionService implements UserDetailsService {
	
	@Autowired
	@Qualifier("usuarioRepository")
	private IUsuarioRepository usuarioRepository;
	
	@Autowired
	@Qualifier("usuarioRolRepository")
	private IUsuarioRolRepository usuarioRolRepository;
	
	@Autowired
	@Qualifier("rolesService")
	private RolesService rolesService;
	
	@Autowired
	@Qualifier("usuarioRolService")
	private UsuarioRolService usuarioRolService;
	
	
	private UsuarioEntity objCurrentUser;
	private String userName;
	private int userType;
	private List<RolEntity> lstRoles;
	private Map<RolEntity, Boolean> mapFullRoles;
	private Map<String, Boolean> mapStringFullRoles;
	private List<String> lstUnderRoles;
	private Map<RolCategoriaEntity, List<RolEntity>> mapCategoriesRoles;
	private Map<RolCategoriaEntity, List<RolEntity>> mapCategoriesRolesMenu;
	
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		clearSession();
		User loadUser = buildUser(userName);
		return loadUser;
	}

	//BASICOS DE USUARIO
	public UsuarioEntity getCurrentUser() {
		if(/* objCurrentUser == null*/ true) {
			User objUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			objCurrentUser = usuarioRepository.findByUsername(objUser.getUsername());
		}

		return objCurrentUser;
	}
	
	//LISTAS DE PRIVILEGIOS Y ROLES
	public List<RolEntity> getRoles() {
			lstRoles = new ArrayList<RolEntity>();
			Collection<SimpleGrantedAuthority> lstOriginalAuthorities = (Collection<SimpleGrantedAuthority>)SecurityContextHolder.getContext().getAuthentication().getAuthorities();
			List<UsuarioRolEntity> lstUsuarioRoles = getCurrentUser().getUsuarioRoles();
			
			for(UsuarioRolEntity itemUsuarioRol : lstUsuarioRoles) {
				for(SimpleGrantedAuthority itemAuthority : lstOriginalAuthorities) {
					if(itemUsuarioRol.getRol().getRol().equals(itemAuthority.getAuthority())) {
						lstRoles.add(itemUsuarioRol.getRol());
						break;
					}
				}
			}

		return lstRoles;
	}
	
	/*
	public Map<RolEntity, Boolean> getFullRolesByCategories() {
		Map<RolEntity, Boolean> mapFullRoles = getFullRoles(objCurrentUser);
		
		RolCategoriaEntity objRolCategoriaActual = null;
		for( RolEntity itemRolEntity : mapFullRoles.keySet()   ) {
			if (objRolCategoriaActual == null) || (objRolCategoriaActual.getIdRolCategoria() != itemRolEntity.getRolCategoria().getIdRolCategoria()) )
				objRolCategoriaActual = itemRolEntity.getRolCategoria();
			}
			
		}
		
		return null;
	}
	*/
	
	public Map<RolEntity, Boolean> getFullRoles() {
		return getFullRoles(objCurrentUser);
	}
	
	public Map<RolEntity, Boolean> getFullRoles(UsuarioEntity objUsuario) {
		Map<RolEntity, Boolean> mapFullRoles = this.mapFullRoles;
			mapFullRoles = new LinkedHashMap<RolEntity, Boolean>();
			
			if(objUsuario != null) {
				List<RolEntity> lstRoles = rolesService.listRoles();
				List<UsuarioRolEntity> lstUsuariosRoles = objUsuario.getUsuarioRoles();
				for(RolEntity itemRol : lstRoles) {
					boolean boolActivo = false;
					for(UsuarioRolEntity itemUsuarioRol : lstUsuariosRoles) {
						if( itemRol.equals( itemUsuarioRol.getRol() ) ) {
							boolActivo = true;
							break;
						}
					}
					
					mapFullRoles.put(itemRol, boolActivo);
				}
			}
		return mapFullRoles;
	}
	
	public Map<String, Boolean> getStringFullRoles() {
		return getStringFullRoles(objCurrentUser);
	}
	
	public Map<String, Boolean> getStringFullRoles(UsuarioEntity objUsuario) {
		
		Map<RolEntity, Boolean> mapFullRoles = getFullRoles();
		Map<String, Boolean> mapStringFullRoles = new LinkedHashMap<String, Boolean>();
		
		for(RolEntity itemRol : mapFullRoles.keySet()) {
			mapStringFullRoles.put(itemRol.getRol(), mapFullRoles.get(itemRol));
		}
		
		return mapStringFullRoles;
	}
	
	public boolean getRolValue(UsuarioEntity objUsuario, String strRol) {
		boolean result = false;
		
		if(objUsuario != null) {
			RolEntity objRol = rolesService.findByRol(strRol);
			if(objRol != null) {
				Map<RolEntity, Boolean> fullRoles = getFullRoles(objUsuario);
	
				for( RolEntity itemRol : fullRoles.keySet() ) {
					if( itemRol.equals(objRol) ) {
						result = fullRoles.getOrDefault(objRol, false);
						break;
					}
				}
			}
		}
		
		return result;
	}
	
	public List<String> getUnderRoles() {
			List<SimpleGrantedAuthority> lstOriginalRoles = (List<SimpleGrantedAuthority>)SecurityContextHolder.getContext().getAuthentication().getAuthorities();
			lstUnderRoles = new ArrayList<String>();		
			
			lstUnderRoles = lstOriginalRoles.stream()
					.filter(item->item.getAuthority()
					.startsWith("_") && item.getAuthority().endsWith("_"))
					.map(item-> item.getAuthority()).collect(Collectors.toList());

		return lstUnderRoles;
	}
	

	//LISTAS PRIVILEGIOS POR CATEGORIAS
	public Map<RolCategoriaEntity, List<RolEntity>> getCategoriesRoles() {

			mapCategoriesRoles = new LinkedHashMap<RolCategoriaEntity, List<RolEntity>>();
			List<RolEntity> lstRoles = getRoles();
			
			lstRoles.forEach(item -> {
				if (mapCategoriesRoles.containsKey(item.getRolCategoria())) {
					mapCategoriesRoles.get(item.getRolCategoria()).add(item);
				} else {
					mapCategoriesRoles.put(item.getRolCategoria(), new ArrayList<RolEntity>(Arrays.asList(item)));
				}
			});

		return mapCategoriesRoles;
	}

	public Map<RolCategoriaEntity, List<RolEntity>> getCategoriesRolesMenu() {
		
		mapCategoriesRolesMenu = new LinkedHashMap<RolCategoriaEntity, List<RolEntity>>();
		List<RolEntity> lstRoles = getRoles();
		
		lstRoles.forEach(item -> {
			if (item.isMenu()) {
				if (mapCategoriesRolesMenu.containsKey(item.getRolCategoria())) {
					mapCategoriesRolesMenu.get(item.getRolCategoria()).add(item);
				} else {
					mapCategoriesRolesMenu.put(item.getRolCategoria(), new ArrayList<RolEntity>(Arrays.asList(item)));
				}
			}
		});

		return mapCategoriesRolesMenu;
	}
	
	
	// CONTROLES DE INICIO DE SESION
	public void clearSession() {
		objCurrentUser = null;
		userName = null;
		userType = 0;
		lstRoles = null;
		lstUnderRoles = null;
		mapCategoriesRoles = null;
		mapCategoriesRolesMenu = null;
	}
	
	private User buildUser(String userName) {
		User currentUser = null;
		UsuarioEntity objUsuario = null;
		
		objUsuario = usuarioRepository.findByUsername(userName);
		currentUser = new User(objUsuario.getUsername(), objUsuario.getPassword(), true, true, true, true, authoritiesFromUsuario(objUsuario));

		return currentUser;
	}

	private List<GrantedAuthority> authoritiesFromUsuario(UsuarioEntity objUsuario) {
		
		List<GrantedAuthority> listAuths = new ArrayList<GrantedAuthority>();
		
		if(objUsuario != null) {

			boolean isAdmin = isAdmin(objUsuario);
			
			if(isAdmin) {
				listAuths.add(new SimpleGrantedAuthority("_ROL_ADMIN_"));
			} else {
				listAuths.add(new SimpleGrantedAuthority("_ROL_USUARIO_"));
			}
			
			for (UsuarioRolEntity usuarioRol : objUsuario.getUsuarioRoles()) {
				if(isAdmin || (!isAdmin && (usuarioRol.getRol().getRolTipo().getIdRolTipo() < 3 ))) {
					listAuths.add(new SimpleGrantedAuthority(usuarioRol.getRol().getRol()));
				}
			}
			
		}

		return listAuths;
	}
	
	
	private boolean isAdmin(UsuarioEntity objUsuario) {
		boolean isAdmin = false;
		
		if(objUsuario != null) {
			for (UsuarioRolEntity usuarioRol : objUsuario.getUsuarioRoles()) {
				if(usuarioRol.getRol().getRolTipo().getIdRolTipo() == 1) {
					isAdmin = true;
					break;
				}
			}
		}	
		
		return isAdmin;
	}



	// VALIDACIÓN DE ACCIONES RESTRINGIDAS
	public boolean isValidAction(int idUsuario, String password, RolEntity objRolEntity) {
		boolean objReturn = false;
		
		UsuarioEntity objUsuario = usuarioRepository.findByIdUsuario(idUsuario);
		objReturn = isValidAction(objUsuario, password, objRolEntity);
		
		return objReturn;
	}
	
	public boolean isValidAction(String username, String password, RolEntity objRolEntity) {
		boolean objReturn = false;
		if(	username != null) {
			UsuarioEntity objUsuario = usuarioRepository.findByUsername(username);
			objReturn = isValidAction(objUsuario, password, objRolEntity);
		}
		
		return objReturn;
	}
	
	
	public boolean isValidAction(UsuarioEntity objUsuario, String password, RolEntity objRolEntity) {
		boolean objReturn = false;
			
			if(objUsuario != null) {
				UsuarioEntity objUsuarioValidar = usuarioRepository.findByIdUsuario(objUsuario.getIdUsuario());
				if(!objUsuarioValidar.isEliminado()) {
					BCryptPasswordEncoder objBCryptPasswordEncoder = new BCryptPasswordEncoder();
					if(objUsuarioValidar.getPassword() != null) {
						if(objBCryptPasswordEncoder.matches(password, objUsuarioValidar.getPassword())) {
							objReturn = true;
						}
					}
				}
			}
		
		return objReturn;
	}
	
	
	
	public UsuarioRolEntity findRol(String rol) {
		return usuarioRolRepository.findByRol_RolAndUsuario_IdUsuario(rol, getCurrentUser().getIdUsuario());
	}
	
	public UsuarioRolEntity findRol(String rol, UsuarioEntity objUsuario) {
		return usuarioRolRepository.findByRol_RolAndUsuario_IdUsuario(rol, objUsuario.getIdUsuario());
	}
	
	public UsuarioRolEntity findRol(RolEntity objRol) {
		UsuarioRolEntity objReturn = null;
		
		if(objRol != null) {
			objReturn = findRol(objRol.getRol());
		}

		return objReturn;
	}
	
	public boolean hasRol(RolEntity objRol) {
		boolean boolReturn = false;
		
		if(objRol != null) {
			UsuarioRolEntity objUsuarioRolEntity = findRol(objRol.getRol());
			if(objUsuarioRolEntity != null) {
				boolReturn = true;
			}
		}
		
		
		return boolReturn;
	}
	
	public boolean hasRol(String rol) {
		boolean boolReturn = false;
		
		if(rol != null) {
			UsuarioRolEntity objUsuarioRolEntity = findRol(rol);
			if(objUsuarioRolEntity != null) {
				boolReturn = true;
			}
		}
		
		return boolReturn;
	}
	
	public boolean hasRol(String rol, UsuarioEntity objUsuario) {
		boolean boolReturn = false;
		
		if(rol != null) {
			UsuarioRolEntity objUsuarioRolEntity = findRol(rol, objUsuario);
			if(objUsuarioRolEntity != null) {
				boolReturn = true;
			}
		}
		
		return boolReturn;
	}

}