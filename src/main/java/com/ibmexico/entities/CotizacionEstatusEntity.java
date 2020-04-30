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
@Table(name="cotizaciones_estatus")
public class CotizacionEstatusEntity {

	@Id
	@Column
	@GeneratedValue
	private int idCotizacionEstatus;
	
	@Column(length = 50)
	private String cotizacionEstatus;
	
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

	@Column(length = 10, nullable = true)
	private int orden;

	public int getIdCotizacionEstatus() {
		return idCotizacionEstatus;
	}

	public void setIdCotizacionEstatus(int idCotizacionEstatus) {
		this.idCotizacionEstatus = idCotizacionEstatus;
	}

	public String getCotizacionEstatus() {
		return cotizacionEstatus;
	}

	public void setCotizacionEstatus(String cotizacionEstatus) {
		this.cotizacionEstatus = cotizacionEstatus;
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
		return "CotizacionEstatusEntity [idCotizacionEstatus=" + idCotizacionEstatus + ", cotizacionEstatus="
				+ cotizacionEstatus + ", creacionUsuario=" + creacionUsuario.getIdUsuario() + ", creacionFecha=" + creacionFecha
				+ ", modificacionUsuario=" + modificacionUsuario.getIdUsuario() + ", modificacionFecha=" + modificacionFecha
				+ ", eliminado=" + eliminado + "]";
	}

	public int getOrden() {
		return orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}
	
		
}
