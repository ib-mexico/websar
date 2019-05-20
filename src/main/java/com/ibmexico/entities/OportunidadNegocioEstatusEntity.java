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
@Table(name="oportunidades_negocios_estatus")
public class OportunidadNegocioEstatusEntity {

	@Id
	@Column
	@GeneratedValue
	private int idOportunidadNegocioEstatus;
	
	@Column(length = 50)
	private String oportunidadNegocioEstatus;
	
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

	public int getIdOportunidadNegocioEstatus() {
		return idOportunidadNegocioEstatus;
	}

	public void setIdOportunidadNegocioEstatus(int idOportunidadNegocioEstatus) {
		this.idOportunidadNegocioEstatus = idOportunidadNegocioEstatus;
	}

	public String getOportunidadNegocioEstatus() {
		return oportunidadNegocioEstatus;
	}

	public void setOportunidadNegocioEstatus(String oportunidadNegocioEstatus) {
		this.oportunidadNegocioEstatus = oportunidadNegocioEstatus;
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
		return "OportunidadNegocioEstatusEntity [idOportunidadNegocioEstatus=" + idOportunidadNegocioEstatus
				+ ", oportunidadNegocioEstatus=" + oportunidadNegocioEstatus + ", creacionUsuario=" + creacionUsuario.getIdUsuario()
				+ ", creacionFecha=" + creacionFecha + ", modificacionUsuario=" + modificacionUsuario.getIdUsuario()
				+ ", modificacionFecha=" + modificacionFecha + ", eliminado=" + eliminado + "]";
	}
	
	
	
	
	
}
