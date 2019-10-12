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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ibmexico.configurations.GeneralConfiguration;

@Entity
@Table(name="cotizaciones_partidas")
public class CotizacionPartidaEntity {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idCotizacionPartida;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_cotizacion", nullable = true)
	private CotizacionEntity cotizacion;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario", nullable = true)
	private UsuarioEntity usuario;
	
	@Column(length = 11)
	private int ordenIndex;
	
	@Column(length = 200, nullable = false)
	private String numeroParte;
	
	@Column(length = 200)
	private String numeroSerie = "";
	
	@Lob
	@Column(nullable = true)
	private String descripcion;
	
	@Column(length = 3)
	private int entregaDiasHabiles;
	
	@Column(length = 11)
	private int cantidad;
	
	@Column(nullable = false, precision = 14, scale = 2, columnDefinition = "DECIMAL(12,2)")
	private BigDecimal precioUnitarioLista;
	
	@Column(nullable = false, precision = 14, scale = 2, columnDefinition = "DECIMAL(12,2)")
	private BigDecimal descuentoPorcentaje;
	
	@Column(nullable = false, precision = 14, scale = 2, columnDefinition = "DECIMAL(12,2)")
	private BigDecimal total;
	
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

	
	
	

	
	// ACCESORS METHODS
	
	public int getIdCotizacionPartida() {
		return idCotizacionPartida;
	}

	public void setIdCotizacionPartida(int idCotizacionPartida) {
		this.idCotizacionPartida = idCotizacionPartida;
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
	
	public int getOrdenIndex() {
		return ordenIndex;
	}

	public void setOrdenIndex(int ordenIndex) {
		this.ordenIndex = ordenIndex;
	}

	public String getNumeroParte() {
		return numeroParte;
	}

	public void setNumeroParte(String numeroParte) {
		this.numeroParte = numeroParte;
	}

	public String getNumeroSerie() {
		return numeroSerie;
	}

	public void setNumeroSerie(String numeroSerie) {
		this.numeroSerie = numeroSerie;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getEntregaDiasHabiles() {
		return entregaDiasHabiles;
	}

	public void setEntregaDiasHabiles(int entregaDiasHabiles) {
		this.entregaDiasHabiles = entregaDiasHabiles;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public BigDecimal getPrecioUnitarioLista() {
		return precioUnitarioLista;
	}
	
	public String getPrecioUnitarioListaNatural() {
		return "$" + GeneralConfiguration.getInstance().getNumberFormat().format(precioUnitarioLista);
	}

	public void setPrecioUnitarioLista(BigDecimal precioUnitarioLista) {
		this.precioUnitarioLista = precioUnitarioLista;
	}

	public BigDecimal getDescuentoPorcentaje() {
		return descuentoPorcentaje;
	}
	
	public String getDescuentoPorcentajeNatural() {
		return GeneralConfiguration.getInstance().getNumberFormat().format(descuentoPorcentaje) + "%";
	}

	public void setDescuentoPorcentaje(BigDecimal descuentoPorcentaje) {
		this.descuentoPorcentaje = descuentoPorcentaje;
	}

	public BigDecimal getPrecioUnitarioDescuento() {
		BigDecimal valor = new BigDecimal(0);

		if(this.descuentoPorcentaje.compareTo(new BigDecimal(0)) == 1) {
			valor = this.precioUnitarioLista.subtract(this.precioUnitarioLista.multiply(this.descuentoPorcentaje).divide(new BigDecimal(100)));
		} else {
			valor = this.precioUnitarioLista;
		}

		return valor.setScale(2, BigDecimal.ROUND_HALF_EVEN);
	}
	
	public String getPrecioUnitarioDescuentoNatural() {
		return "$" + GeneralConfiguration.getInstance().getNumberFormat().format(this.getPrecioUnitarioDescuento());
	}
	
	public BigDecimal getTotal() {
		return total;
	}
	
	public String getTotalNatural() {
		return "$" + GeneralConfiguration.getInstance().getNumberFormat().format(total);
	}
	
	public void setTotal(BigDecimal total) {
		this.total = total;
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
		return "CotizacionPartidaEntity [idCotizacionPartida=" + idCotizacionPartida + ", cotizacion=" + cotizacion.getFolio()
				+ ", usuario=" + usuario.getNombre() + ", numeroParte=" + numeroParte + ", numeroSerie=" + numeroSerie
				+ ", descripcion=" + descripcion + ", entregaDiasHabiles=" + entregaDiasHabiles + ", cantidad="
				+ cantidad + ", precioUnitarioLista=" + precioUnitarioLista + ", descuentoPorcentaje="
				+ descuentoPorcentaje + ", total=" + total + ", creacionUsuario=" + creacionUsuario.getIdUsuario() + ", creacionFecha=" + creacionFecha
				+ ", modificacionUsuario=" + modificacionUsuario.getIdUsuario() + ", modificacionFecha=" + modificacionFecha
				+ ", eliminado=" + eliminado + "]";
	}
	
}
