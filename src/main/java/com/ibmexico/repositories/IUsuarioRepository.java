package com.ibmexico.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ibmexico.entities.UsuarioEntity;

@Repository("usuarioRepository")
public interface IUsuarioRepository extends JpaRepository<UsuarioEntity, Serializable> {

	public abstract UsuarioEntity findByCorreo(String correo);
	public abstract UsuarioEntity findByClave(String correo);
	public abstract UsuarioEntity findByUsernameAndEliminadoFalse(String username);
	public abstract UsuarioEntity findByIdUsuario(int idUsuario);
	
	@Query("SELECT objUsuario FROM UsuarioEntity objUsuario WHERE objUsuario.eliminado = false")	
	public abstract List<UsuarioEntity> listUsuariosActivos();
	
	@Query("SELECT objUsuario FROM UsuarioEntity objUsuario WHERE objUsuario.eliminado = false AND objUsuario.idUsuario != ?1")	
	public abstract List<UsuarioEntity> listUsuariosActivos(int idUsuario);
	
	@Query("SELECT objUsuario FROM UsuarioEntity objUsuario WHERE objUsuario.eliminado = false AND objUsuario.usuarioGrupo.idUsuarioGrupo != null ORDER BY objUsuario.usuarioGrupo.usuarioGrupo DESC")	
	public abstract List<UsuarioEntity> listUsuariosGruposActivos();
	
	@Query("SELECT objUsuario FROM UsuarioEntity objUsuario WHERE objUsuario.eliminado = false AND objUsuario.usuarioGrupo.idUsuarioGrupo = ?1 ORDER BY objUsuario.nombre ASC")
	public abstract List<UsuarioEntity> listUsuariosGruposActivos(int idUsuarioGrupo);
	
	
	//TABLE
	@Query("SELECT COUNT(objUsuario) FROM UsuarioEntity objUsuario ")	
	public abstract long countForDataTable();
	
	@Query("SELECT COUNT(objUsuario) FROM UsuarioEntity objUsuario WHERE (objUsuario.nombreCompleto like %?1% OR objUsuario.correo like %?1%)")	
	public abstract long countForDataTable(String search);
				
	@Query("SELECT objUsuario FROM UsuarioEntity objUsuario order by objUsuario.nombre ASC")
	public abstract List<UsuarioEntity> findForDataTable(Pageable page);
				
	@Query("SELECT objUsuario FROM UsuarioEntity objUsuario WHERE (objUsuario.nombreCompleto like %?1% OR objUsuario.correo like %?1%) order by objUsuario.nombre ASC")
	public abstract List<UsuarioEntity> findForDataTable(String search,Pageable page);
	
	
	/*
	//LISTADOS
	public abstract List<UsuarioEntity> findByIdUsuarioContainingOrCorreoContainingOrNombreCompletoContaining(int searchIdUsuario, String searchCorreo, String searchNombreCompleto, Pageable page);
	public abstract List<UsuarioEntity> findByIdUsuarioContainingOrCorreoContainingOrNombreCompletoContaining(int searchIdUsuario, String searchCorreo, String searchNombreCompleto);
	public abstract List<UsuarioEntity> findByCorreoContainingOrNombreCompletoContaining(String searchCorreo, String searchNombreCompleto, Pageable page);
	public abstract List<UsuarioEntity> findByCorreoContainingOrNombreCompletoContaining(String searchCorreo, String searchNombreCompleto);
	public abstract List<UsuarioEntity> findByEliminado(boolean eliminado);
	
	@Query(" SELECT COUNT(objUsuario) FROM UsuarioEntity objUsuario WHERE objUsuario.eliminado = ?1 ")
	public abstract long countUsuarios(boolean eliminado);
	
	//CONTADORES
	public abstract long countIdUsuarioContainingOrByCorreoContainingOrNombreCompletoContaining(int searchIdUsuario, String searchCorreo, String searchNombreCompleto);
	public abstract long countByCorreoContainingOrNombreCompletoContaining(String searchCorreo, String searchNombreCompleto);
	
	*/
}
