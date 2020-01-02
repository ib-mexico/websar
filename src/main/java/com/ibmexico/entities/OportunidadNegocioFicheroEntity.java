package com.ibmexico.entities;

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
@Table(name="oportunidades_negocios_ficheros")
public class OportunidadNegocioFicheroEntity {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idOportunidadNegocioFichero;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private OportunidadNegocioEntity oportunidadNegocio;
	
	@Column
	private String titulo;
	
	@Lob
	@Column
	private String descripcion;
	
	@Column
	private String url;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creacion_id_usuario", nullable = false)
	private UsuarioEntity creacionUsuario;
	
	@Column(nullable = false)
	private LocalDateTime creacionFecha;

	/** Campos adicionales para la funcionalidad de la llamada de calidad, se hace uso de la tabla cotizacionTipoFichero debido, a que
	 * es una categoria de tipos de fichero y la llamada de calidad, se encuentra en uno de ellas "CALIDAD" */	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_cotizacion_tipo_fichero", nullable = true)
	private CotizacionTipoFicheroEntity cotizacionTipoFichero;
	
	@Column(nullable = true)
	private LocalDateTime inicioLlamada;
	/**End call */

	//ACCESORS METHODS
	public int getIdOportunidadNegocioFichero() {
		return idOportunidadNegocioFichero;
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

	public void setIdOportunidadNegocioFichero(int idOportunidadNegocioFichero) {
		this.idOportunidadNegocioFichero = idOportunidadNegocioFichero;
	}

	public OportunidadNegocioEntity getOportunidadNegocio() {
		return oportunidadNegocio;
	}

	public void setOportunidadNegocio(OportunidadNegocioEntity oportunidadNegocio) {
		this.oportunidadNegocio = oportunidadNegocio;
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
	
	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public CotizacionTipoFicheroEntity getCotizacionTipoFichero() {
		return cotizacionTipoFichero;
	}

	public void setCotizacionTipoFichero(CotizacionTipoFicheroEntity cotizacionTipoFichero) {
		this.cotizacionTipoFichero = cotizacionTipoFichero;
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
	
}
