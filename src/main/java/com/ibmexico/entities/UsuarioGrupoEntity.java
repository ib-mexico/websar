package com.ibmexico.entities;

import java.math.BigDecimal;
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

import com.ibmexico.configurations.GeneralConfiguration;

@Entity
@Table(name="usurios_grupos")
public class UsuarioGrupoEntity {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idUsuarioGrupo;
	
	@Column(length = 100)
	private String usuarioGrupo;
	
	@Column(nullable = true, precision = 14, scale = 2, columnDefinition = "DECIMAL(12,2)")
	private BigDecimal quotaGrupo;
	
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
	public int getIdUsuarioGrupo() {
		return idUsuarioGrupo;
	}

	public void setIdUsuarioGrupo(int idUsuarioGrupo) {
		this.idUsuarioGrupo = idUsuarioGrupo;
	}

	public String getUsuarioGrupo() {
		return usuarioGrupo;
	}

	public void setUsuarioGrupo(String usuarioGrupo) {
		this.usuarioGrupo = usuarioGrupo;
	}
	
	public BigDecimal getQuotaGrupo() {
		BigDecimal valor = new BigDecimal(0);
		
		if(quotaGrupo != null) {
			valor = quotaGrupo;
		}
		return valor;
	}
	
	public String getQuotaGrupoNatural() {
		String valor = "";
		
		if(quotaGrupo != null) {	
			valor = "$" + GeneralConfiguration.getInstance().getNumberFormat().format(quotaGrupo);
		}
		return valor;
	}
	
	public void setQuotaGrupo(BigDecimal quotaGrupo) {
		this.quotaGrupo = quotaGrupo;
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
	
	
}
