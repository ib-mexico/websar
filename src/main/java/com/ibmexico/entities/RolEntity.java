package com.ibmexico.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "roles")
public class RolEntity {

	//FIELDS
	@Id	
	@GeneratedValue 
	@Column(unique = true, nullable = false)
	private int idRol;

	@Column(nullable = false, length= 100, unique = true)
	private String rol;
	
	@JoinColumn(name="id_rol_tipo", nullable = false)
	@ManyToOne(fetch = FetchType.EAGER)
	private RolTipo rolTipo;
	
	@JoinColumn(name="id_rol_categoria", nullable = false)
	@ManyToOne(fetch = FetchType.EAGER)
	private RolCategoriaEntity rolCategoria;
	
	@Column(nullable = false, length = 100)
	private String label;
	
	@Column(length = 256)
	private String url;
	
	@Column(length = 10, nullable = true)
	private int orden;

	@Column(nullable = false)
	private boolean menu = false;

	@Column(nullable = false)
	private boolean submenu = false;

	@Column(nullable = true)
	private int idRolSubmenu; 
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "rol")
	private List<UsuarioRolEntity> usuarioRol = new ArrayList<UsuarioRolEntity>();

	// ACCESORS METHODS
	public int getIdRol() {
		return idRol;
	}

	public String getRol() {
		return rol;
	}

	public RolCategoriaEntity getRolCategoria() {
		return rolCategoria;
	}

	public String getLabel() {
		return label;
	}
	
	public boolean isMenu() {
		return menu;
	}

	public String getUrl() {
		return url;
	}

	public List<UsuarioRolEntity> getUsuarioRol() {
		return usuarioRol;
	}
	
	public RolTipo getRolTipo() {
		return rolTipo;
	}

	// METHODS
	@Override
	public String toString() {
		return "RolEntity [idRol=" + idRol + ", rol=" + rol + ", rolCategoria=" + rolCategoria.getRolCategoria() + ", label=" + label
				+ ", url=" + url + ", menu=" + menu + ",orden="+orden+" , submenu ="+submenu+",idRolSubmenu="+idRolSubmenu+"]";
	}
	// @Override
	// public String toString() {
	// 	return "RolEntity [idRol=" + idRol + ", idRolSubmenu=" + idRolSubmenu + ", label=" + label + ", menu=" + menu
	// 			+ ", orden=" + orden + ", rol=" + rol + ", rolCategoria=" + rolCategoria.getRolCategoria() + ", submenu=" + submenu + ", url=" + url + ", usuarioRol=" + usuarioRol + "]";
	// }


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idRol;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RolEntity other = (RolEntity) obj;
		if (idRol != other.idRol)
			return false;
		return true;
	}

	public int getOrden() {
		return orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}

	public boolean isSubmenu() {
		return submenu;
	}

	public void setSubmenu(boolean submenu) {
		this.submenu = submenu;
	}

	public int getIdRolSubmenu() {
		return idRolSubmenu;
	}

	public void setIdRolSubmenu(int idRolSubmenu) {
		this.idRolSubmenu = idRolSubmenu;
	}


	
	
	
}
