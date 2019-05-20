package com.ibmexico.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ibmexico.entities.UsuarioRolEntity;

@Repository("usuarioRolRepository")
public interface IUsuarioRolRepository extends JpaRepository<UsuarioRolEntity, Serializable> {
	
	public abstract UsuarioRolEntity findByIdUsuarioRol(int idUsuarioRol);
	
	public abstract UsuarioRolEntity findByRol_IdRolAndUsuario_IdUsuario(int idRol, int idUsuario);
	
	public abstract UsuarioRolEntity findByRol_RolAndUsuario_IdUsuario(String rol, int idUsuario);
	
	public abstract List<UsuarioRolEntity> findByUsuario_IdUsuario(int idUsuario);
	
	
	public abstract List<UsuarioRolEntity> removeByRol_IdRolAndUsuario_IdUsuario(int idRol, int idUsuario);
	public abstract List<UsuarioRolEntity> removeByUsuario_IdUsuario(int idUsuario);
	
	@Modifying
	@Query("DELETE FROM UsuarioRolEntity objRol WHERE objRol.usuario.idUsuario = ?1")
	public void removeUserPrivileges(int idUsuario);
	
	//public abstract List<UsuarioRolEntity> findByRol_IdRol(List<Integer> lstRoles);

}