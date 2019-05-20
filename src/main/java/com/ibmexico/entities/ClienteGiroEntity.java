package com.ibmexico.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="clientes_giros")
public class ClienteGiroEntity {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idClienteGiro;
	
	@Column(length = 100)
	private String clienteGiro;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creacion_id_usuario", nullable = false)
	private UsuarioEntity creacionUsuario;

	@Column(nullable = false)
	private LocalDateTime creacionFecha;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "modificacion_id_usuario", nullable = false)
	private UsuarioEntity modificacionUsuario;

	@Column(nullable = false)
	private LocalDateTime modificacionFecha;

	@Column
	private boolean eliminado = false;
	
	
	
	
	
	// ACCESORS METHODS
	public int getIdClienteGiro() {
		return idClienteGiro;
	}

	public void setIdClienteGiro(int idClienteGiro) {
		this.idClienteGiro = idClienteGiro;
	}

	public String getClienteGiro() {
		return clienteGiro;
	}

	public void setClienteGiro(String clienteGiro) {
		this.clienteGiro = clienteGiro;
	}

	public UsuarioEntity getCreacionUsuario() {
		return creacionUsuario;
	}

	public void setCreacionUsuario(UsuarioEntity creacionUsuario) {
		this.creacionUsuario = creacionUsuario;
	}

	public LocalDateTime getCreacionFecha() {
		return creacionFecha;
	}

	public void setCreacionFecha(LocalDateTime creacionFecha) {
		this.creacionFecha = creacionFecha;
	}

	public UsuarioEntity getModificacionUsuario() {
		return modificacionUsuario;
	}

	public void setModificacionUsuario(UsuarioEntity modificacionUsuario) {
		this.modificacionUsuario = modificacionUsuario;
	}

	public LocalDateTime getModificacionFecha() {
		return modificacionFecha;
	}

	public void setModificacionFecha(LocalDateTime modificacionFecha) {
		this.modificacionFecha = modificacionFecha;
	}

	public boolean isEliminado() {
		return eliminado;
	}

	public void setEliminado(boolean eliminado) {
		this.eliminado = eliminado;
	}

	@Override
	public String toString() {
		return "ClienteGiroEntity [idClienteGiro=" + idClienteGiro + ", clienteGiro=" + clienteGiro
				+ ", creacionUsuario=" + creacionUsuario.getIdUsuario() + ", creacionFecha=" + creacionFecha + ", modificacionUsuario="
				+ modificacionUsuario.getIdUsuario() + ", modificacionFecha=" + modificacionFecha + ", eliminado=" + eliminado + "]";
	}
	
	
	
	
	
	
	
	
}
