package com.ibmexico.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
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

import org.hibernate.annotations.Formula;

import com.ibmexico.configurations.GeneralConfiguration;
import com.ibmexico.entities.UsuarioEntity;

@Entity
@Table(name="cotizaciones")
public class CotizacionEntity {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idCotizacion;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_empresa", nullable = true)
	private EmpresaEntity empresa;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario", nullable = true)
	private UsuarioEntity usuario;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario_vendedor", nullable = true)
	private UsuarioEntity usuarioVendedor;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario_implementador", nullable = true)
	private UsuarioEntity usuarioImplementador;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario_cobranza", nullable = true)
	private UsuarioEntity usuarioCobranza;	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_sucursal", nullable = true)
	private SucursalEntity sucursal;
	
	@Formula(value = "(select concat('COT-', e.clave,'-',u.clave,'-', lpad(id_cotizacion, 7, '0')) from usuarios u ,empresas e where u.id_usuario = id_usuario AND e.id_empresa = id_empresa)")
	private String folio;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_cliente", nullable = true)
	private ClienteEntity cliente;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_cliente_contacto", nullable = true)
	private ClienteContactoEntity clienteContacto;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_oportunidad_negocio", nullable = true)
	private OportunidadNegocioEntity oportunidadNegocio;
	
	@Column
	private String concepto;
	
	@Column(length = 100)
	private String ubicacion;
	
	@Column(length = 100)
	private String entregaLugar;
	
	@Column
	private String entregaDiasHabiles;
	
	@Column
	private String vigenciaPrecioDiasHabiles;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_forma_pago")
	private FormaPagoEntity formaPago;
	
	@Lob
	@Column
	private String condicionesPago;
	
	@Column
	private String diasCredito;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_moneda", nullable = true)
	private MonedaEntity moneda;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_cotizacion_estatus", nullable = true)
	private CotizacionEstatusEntity cotizacionEstatus;
	
	@Column(nullable = true)
	private LocalDate aprobacionFecha;
	
	@Column(nullable = true)
	private LocalDate facturacionFecha;
	
	@Column(length = 50, nullable = true)
	private String facturaNumero = "";
	
	@Column(nullable = true)
	private LocalDate pagoFecha;
	
	@Column(length = 50, nullable = true)
	private String pagoReferencia = "";
	
	@Column(nullable = true)
	private LocalDate inicioCobranzaFecha;
	
	@Lob
	@Column
	private String observaciones;
	
	@Column
	private boolean boolVentaCompartida = false;
	
	@Column
	private boolean boolImplementacion = false;
	
	@Column
	private boolean boolMaestra = false;
	
	@Column
	private boolean boolRenta = false;
	
	@Column
	private boolean boolNormal = false;
	
	@Column(nullable = false, precision = 14, scale = 2, columnDefinition = "DECIMAL(12,2)")
	private BigDecimal subtotal;

	@Column(nullable = false, precision = 14, scale = 2, columnDefinition = "DECIMAL(12,2)")
	private BigDecimal ivaPorcentaje;

	@Column(nullable = false, precision = 14, scale = 2, columnDefinition = "DECIMAL(12,2)")
	private BigDecimal iva;

	@Column(nullable = false, precision = 14, scale = 2, columnDefinition = "DECIMAL(12,2)")
	private BigDecimal total;
	
	@Column(length = 50, nullable = true)
	private String requisicionMaterialFolio = "";
	
	@Column(nullable = true)
	private LocalDate solicitudFecha;
	
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
	public int getIdCotizacion() {
		return idCotizacion;
	}

	public void setIdCotizacion(int idCotizacion) {
		this.idCotizacion = idCotizacion;
	}
	
	public EmpresaEntity getEmpresa() {
		return empresa;
	}

	public void setEmpresa(EmpresaEntity empresa) {
		this.empresa = empresa;
	}

	public UsuarioEntity getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioEntity usuario) {
		this.usuario = usuario;
	}
	
	public UsuarioEntity getUsuarioVendedor() {
		return usuarioVendedor;
	}

	public void setUsuarioVendedor(UsuarioEntity usuarioVendedor) {
		this.usuarioVendedor = usuarioVendedor;
	}
	
	public UsuarioEntity getUsuarioImplementador() {
		return usuarioImplementador;
	}

	public void setUsuarioImplementador(UsuarioEntity usuarioImplementador) {
		this.usuarioImplementador = usuarioImplementador;
	}
	
	public UsuarioEntity getUsuarioCobranza() {
		return usuarioCobranza;
	}

	public void setUsuarioCobranza(UsuarioEntity usuarioCobranza) {
		this.usuarioCobranza = usuarioCobranza;
	}

	public SucursalEntity getSucursal() {
		return sucursal;
	}

	public void setSucursal(SucursalEntity sucursal) {
		this.sucursal = sucursal;
	}
	
	public String getFolio() {
		
		String strFolio = "";
		
		if(folio != null) {
			strFolio = folio;
		}
		return strFolio;
	}
	
	public ClienteEntity getCliente() {
		return cliente;
	}

	public void setCliente(ClienteEntity cliente) {
		this.cliente = cliente;
	}

	public ClienteContactoEntity getClienteContacto() {
		return clienteContacto;
	}

	public void setClienteContacto(ClienteContactoEntity clienteContacto) {
		this.clienteContacto = clienteContacto;
	}
	
	public OportunidadNegocioEntity getOportunidadNegocio() {
		
		OportunidadNegocioEntity objOportunidad = null;
		
		if(oportunidadNegocio != null) {
			objOportunidad = oportunidadNegocio;
		}
		return objOportunidad;
	}

	public void setOportunidadNegocio(OportunidadNegocioEntity oportunidadNegocio) {
		this.oportunidadNegocio = oportunidadNegocio;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public String getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public String getEntregaLugar() {
		return entregaLugar;
	}

	public void setEntregaLugar(String entregaLugar) {
		this.entregaLugar = entregaLugar;
	}

	public String getEntregaDiasHabiles() {
		return entregaDiasHabiles;
	}

	public void setEntregaDiasHabiles(String entregaDiasHabiles) {
		this.entregaDiasHabiles = entregaDiasHabiles;
	}

	public String getVigenciaPrecioDiasHabiles() {
		return vigenciaPrecioDiasHabiles;
	}

	public void setVigenciaPrecioDiasHabiles(String vigenciaPrecioDiasHabiles) {
		this.vigenciaPrecioDiasHabiles = vigenciaPrecioDiasHabiles;
	}

	public FormaPagoEntity getFormaPago() {
		return formaPago;
	}

	public void setFormaPago(FormaPagoEntity formaPago) {
		this.formaPago = formaPago;
	}

	public String getCondicionesPago() {
		return condicionesPago;
	}

	public void setCondicionesPago(String condicionesPago) {
		this.condicionesPago = condicionesPago;
	}

	public String getDiasCredito() {
		return diasCredito;
	}

	public void setDiasCredito(String diasCredito) {
		this.diasCredito = diasCredito;
	}

	public MonedaEntity getMoneda() {
		return moneda;
	}

	public void setMoneda(MonedaEntity moneda) {
		this.moneda = moneda;
	}

	public CotizacionEstatusEntity getCotizacionEstatus() {
		return cotizacionEstatus;
	}

	public void setCotizacionEstatus(CotizacionEstatusEntity cotizacionEstatus) {
		this.cotizacionEstatus = cotizacionEstatus;
	}

	public LocalDate getAprobacionFecha() {
		return aprobacionFecha;
	}
	
	public String getAprobacionFechaNatural() {
		
		String fecha = "";
		
		if(this.aprobacionFecha != null) {
			fecha = aprobacionFecha.format(GeneralConfiguration.getInstance().getDateFormatterNatural());
		}
		return fecha;
	}

	public void setAprobacionFecha(LocalDate aprobacionFecha) {
		this.aprobacionFecha = aprobacionFecha;
	}

	public LocalDate getFacturacionFecha() {
		return facturacionFecha;
	}
	
	public String getFacturacionFechaNatural() {
		
		String fecha = "";
		
		if(facturacionFecha != null) {
			fecha = facturacionFecha.format(GeneralConfiguration.getInstance().getDateFormatterNatural());
		}
		
		return fecha;
	}

	public void setFacturacionFecha(LocalDate facturacionFecha) {
		this.facturacionFecha = facturacionFecha;
	}

	public String getFacturaNumero() {
		return facturaNumero;
	}

	public void setFacturaNumero(String facturaNumero) {
		this.facturaNumero = facturaNumero;
	}

	public LocalDate getPagoFecha() {
		return pagoFecha;
	}
	
	public String getPagoFechaNatural() {
		
		String fecha = "";
		
		if(pagoFecha != null) {			
			fecha = pagoFecha.format(GeneralConfiguration.getInstance().getDateFormatterNatural());
		}
		
		return fecha;
	}

	public void setPagoFecha(LocalDate pagoFecha) {
		this.pagoFecha = pagoFecha;
	}

	public String getPagoReferencia() {
		return pagoReferencia;
	}

	public void setPagoReferencia(String pagoReferencia) {
		this.pagoReferencia = pagoReferencia;
	}
	
	public LocalDate getInicioCobranzaFecha() {
		return inicioCobranzaFecha;
	}
	
	public String getInicioCobranzaFechaNatural() {
		
		String fecha = "";
		
		if(inicioCobranzaFecha != null) {			
			fecha = inicioCobranzaFecha.format(GeneralConfiguration.getInstance().getDateFormatterNatural());
		}
		
		return fecha;
	}

	public void setInicioCobranzaFecha(LocalDate inicioCobranzaFecha) {
		this.inicioCobranzaFecha = inicioCobranzaFecha;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	public boolean isVentaCompartida() {
		return boolVentaCompartida;
	}

	public void setVentaCompartida(boolean boolVentaCompartida) {
		this.boolVentaCompartida = boolVentaCompartida;
	}
	
	public boolean isImplementacion() {
		return boolImplementacion;
	}

	public void setImplementacion(boolean boolImplementacion) {
		this.boolImplementacion = boolImplementacion;
	}

	public BigDecimal getSubtotal() {
		return subtotal;
	}
	
	public String getSubtotalNatural() {
		return "$ " + GeneralConfiguration.getInstance().getNumberFormat().format(subtotal);
	}

	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}

	public BigDecimal getIvaPorcentaje() {
		return ivaPorcentaje;
	}
	
	public String getIvaPorcentajeNatural() {
		return GeneralConfiguration.getInstance().getNumberFormat().format(ivaPorcentaje) + "%";
	}

	public void setIvaPorcentaje(BigDecimal ivaPorcentaje) {
		this.ivaPorcentaje = ivaPorcentaje;
	}	

	public BigDecimal getIva() {
		return iva;
	}
	
	public String getIvaNatural() {
		return "$ " + GeneralConfiguration.getInstance().getNumberFormat().format(iva);
	}

	public void setIva(BigDecimal iva) {
		this.iva = iva;
	}

	public BigDecimal getTotal() {
		return total;
	}
	
	public String getTotalNatural() {
		return "$ " + GeneralConfiguration.getInstance().getNumberFormat().format(total);
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public String getRequisicionMaterialFolio() {
		return requisicionMaterialFolio;
	}

	public void setRequisicionMaterialFolio(String requisicionMaterialFolio) {
		this.requisicionMaterialFolio = requisicionMaterialFolio;
	}

	public LocalDate getSolicitudFecha() {
		return solicitudFecha;
	}
	
	public String getSolicitudFechaNatural() {
		return solicitudFecha.format(GeneralConfiguration.getInstance().getDateFormatterNatural());
	}

	public void setSolicitudFecha(LocalDate localDate) {
		this.solicitudFecha = localDate;
	}

	public UsuarioEntity getCreacionUsuario() {
		return creacionUsuario;
	}
	
	public boolean isMaestra() {
		return boolMaestra;
	}

	public void setMaestra(boolean boolMaestra) {
		this.boolMaestra = boolMaestra;
	}
	
	public boolean isRenta() {
		return boolRenta;
	}

	public void setRenta(boolean boolRenta) {
		this.boolRenta = boolRenta;
	}
	
	public boolean isNormal() {
		return boolNormal;
	}

	public void setNormal(boolean boolNormal) {
		this.boolNormal = boolNormal;
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
		return "CotizacionEntity [idCotizacion=" + idCotizacion + ", usuario=" + usuario.getNombreCompleto() + ", sucursal=" + sucursal.getSucursal()
				+ ", folio=" + folio + ", cliente=" + cliente.getCliente() + ", clienteContacto=" + clienteContacto.getContacto() + ", concepto=" + concepto
				+ ", ubicacion=" + ubicacion + ", entregaLugar=" + entregaLugar + ", entregaDiasHabiles="
				+ entregaDiasHabiles + ", vigenciaPrecioDiasHabiles=" + vigenciaPrecioDiasHabiles + ", formaPago="
				+ formaPago.getFormaPago() + ", condicionesPago=" + condicionesPago + ", diasCredito=" + diasCredito + ", moneda="
				+ moneda.getMonedaCodigo() + ", cotizacionEstatus=" + cotizacionEstatus.getCotizacionEstatus() + ", aprobacionFecha=" + aprobacionFecha
				+ ", facturacionFecha=" + facturacionFecha + ", facturaNumero=" + facturaNumero + ", pagoFecha="
				+ pagoFecha + ", pagoReferencia=" + pagoReferencia + ", observaciones=" + observaciones + ", subtotal="
				+ subtotal + ", ivaPorcentaje=" + ivaPorcentaje + ", iva=" + iva + ", total=" + total
				+ ", requisicionMaterialFolio=" + requisicionMaterialFolio + ", solicitudFecha=" + solicitudFecha
				+ ", creacionUsuario=" + creacionUsuario.getIdUsuario() + ", creacionFecha=" + creacionFecha + ", modificacionUsuario="
				+ modificacionUsuario.getIdUsuario() + ", modificacionFecha=" + modificacionFecha + ", eliminado=" + eliminado + "]";
	}
	
	
	
	
}
