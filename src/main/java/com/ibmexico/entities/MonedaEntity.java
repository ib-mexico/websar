package com.ibmexico.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ibmexico.configurations.GeneralConfiguration;

@Entity
@Table(name="monedas")
public class MonedaEntity {

	@Id
	@Column
	@GeneratedValue
	private int idMoneda;
	
	@Column(length = 3)
	private String monedaCodigo;
	
	@Column(length = 50)
	private String moneda;
	
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
	public int getIdMoneda() {
		return idMoneda;
	}

	public void setIdMoneda(int idMoneda) {
		this.idMoneda = idMoneda;
	}

	public String getMonedaCodigo() {
		return monedaCodigo;
	}

	public void setMonedaCodigo(String monedaCodigo) {
		this.monedaCodigo = monedaCodigo;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
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

	public String getCreacionFechaNatural() {
		return creacionFecha.format(GeneralConfiguration.getInstance().getDateFormatterNatural());
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
	
	public String getModificacionFechaNatural() {
		return modificacionFecha.format(GeneralConfiguration.getInstance().getDateFormatterNatural());
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
		return "MonedaEntity [idMoneda=" + idMoneda + ", monedaCodigo=" + monedaCodigo + ", moneda=" + moneda
				+ ", creacionUsuario=" + creacionUsuario.getIdUsuario() + ", creacionFecha=" + creacionFecha + ", modificacionUsuario="
				+ modificacionUsuario.getIdUsuario() + ", modificacionFecha=" + modificacionFecha + ", eliminado=" + eliminado + "]";
	}
	
	
	
	
	
	
	
	
	
}
