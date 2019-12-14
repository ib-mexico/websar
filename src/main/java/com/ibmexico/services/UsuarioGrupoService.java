package com.ibmexico.services;

import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

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

	public JsonObject jsonUsuarioGrupoActivos(){
		JsonObjectBuilder jsonReturn= Json.createObjectBuilder();
		JsonArrayBuilder jsonRows=Json.createArrayBuilder();
		List<UsuarioGrupoEntity> lstUserGrupo=usuarioGrupoRepository.listUsuariosGruposActivos();
		lstUserGrupo.forEach((item)-> {
			jsonRows.add(Json.createObjectBuilder()
				.add("idUsuarioGrupo", item.getIdUsuarioGrupo())
				.add("nombre_completo", item.getUsuarioGrupo())
				);
			});
		jsonReturn.add("jsonGrupo", jsonRows);
		
		return jsonReturn.build();

	}
}
