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
@Table(name="cotizaciones_usuarios_quotas")
public class CotizacionUsuarioQuotaEntity {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idCotizacionUsuarioQuota;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_cotizacion", nullable = true)
	private CotizacionEntity cotizacion;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario", nullable = true)
	private UsuarioEntity usuario;
	
	@Column(nullable = false, precision = 14, scale = 2, columnDefinition = "DECIMAL(12,2)")
	private BigDecimal valorQuota;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creacion_id_usuario", nullable = false)
	private UsuarioEntity creacionUsuario;

	@Column(nullable = true)
	private LocalDateTime creacionFecha;

	@Column
	private boolean eliminado = false;
	
	
	
	//ACCESORS METHODS		
	public int getIdCotizacionUsuarioQuota() {
		return idCotizacionUsuarioQuota;
	}



	public void setIdCotizacionUsuarioQuota(int idCotizacionUsuarioQuota) {
		this.idCotizacionUsuarioQuota = idCotizacionUsuarioQuota;
	}



	public CotizacionEntity getCotizacion() {
		return cotizacion;
	}



	public void setCotizacion(CotizacionEntity cotizacion) {
		this.cotizacion = cotizacion;
	}



	public UsuarioEntity getUsuario() {
		return usuario;
	}



	public void setUsuario(UsuarioEntity usuario) {
		this.usuario = usuario;
	}



	public BigDecimal getValorQuota() {
		return valorQuota;
	}

	
	public String getValorQuotaNatural() {
		return GeneralConfiguration.getInstance().getNumberFormat().format(valorQuota);
	}

	
	public void setValorQuota(BigDecimal valorQuota) {
		this.valorQuota = valorQuota;
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



	public boolean isEliminado() {
		return eliminado;
	}



	public void setEliminado(boolean eliminado) {
		this.eliminado = eliminado;
	}


	
		
	
}
