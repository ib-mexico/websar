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
@Table(name="cotizaciones_comisiones")
public class CotizacionComisionEntity {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idCotizacionComision;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_cotizacion", nullable = true)
	private CotizacionEntity cotizacion;
	
	@Column(nullable = false, precision = 14, scale = 2, columnDefinition = "DECIMAL(12,2)")
	private BigDecimal utilidadBruta;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario_ejecutivo", nullable = true)
	private UsuarioEntity usuarioEjecutivo;
	
	@Column(nullable = false, precision = 14, scale = 2, columnDefinition = "DECIMAL(12,2)")
	private BigDecimal porcentajeEjecutivo;
	
	@Column(nullable = false, precision = 14, scale = 2, columnDefinition = "DECIMAL(12,2)")
	private BigDecimal comisionEjecutivo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario_cotizante", nullable = true)
	private UsuarioEntity usuarioCotizante;
	
	@Column(nullable = false, precision = 14, scale = 2, columnDefinition = "DECIMAL(12,2)")
	private BigDecimal porcentajeCotizante;
	
	@Column(nullable = false, precision = 14, scale = 2, columnDefinition = "DECIMAL(12,2)")
	private BigDecimal comisionCotizante;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario_vendedor", nullable = true)
	private UsuarioEntity usuarioVendedor;
	
	@Column(nullable = false, precision = 14, scale = 2, columnDefinition = "DECIMAL(12,2)")
	private BigDecimal porcentajeVendedor;
	
	@Column(nullable = false, precision = 14, scale = 2, columnDefinition = "DECIMAL(12,2)")
	private BigDecimal comisionVendedor;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario_implementador", nullable = true)
	private UsuarioEntity usuarioImplementador;
	
	@Column(nullable = false, precision = 14, scale = 2, columnDefinition = "DECIMAL(12,2)")
	private BigDecimal porcentajeImplementador;
	
	@Column(nullable = false, precision = 14, scale = 2, columnDefinition = "DECIMAL(12,2)")
	private BigDecimal comisionImplementador;
	
	@Column(nullable = false, precision = 14, scale = 2, columnDefinition = "DECIMAL(12,2)")
	private BigDecimal totalComisiones;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creacion_id_usuario", nullable = false)
	private UsuarioEntity creacionUsuario;

	@Column(nullable = true)
	private LocalDateTime creacionFecha;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "modificacion_id_usuario", nullable = false)
	private UsuarioEntity modificacionUsuario;

	@Column(nullable = true)
	private LocalDateTime modificacionFecha;

	@Column
	private boolean eliminado = false;
	
	
	
	//ACCESORS METHODS
	public int getIdCotizacionComision() {
		return idCotizacionComision;
	}

	public void setIdCotizacionComision(int idCotizacionComision) {
		this.idCotizacionComision = idCotizacionComision;
	}

	public CotizacionEntity getCotizacion() {
		return cotizacion;
	}

	public void setCotizacion(CotizacionEntity cotizacion) {
		this.cotizacion = cotizacion;
	}

	public BigDecimal getUtilidadBruta() {
		return utilidadBruta;
	}
	
	public String getUtilidadBrutaNatural() {
		return GeneralConfiguration.getInstance().getNumberFormat().format(utilidadBruta);
	}

	public void setUtilidadBruta(BigDecimal utilidadBruta) {
		this.utilidadBruta = utilidadBruta;
	}

	public UsuarioEntity getUsuarioEjecutivo() {
		UsuarioEntity objUsuario = null;
		
		if(usuarioEjecutivo != null) {
			objUsuario = usuarioEjecutivo;
		}
		return objUsuario;
	}

	public void setUsuarioEjecutivo(UsuarioEntity usuarioEjecutivo) {
		this.usuarioEjecutivo = usuarioEjecutivo;
	}

	public BigDecimal getPorcentajeEjecutivo() {
		BigDecimal porcentaje = new BigDecimal(0);
		
		if(porcentajeEjecutivo != null) {
			porcentaje = porcentajeEjecutivo;
		}
		
		return porcentaje;
	}
	
	public String getPorcentajeEjecutivoNatural() {
		String porcentajeNatural = "0%";
		
		if(porcentajeEjecutivo != null) {
			porcentajeNatural = GeneralConfiguration.getInstance().getNumberFormat().format(porcentajeEjecutivo) + "%";
		}
		
		return porcentajeNatural;
	}

	public void setPorcentajeEjecutivo(BigDecimal porcentajeEjecutivo) {
		this.porcentajeEjecutivo = porcentajeEjecutivo;
	}

	public BigDecimal getComisionEjecutivo() {
		BigDecimal comision = new BigDecimal(0);
		
		if(comisionEjecutivo != null) {
			comision = comisionEjecutivo;
		}
		
		return comision;
	}
	
	public String getComisionEjecutivoNatural() {
		String comisionNatural = "$0.00";
		
		if(comisionEjecutivo != null) {
			comisionNatural = GeneralConfiguration.getInstance().getNumberFormat().format(comisionEjecutivo);
		}
		
		return comisionNatural;
	}

	public void setComisionEjecutivo(BigDecimal comisionEjecutivo) {
		this.comisionEjecutivo = comisionEjecutivo;
	}

	public UsuarioEntity getUsuarioCotizante() {
		return usuarioCotizante;
	}

	public void setUsuarioCotizante(UsuarioEntity usuarioCotizante) {
		this.usuarioCotizante = usuarioCotizante;
	}

	public BigDecimal getPorcentajeCotizante() {
		BigDecimal porcentaje = new BigDecimal(0);
		
		if(porcentajeCotizante != null) {
			porcentaje = porcentajeCotizante;
		}
		
		return porcentaje;
	}
	
	public String getPorcentajeCotizanteNatural() {
		String porcentajeNatural = "0%";
		
		if(porcentajeCotizante != null) {
			porcentajeNatural = GeneralConfiguration.getInstance().getNumberFormat().format(porcentajeCotizante) + "%";
		}
		
		return porcentajeNatural;
	}

	public void setPorcentajeCotizante(BigDecimal porcentajeCotizante) {
		this.porcentajeCotizante = porcentajeCotizante;
	}

	public BigDecimal getComisionCotizante() {
		BigDecimal comision = new BigDecimal(0);
		
		if(comisionCotizante != null) {
			comision = comisionCotizante;
		}
		
		return comision;
	}
	
	public String getComisionCotizanteNatural() {
		String comisionNatural = "$0.00";
		
		if(comisionCotizante != null) {
			comisionNatural = GeneralConfiguration.getInstance().getNumberFormat().format(comisionCotizante);
		}
		
		return comisionNatural;
	}

	public void setComisionCotizante(BigDecimal comisionCotizante) {
		this.comisionCotizante = comisionCotizante;
	}

	public UsuarioEntity getUsuarioVendedor() {
		return usuarioVendedor;
	}

	public void setUsuarioVendedor(UsuarioEntity usuarioVendedor) {
		this.usuarioVendedor = usuarioVendedor;
	}

	public BigDecimal getPorcentajeVendedor() {
		BigDecimal porcentaje = new BigDecimal(0);
		
		if(porcentajeVendedor != null) {
			porcentaje = porcentajeVendedor;
		}
		
		return porcentaje;
	}
	
	public String getPorcentajeVendedorNatural() {
		String porcentajeNatural = "0%";
		
		if(porcentajeVendedor != null) {
			porcentajeNatural = GeneralConfiguration.getInstance().getNumberFormat().format(porcentajeVendedor) + "%";
		}
		
		return porcentajeNatural;
	}

	public void setPorcentajeVendedor(BigDecimal porcentajeVendedor) {
		this.porcentajeVendedor = porcentajeVendedor;
	}

	public BigDecimal getComisionVendedor() {
		BigDecimal comision = new BigDecimal(0);
		
		if(comisionVendedor != null) {
			comision = comisionVendedor;
		}
		
		return comision;
	}
	
	public String getComisionVendedorNatural() {
		String comisionNatural = "$0.00";
		
		if(comisionVendedor != null) {
			comisionNatural = GeneralConfiguration.getInstance().getNumberFormat().format(comisionVendedor);
		}
		
		return comisionNatural;
	}

	public void setComisionVendedor(BigDecimal comisionVendedor) {
		this.comisionVendedor = comisionVendedor;
	}

	public UsuarioEntity getUsuarioImplementador() {
		UsuarioEntity objUsuario = null;
		
		if(usuarioImplementador != null) {
			objUsuario = usuarioImplementador;
		}
		return objUsuario;
	}

	public void setUsuarioImplementador(UsuarioEntity usuarioImplementador) {
		this.usuarioImplementador = usuarioImplementador;
	}

	public BigDecimal getPorcentajeImplementador() {
		BigDecimal porcentaje = new BigDecimal(0);
		
		if(porcentajeImplementador != null) {
			porcentaje = porcentajeImplementador;
		}
		
		return porcentaje;
	}
	
	public String getPorcentajeImplementadorNatural() {
		String porcentajeNatural = "0%";
		
		if(porcentajeImplementador != null) {
			porcentajeNatural = GeneralConfiguration.getInstance().getNumberFormat().format(porcentajeImplementador) + "%";
		}
		
		return porcentajeNatural;
	}

	public void setPorcentajeImplementador(BigDecimal porcentajeImplementador) {
		this.porcentajeImplementador = porcentajeImplementador;
	}

	public BigDecimal getComisionImplementador() {
		BigDecimal comision = new BigDecimal(0);
		
		if(comisionImplementador != null) {
			comision = comisionImplementador;
		}
		
		return comision;
	}
	
	public String getComisionImplementadorNatural() {
		String comisionNatural = "$0.00";
		
		if(comisionImplementador != null) {
			comisionNatural = GeneralConfiguration.getInstance().getNumberFormat().format(comisionImplementador);
		}
		
		return comisionNatural;
	}

	public void setComisionImplementador(BigDecimal comisionImplementador) {
		this.comisionImplementador = comisionImplementador;
	}

	public BigDecimal getTotalComisiones() {
		return totalComisiones;
	}
	
	public String getTotalComisionesNatural() {
		return GeneralConfiguration.getInstance().getNumberFormat().format(totalComisiones);
	}

	public void setTotalComisiones(BigDecimal totalComisiones) {
		this.totalComisiones = totalComisiones;
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
		return "CotizacionComisionEntity [idCotizacionComision=" + idCotizacionComision + ", cotizacion=" + cotizacion.getFolio()
				+ ", utilidadBruta=" + utilidadBruta + ", usuarioEjecutivo=" + usuarioEjecutivo
				+ ", porcentajeEjecutivo=" + porcentajeEjecutivo + ", comisionEjecutivo=" + comisionEjecutivo
				+ ", usuarioCotizante=" + usuarioCotizante.getNombre() + ", porcentajeCotizante=" + porcentajeCotizante
				+ ", comisionCotizante=" + comisionCotizante + ", usuarioVendedor=" + usuarioVendedor.getNombre()
				+ ", porcentajeVendedor=" + porcentajeVendedor + ", comisionVendedor=" + comisionVendedor
				+ ", usuarioImplementador=" + usuarioImplementador + ", porcentajeImplementador="
				+ porcentajeImplementador + ", comisionImplementador=" + comisionImplementador + ", totalComisiones="
				+ totalComisiones + ", creacionUsuario=" + creacionUsuario + ", creacionFecha=" + creacionFecha
				+ ", modificacionUsuario=" + modificacionUsuario + ", modificacionFecha=" + modificacionFecha
				+ ", eliminado=" + eliminado + "]";
	}
	
	
	
		
	
}
