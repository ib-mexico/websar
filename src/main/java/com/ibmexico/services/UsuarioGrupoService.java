package com.ibmexico.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibmexico.entities.UsuarioGrupoEntity;
import com.ibmexico.repositories.IUsuarioGrupoRepository;

@Service("usuarioGrupoService")
public class UsuarioGrupoService {

	@Autowired
	@Qualifier("usuarioGrupoRepository")
	private IUsuarioGrupoRepository usuarioGrupoRepository;
	
	@Autowired
	@Qualifier("sessionService")
	private SessionService sessionService;
	
	public UsuarioGrupoEntity findByIdUsuarioGrupo(int idUsuarioGrupo) {
		return usuarioGrupoRepository.findByIdUsuarioGrupo(idUsuarioGrupo);
	}
	
	public List<UsuarioGrupoEntity> listUsuariosGruposActivos() {
		return usuarioGrupoRepository.listUsuariosGruposActivos();
	}
}
