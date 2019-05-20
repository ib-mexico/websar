package com.ibmexico.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Entity
@Table(name = "usuarios_roles")
public class UsuarioRolEntity {

	// @Table(name = "usuarios_roles", uniqueConstraints =
	// @UniqueConstraint(columnNames = { "id_usuario", "rol" }))

	public static Log LOG = LogFactory.getLog(UsuarioRolEntity.class);

	// FIELDS
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique = true, nullable = false)
	private int idUsuarioRol;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario", nullable = false)
	private UsuarioEntity usuario;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_rol", nullable = false)
	private RolEntity rol;

	// ACCESORS METHODS
	public int getIdUsuarioRol() {
		return idUsuarioRol;
	}

	public void setIdUsuarioRol(int idUsuarioRol) {
		this.idUsuarioRol = idUsuarioRol;
	}

	public UsuarioEntity getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioEntity usuario) {
		this.usuario = usuario;
	}

	public RolEntity getRol() {
		return rol;
	}

	public void setRol(RolEntity rol) {
		this.rol = rol;
	}

	// METHODS
	@Override
	public String toString() {
		return "UsuarioRolEntity [idUsuarioRol=" + idUsuarioRol + ", usuario=" + usuario.getIdUsuario() + ", rol=" + rol.getRol() + "]";
	}
}
