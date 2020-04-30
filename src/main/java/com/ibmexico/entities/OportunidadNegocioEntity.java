package com.ibmexico.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.ibmexico.configurations.GeneralConfiguration;

@Entity
@Table(name="oportunidades_negocios")
public class OportunidadNegocioEntity {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idOportunidadNegocio;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario_vendedor", nullable = true)
	private UsuarioEntity usuarioVendedor;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario_encargado", nullable = true)
	private UsuarioEntity usuarioEncargado;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_cliente", nullable = true)
	private ClienteEntity cliente;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_cliente_contacto", nullable = true)
	private ClienteContactoEntity clienteContacto;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_empresa", nullable = true)
	private EmpresaEntity empresa;
	
	@Column
	private String oportunidad;
	
	@Column(nullable = true)
	private LocalDate cierreFecha;
	
	@Column(nullable = true)
	private LocalDate renovacionFecha;	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_oportunidad_negocio_estatus", nullable = true)
	private OportunidadNegocioEstatusEntity oportunidadNegocioEstatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_opn_negocio_estatus_clasificacion", nullable = true)
	private OportunidadNegocioEstatusClasificacionEntity opnNegocioEstatusClasificacion;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_moneda", nullable = true)
	private MonedaEntity moneda;
	
	@Column(nullable = true, precision = 14, scale = 2, columnDefinition = "DECIMAL(12,2)")
	private BigDecimal tipoCambio;
	
	@Column(nullable = true, precision = 14, scale = 2, columnDefinition = "DECIMAL(12,2)")
	private BigDecimal valorMonedaExtranjera;
	
	@Column(nullable = false, precision = 14, scale = 2, columnDefinition = "DECIMAL(12,2)")
	private BigDecimal ingresoEstimado;
	
	@Column(nullable = false, precision = 14, scale = 2, columnDefinition = "DECIMAL(12,2)")
	private BigDecimal probabilidadPorcentaje;
	
	@Column
	private int prioridad;
	
	@Lob
	@Column
	private String notasInternas;
	
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
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "oportunidad", cascade = CascadeType.ALL)
	private List<ActividadEntity> oportunidadesActividades = new ArrayList<ActividadEntity>();
	
	@OneToMany(mappedBy = "oportunidadNegocio", cascade = CascadeType.ALL)
	private List<CotizacionEntity> cotizaciones = new ArrayList<CotizacionEntity>();
	
	@OneToMany(mappedBy = "oportunidadNegocio", cascade = CascadeType.ALL)
	private List<OportunidadNegocioFicheroEntity> ficheros = new ArrayList<OportunidadNegocioFicheroEntity>();

	
	
	

	// ACCESORS METHODS
	public int getIdOportunidadNegocio() {
		return idOportunidadNegocio;
	}

	public void setIdOportunidadNegocio(int idOportunidadNegocio) {
		this.idOportunidadNegocio = idOportunidadNegocio;
	}

	public UsuarioEntity getUsuarioVendedor() {
		return usuarioVendedor;
	}

	public void setUsuarioVendedor(UsuarioEntity usuario) {
		this.usuarioVendedor = usuario;
	}
	
	public UsuarioEntity getUsuarioEncargado() {
		return usuarioEncargado;
	}

	public void setUsuarioEncargado(UsuarioEntity usuarioEncargado) {
		this.usuarioEncargado = usuarioEncargado;
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
	
	public EmpresaEntity getEmpresa() {
		return empresa;
	}

	public void setEmpresa(EmpresaEntity empresa) {
		this.empresa = empresa;
	}

	public String getOportunidad() {
		return oportunidad.toUpperCase();
	}

	public void setOportunidad(String oportunidad) {
		this.oportunidad = oportunidad;
	}
	
	public LocalDate getCierreFecha() {
		return cierreFecha;
	}
	
	public String getCierreFechaNatural() {
		return cierreFecha.format(GeneralConfiguration.getInstance().getDateFormatterNatural());
	}

	public void setCierreFecha(LocalDate cierreFecha) {
		this.cierreFecha = cierreFecha;
	}
	
	public LocalDate getRenovacionFecha() {
		return renovacionFecha;
	}
	
	public String getRenovacionFechaNatural() {
		return renovacionFecha.format(GeneralConfiguration.getInstance().getDateFormatterNatural());
	}

	public void setRenovacionFecha(LocalDate renovacionFecha) {
		this.renovacionFecha = renovacionFecha;
	}

	public OportunidadNegocioEstatusEntity getOportunidadNegocioEstatus() {
		return oportunidadNegocioEstatus;
	}

	public void setOportunidadNegocioEstatus(OportunidadNegocioEstatusEntity oportunidadNegocioEstatus) {
		this.oportunidadNegocioEstatus = oportunidadNegocioEstatus;
	}
	
	public MonedaEntity getMoneda() {
		return moneda;
	}

	public void setMoneda(MonedaEntity moneda) {
		this.moneda = moneda;
	}
	
	public BigDecimal getTipoCambio() {
		return tipoCambio;
	}
	
	public String getTipoCambioNatural() {
		
		String valor = "0.00";
		
		if(tipoCambio != null) {
			valor = GeneralConfiguration.getInstance().getNumberFormat().format(tipoCambio);			
		}
		
		return valor;
	}
	
	public void setTipoCambio(BigDecimal tipoCambio) {
		this.tipoCambio = tipoCambio;
	}
	
	public BigDecimal getValorMonedaExtranjera() {
		return valorMonedaExtranjera;
	}
	
	public String getValorMonedaExtranjeraNatural() {
		String valor = "0.00";
		
		if(valorMonedaExtranjera != null) {			
			valor = GeneralConfiguration.getInstance().getNumberFormat().format(valorMonedaExtranjera);
		}
		
		return valor;
	}

	public void setValorMonedaExtranjera(BigDecimal valorMonedaExtranjera) {
		this.valorMonedaExtranjera = valorMonedaExtranjera;
	}

	public BigDecimal getIngresoEstimado() {
		return ingresoEstimado;
	}
	
	public String getIngresoEstimadoNatural() {
		return GeneralConfiguration.getInstance().getNumberFormat().format(ingresoEstimado);
	}

	public void setIngresoEstimado(BigDecimal ingresoEstimado) {
		this.ingresoEstimado = ingresoEstimado;
	}
	

	public BigDecimal getProbabilidadPorcentaje() {
		return probabilidadPorcentaje;
	}
	
	public String getProbabilidadPorcentajeNatural() {
		return GeneralConfiguration.getInstance().getNumberFormat().format(probabilidadPorcentaje) + "%";
	}
	
	public void setProbabilidadPorcentaje(BigDecimal probabilidadPorcentaje) {
		this.probabilidadPorcentaje = probabilidadPorcentaje;
	}

	public int getPrioridad() {
		return prioridad;
	}

	public void setPrioridad(int prioridad) {
		this.prioridad = prioridad;
	}

	public String getNotasInternas() {
		return notasInternas;
	}

	public void setNotasInternas(String notasInternas) {
		this.notasInternas = notasInternas;
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

	public List<ActividadEntity> getOportunidadesActividades() {
		return oportunidadesActividades;
	}

	public void setOportunidadesActividades(List<ActividadEntity> oportunidadesActividades) {
		this.oportunidadesActividades = oportunidadesActividades;
	}
	
	public List<CotizacionEntity> getCotizaciones() {
		return cotizaciones;
	}

	public void setCotizaciones(List<CotizacionEntity> cotizaciones) {
		this.cotizaciones = cotizaciones;
	}
	
	public List<OportunidadNegocioFicheroEntity> getFicheros() {
		return ficheros;
	}

	public void setFicheros(List<OportunidadNegocioFicheroEntity> ficheros) {
		this.ficheros = ficheros;
	}

	public OportunidadNegocioEstatusClasificacionEntity getOpnNegocioEstatusClasificacion() {
		return opnNegocioEstatusClasificacion;
	}

	public void setOpnNegocioEstatusClasificacion(
			OportunidadNegocioEstatusClasificacionEntity opnNegocioEstatusClasificacion) {
		this.opnNegocioEstatusClasificacion = opnNegocioEstatusClasificacion;
	}
	
	
	
	
}
