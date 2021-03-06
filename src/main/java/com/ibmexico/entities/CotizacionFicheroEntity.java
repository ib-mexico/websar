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

import com.ibmexico.configurations.GeneralConfiguration;


@Entity
@Table(name="cotizaciones_ficheros")
public class CotizacionFicheroEntity {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idCotizacionFichero;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private CotizacionEntity cotizacion;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_cotizacion_tipo_fichero", nullable = true)
	private CotizacionTipoFicheroEntity cotizacionTipoFichero;
	
	@Lob
	@Column
	private String observaciones;
	
	@Column(nullable = true)
	private String folio;
	
	@Column(nullable = true)
	private String folioOrdenCompra;

	@Column(nullable = true)
	private String referenciaPago;
	
	@Column(nullable = true, precision = 14, scale = 2, columnDefinition = "DECIMAL(12,2)")
	private BigDecimal importe;
	
	@Column(nullable = true)
	private String banco;
	
	@Column(nullable = true)
	private String proveedor;
	
	@Column(nullable = true)
	private LocalDate vencimientoFecha;
	
	@Column(nullable = true)
	private LocalDateTime inicioLlamada;
	
	@Column
	private String url;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creacion_id_usuario", nullable = false)
	private UsuarioEntity creacionUsuario;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_gasto", nullable = true)
	private ActivoServicioProveedorMant2Entity gasto;
	
	@Column(nullable = false)
	private LocalDateTime creacionFecha;

	
	//ACCESORS METHODS
	public int getIdCotizacionFichero() {
		return idCotizacionFichero;
	}
	
	public String getFicheroFullName() {
		String objRetorno =  null;
		
		if(getFicheroNombre() != null && getFicheroExtension() != null) {
			objRetorno = getFicheroNombre() + getFicheroExtension();
		}
		
		return objRetorno;
	}
	
	public String getFicheroNombre() {
		String objRetorno =  null;
		
		if(this.url != null) {
			String[] arrNombreFichero = this.url.split("\\.");
	    	
	    	if(arrNombreFichero.length >= 2) {
	    		objRetorno = this.url.substring(0, this.url.length() - arrNombreFichero[arrNombreFichero.length-1].length());
	    	}
		}
		
		return objRetorno;
	}
	
	public String getFicheroExtension() {
		String objRetorno =  null;
		
		if(this.url != null) {
			String[] arrNombreFichero = this.url.split("\\.");
	    	
	    	if(arrNombreFichero.length >= 2) {
	    		objRetorno = arrNombreFichero[arrNombreFichero.length-1];
	    	}
		}
		
		return objRetorno;
	}
	
	public String getFicheroTipo() {
		String objRetorno =  null;
		
		if(this.getFicheroExtension() != null) {
			switch(this.getFicheroExtension().toLowerCase()) {
				case "jpg":
				case "jpeg":
				case "png":
				case "gif":
				case "bmp":
					objRetorno = "imagen";
				break;
				
				case "pdf":
					objRetorno = "pdf";
				break;
				
				case "doc":
				case "docx":
					objRetorno = "documento";
				break;
				
				case "xls":
				case "xlsx":
					objRetorno = "hojaCalculo";
				break;
				
				default:
					objRetorno = "otro";
				break;
			}
		}
		
		return objRetorno;
	}

	public void setIdCotizacionFichero(int idCotizacionFichero) {
		this.idCotizacionFichero = idCotizacionFichero;
	}

	public CotizacionEntity getCotizacion() {
		return cotizacion;
	}

	public void setCotizacion(CotizacionEntity cotizacion) {
		this.cotizacion = cotizacion;
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

	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getObservaciones() {
		return observaciones;
	}
	
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getFolio() {
		return folio;
	}

	public void setFolio(String folio) {
		this.folio = folio;
	}
	
	public String getFolioOrdenCompra() {
		return folioOrdenCompra;
	}

	public void setFolioOrdenCompra(String folioOrdenCompra) {
		this.folioOrdenCompra = folioOrdenCompra;
	}

	public BigDecimal getImporte() {
		return importe;
	}
	
	public String getImporteNatural() {
		
		String cantidad = "";
		
		if(importe != null) {
			cantidad = GeneralConfiguration.getInstance().getNumberFormat().format(importe);
		}
		
		return cantidad;
	}

	public void setImporte(BigDecimal importe) {
		this.importe = importe;
	}

	public String getBanco() {
		return banco;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public LocalDate getVencimientoFecha() {
		return vencimientoFecha;
	}
	
	public String getVencimientoFechaNatural() {
		
		String fecha = "";
		
		if(vencimientoFecha != null) {
			fecha = creacionFecha.format(GeneralConfiguration.getInstance().getDateFormatterNatural());
		}
		
		return fecha;
	}

	public void setVencimientoFecha(LocalDate vencimientoFecha) {
		this.vencimientoFecha = vencimientoFecha;
	}

	public CotizacionTipoFicheroEntity getCotizacionTipoFichero() {
		return cotizacionTipoFichero;
	}

	public void setCotizacionTipoFichero(CotizacionTipoFicheroEntity cotizacionTipoFichero) {
		this.cotizacionTipoFichero = cotizacionTipoFichero;
	}

	public String getProveedor() {
		return proveedor;
	}

	public void setProveedor(String proveedor) {
		this.proveedor = proveedor;
	}

	public ActivoServicioProveedorMant2Entity getGasto() {
		return gasto;
	}

	public void setGasto(ActivoServicioProveedorMant2Entity gasto) {
		this.gasto = gasto;
	}

	
	public LocalDateTime getInicioLlamada() {
		return inicioLlamada;
	}


	public String getInicioLlamadaFullNatural() {
		
		String fechaFull = "";
		
		if(inicioLlamada != null) {			
			fechaFull = inicioLlamada.format(GeneralConfiguration.getInstance().getDateTimeFullFormatterNatural());
		}
		
		return fechaFull;
	}
	
	public String getInicioLlamadaNatural() {
		
		String fecha = "";
		
		if(inicioLlamada != null) {			
			fecha = inicioLlamada.format(GeneralConfiguration.getInstance().getDateFormatterNatural());
		}
		
		return fecha;		
	}
	
	public String getInicioLlamadaHoraNatural() {
		
		String hora = "";
		
		if(inicioLlamada != null) {			
			hora = inicioLlamada.format(GeneralConfiguration.getInstance().getTimeFormatterNatural());
		}
		return hora;		
	}

	public void setInicioLlamada(LocalDateTime inicioLlamada) {
		this.inicioLlamada = inicioLlamada;
	}

	public String getReferenciaPago() {
		return referenciaPago;
	}

	public void setReferenciaPago(String referenciaPago) {
		this.referenciaPago = referenciaPago;
	}
	
}
