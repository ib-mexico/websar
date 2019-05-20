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

import org.hibernate.annotations.Formula;

import com.ibmexico.configurations.GeneralConfiguration;

@Entity
@Table(name="ordenes_servicios")
public class OrdenServicioEntity {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idOrdenServicio;
	
	@Formula(value = "(select concat('OS-',u.clave,'-', lpad(id_orden_servicio, 7, '0')) from usuarios u where u.id_usuario = id_usuario_elabora)")
	private String folio;
	
	@Column(nullable = false)
	private LocalDateTime inicioFecha;
	
	@Column(nullable = true)
	private String tiempoEspera;
	
	@Column(nullable = false)
	private LocalDateTime entregaFecha;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_cotizacion", nullable = true)
	private CotizacionEntity cotizacion;
	
	@Column(nullable = true)
	private String osTicket;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_cliente", nullable = false)
	private ClienteEntity cliente;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_cliente_contacto", nullable = false)
	private ClienteContactoEntity clienteContacto;
	
	@Lob
	@Column(nullable = true)
	private String alcanceServicios;
	
	@Lob
	@Column(nullable = true)
	private String bitacoraActividades;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario_elabora", nullable = false)
	private UsuarioEntity usuarioElabora;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario_revisa", nullable = true)
	private UsuarioEntity usuarioRevisa;
	
	@Lob
	@Column(nullable = true)
	private String urlFirmaElabora;
	
	@Lob
	@Column(nullable = true)
	private String urlFirmaRevisa;
	
	@Column(nullable = true)
	private String nombreRecibe;
	
	@Column(nullable = true)
	private String puestoRecibe;
	
	@Column(nullable = true)
	private String telefonoRecibe;
	
	@Column(nullable = true)
	private String correoRecibe;
	
	@Lob
	@Column(nullable = true)
	private String urlFirmaRecibe;
	
	@Column(nullable = false, precision = 14, scale = 2, columnDefinition = "DECIMAL(12,2)")
	private BigDecimal subtotal;
	
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
	public int getIdOrdenServicio() {
		return idOrdenServicio;
	}
	
	public void setIdOrdenServicio(int idOrdenServicio) {
		this.idOrdenServicio = idOrdenServicio;
	}
	
	public String getFolio() {
		return folio;
	}
	
	public void setFolio(String folio) {
		this.folio = folio;
	}
	
	public LocalDateTime getInicioFecha() {
		return inicioFecha;
	}
	
	public String getInicioFechaFullNatural() {
		
		String fechaFull = "";
		
		if(inicioFecha != null) {			
			fechaFull = inicioFecha.format(GeneralConfiguration.getInstance().getDateTimeFullFormatterNatural());
		}
		
		return fechaFull;
	}
	
	public String getInicioFechaNatural() {
		
		String fecha = "";
		
		if(inicioFecha != null) {			
			fecha = inicioFecha.format(GeneralConfiguration.getInstance().getDateFormatterNatural());
		}
		
		return fecha;		
	}
	
	public String getInicioHoraNatural() {
		
		String hora = "";
		
		if(inicioFecha != null) {			
			hora = inicioFecha.format(GeneralConfiguration.getInstance().getTimeFormatterNatural());
		}
		
		return hora;		
	}
	
	public void setInicioFecha(LocalDateTime inicioFecha) {
		this.inicioFecha = inicioFecha;
	}
	
	public String getTiempoEspera() {
		return tiempoEspera;
	}
	
	public void setTiempoEspera(String tiempoEspera) {
		this.tiempoEspera = tiempoEspera;
	}
	
	public LocalDateTime getEntregaFecha() {
		return entregaFecha;
	}
	
	public String getEntregaFechaFullNatural() {
		
		String fechaFull = "";
		
		if(entregaFecha != null) {			
			fechaFull = entregaFecha.format(GeneralConfiguration.getInstance().getDateTimeFullFormatterNatural());
		}
		
		return fechaFull;
	}
	
	public String getEntregaFechaNatural() {
		
		String fecha = "";
		
		if(entregaFecha != null) {			
			fecha = entregaFecha.format(GeneralConfiguration.getInstance().getDateFormatterNatural());
		}
		
		return fecha;
	}
	
	public String getEntregaHoraNatural() {
		
		String hora = "";
		
		if(entregaFecha != null) {			
			hora = entregaFecha.format(GeneralConfiguration.getInstance().getTimeFormatterNatural());
		}
		
		return hora;
	}
	
	public void setEntregaFecha(LocalDateTime entregaFecha) {
		this.entregaFecha = entregaFecha;
	}
	
	public CotizacionEntity getCotizacion() {
		return cotizacion;
	}
	
	public void setCotizacion(CotizacionEntity cotizacion) {
		this.cotizacion = cotizacion;
	}
	
	public String getOsTicket() {
		return osTicket;
	}
	
	public void setOsTicket(String osTicket) {
		this.osTicket = osTicket;
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
	
	public String getAlcanceServicios() {
		return alcanceServicios;
	}
	
	public void setAlcanceServicios(String alcanceServicios) {
		this.alcanceServicios = alcanceServicios;
	}
	
	public String getBitacoraActividades() {
		return bitacoraActividades;
	}
	
	public void setBitacoraActividades(String bitacoraActividades) {
		this.bitacoraActividades = bitacoraActividades;
	}
	
	public UsuarioEntity getUsuarioElabora() {
		return usuarioElabora;
	}
	
	public void setUsuarioElabora(UsuarioEntity usuarioElabora) {
		this.usuarioElabora = usuarioElabora;
	}
	
	public UsuarioEntity getUsuarioRevisa() {
		return usuarioRevisa;
	}
	
	public void setUsuarioRevisa(UsuarioEntity usuarioRevisa) {
		this.usuarioRevisa = usuarioRevisa;
	}
	
	public String getUrlFirmaElabora() {
		return urlFirmaElabora;
	}
	
	public void setUrlFirmaElabora(String urlFirmaElabora) {
		this.urlFirmaElabora = urlFirmaElabora;
	}
	
	public String getUrlFirmaRevisa() {
		return urlFirmaRevisa;
	}
	
	public void setUrlFirmaRevisa(String urlFirmaRevisa) {
		this.urlFirmaRevisa = urlFirmaRevisa;
	}
	
	public String getNombreRecibe() {
		return nombreRecibe;
	}
	
	public void setNombreRecibe(String nombreRecibe) {
		this.nombreRecibe = nombreRecibe;
	}
	
	public String getTelefonoRecibe() {
		return telefonoRecibe;
	}
	
	public void setTelefonoRecibe(String telefonoRecibe) {
		this.telefonoRecibe = telefonoRecibe;
	}
	
	public String getCorreoRecibe() {
		return correoRecibe;
	}
	
	public void setCorreoRecibe(String correoRecibe) {
		this.correoRecibe = correoRecibe;
	}
	
	public String getPuestoRecibe() {
		return puestoRecibe;
	}
	
	public void setPuestoRecibe(String puestoRecibe) {
		this.puestoRecibe = puestoRecibe;
	}
	
	public String getUrlFirmaRecibe() {
		return urlFirmaRecibe;
	}
	
	public void setUrlFirmaRecibe(String urlFirmaRecibe) {
		this.urlFirmaRecibe = urlFirmaRecibe;
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
	
	
	
}
