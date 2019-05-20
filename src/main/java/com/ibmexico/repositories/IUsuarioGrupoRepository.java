package com.ibmexico.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ibmexico.entities.UsuarioGrupoEntity;

@Repository("usuarioGrupoRepository")
public interface IUsuarioGrupoRepository extends JpaRepository<UsuarioGrupoEntity, Serializable> {

	public abstract UsuarioGrupoEntity findByIdUsuarioGrupo(int idUsuarioGrupo);
	
	@Query("SELECT objGrupo FROM UsuarioGrupoEntity objGrupo WHERE objGrupo.eliminado = false")
	public abstract List<UsuarioGrupoEntity> listUsuariosGruposActivos();
}
