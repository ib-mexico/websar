package com.ibmexico.entities;

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

@Entity
@Table(name="garantias")
public class GarantiaEntity {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idGarantia;
	
	@Formula(value = "(select concat('G-', u.clave, '-', lpad(id_garantia, 5, '0')) from usuarios u WHERE u.id_usuario = creacion_id_usuario)")
	private String folio;
	
	@Column(nullable = true)
	private LocalDate recepcionFecha;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_cliente", nullable = false)
	private ClienteEntity cliente;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_cliente_contacto", nullable = false)
	private ClienteContactoEntity clienteContacto;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_garantia_estatus", nullable = false)
	private GarantiaEstatusEntity garantiaEstatus;
	
	@Column
	private int cantidad;
	
	@Column
	private String producto;
	
	@Column
	private String numeroSerie;
	
	@Lob
	@Column
	private String falla;
	
	@Column
	private boolean doa = false;
	
	@Column
	private boolean rma = false;
	
	@Lob
	@Column
	private String diagnostico;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_proveedor", nullable = false)
	private ProveedorEntity proveedor;
	
	@Column
	private String factura;
	
	@Column(nullable = true)
	private LocalDate entregaFecha;
	
	@Column
	private String nombreRecibe;
	
	@Lob
	@Column
	private String urlFirmaRecepcion;
	
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

	
	
	
	//ACCESORS METHODS
	public int getIdGarantia() {
		return idGarantia;
	}

	public void setIdGarantia(int idGarantia) {
		this.idGarantia = idGarantia;
	}
	
	public String getFolio() {
		return folio;
	}

	public LocalDate getRecepcionFecha() {
		return recepcionFecha;
	}

	public void setRecepcionFecha(LocalDate recepcionFecha) {
		this.recepcionFecha = recepcionFecha;
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

	public GarantiaEstatusEntity getGarantiaEstatus() {
		return garantiaEstatus;
	}

	public void setGarantiaEstatus(GarantiaEstatusEntity garantiaEstatus) {
		this.garantiaEstatus = garantiaEstatus;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public String getProducto() {
		return producto;
	}

	public void setProducto(String producto) {
		this.producto = producto;
	}

	public String getNumeroSerie() {
		return numeroSerie;
	}

	public void setNumeroSerie(String numeroSerie) {
		this.numeroSerie = numeroSerie;
	}

	public String getFalla() {
		return falla;
	}

	public void setFalla(String falla) {
		this.falla = falla;
	}

	public boolean isDoa() {
		return doa;
	}

	public void setDoa(boolean doa) {
		this.doa = doa;
	}

	public boolean isRma() {
		return rma;
	}

	public void setRma(boolean rma) {
		this.rma = rma;
	}

	public String getDiagnostico() {
		return diagnostico;
	}

	public void setDiagnostico(String diagnostico) {
		this.diagnostico = diagnostico;
	}

	public ProveedorEntity getProveedor() {
		return proveedor;
	}

	public void setProveedor(ProveedorEntity proveedor) {
		this.proveedor = proveedor;
	}

	public String getFactura() {
		return factura;
	}

	public void setFactura(String factura) {
		this.factura = factura;
	}
	
	public LocalDate getEntregaFecha() {
		return entregaFecha;
	}
	
	public String getEntregaFechaNatural() {
		
		String fecha = "";
		
		if(entregaFecha != null) {			
			fecha = entregaFecha.format(GeneralConfiguration.getInstance().getDateFormatterNatural());
		}
		
		return fecha;
	}

	public void setEntregaFecha(LocalDate entregaFecha) {
		this.entregaFecha = entregaFecha;
	}
	
	public String getNombreRecibe() {
		return nombreRecibe;
	}

	public void setNombreRecibe(String nombreRecibe) {
		this.nombreRecibe = nombreRecibe;
	}
	
	public String getUrlFirmaRecepcion() {
		return urlFirmaRecepcion;
	}

	public void setUrlFirmaRecepcion(String urlFirmaRecepcion) {
		this.urlFirmaRecepcion = urlFirmaRecepcion;
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
