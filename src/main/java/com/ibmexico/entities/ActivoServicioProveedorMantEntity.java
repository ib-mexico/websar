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

@Table(name = "activo_servicio_proveedor_mantenimiento")
@Entity
public class ActivoServicioProveedorMantEntity {
    @Id
    @GeneratedValue(strategy =GenerationType.AUTO)
    private int idServicioProveedorMant;

    @Column(nullable = true, precision = 14, scale = 2, columnDefinition = "DECIMAL(12,2)")
    private BigDecimal precioServicioProveedor;

    @Column(nullable = true)
    private String urlCotizacion;

    @Column(nullable = true)
    private String observaciones;

	@Column(nullable = true)
	private boolean aceptado = false;

    @ManyToOne
    @JoinColumn(name = "id_activo_servicio_proveedor")
    private ActivoServicioProveedorEntity  activoServicioProveedor;

    @ManyToOne
    @JoinColumn(name = "id_bien_detalle_mantenimiento")
    private BienDetalleMantenimientoEntity bienDetalleMant;

    //usuario y fecha cuando crean y modifican
    @Column(nullable = true)
    private LocalDateTime creacionFecha;
    
    @Column(nullable = true)
    private LocalDateTime modificacionFecha;

    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creacion_id_usuario", nullable = false)
	private UsuarioEntity creacionUsuario;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "modificacion_id_usuario", nullable = false)
	private UsuarioEntity modificacionUsuario;

    public int getIdServicioProveedorMant() {
        return idServicioProveedorMant;
    }

    public void setIdServicioProveedorMant(int idServicioProveedorMant) {
        this.idServicioProveedorMant = idServicioProveedorMant;
    }

    public BigDecimal getPrecioServicioProveedor() {
        return precioServicioProveedor;
    }

    public void setPrecioServicioProveedor(BigDecimal precioServicioProveedor) {
        this.precioServicioProveedor = precioServicioProveedor;
    }

    
    public ActivoServicioProveedorEntity getActivoServicioProveedor() {
        return activoServicioProveedor;
    }

    public void setActivoServicioProveedor(ActivoServicioProveedorEntity activoServicioProveedor) {
        this.activoServicioProveedor = activoServicioProveedor;
    }
    
    public BienDetalleMantenimientoEntity getBienDetalleMant() {
        return bienDetalleMant;
    }

    public void setBienDetalleMant(BienDetalleMantenimientoEntity bienDetalleMant) {
        this.bienDetalleMant = bienDetalleMant;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    public LocalDateTime getCreacionFecha() {
        return creacionFecha;
    }
    // formateo de la fecha 
    public String getCreacionFechaNatural() {
        return creacionFecha.format(GeneralConfiguration.getInstance().getDateFormatterNatural());
    }
    
    public void setCreacionFecha(LocalDateTime creacionFecha) {
        this.creacionFecha = creacionFecha;
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

    public UsuarioEntity getCreacionUsuario() {
        return creacionUsuario;
    }
    
    public void setCreacionUsuario(UsuarioEntity creacionUsuario) {
        this.creacionUsuario = creacionUsuario;
    }
    
    public UsuarioEntity getModificacionUsuario() {
        return modificacionUsuario;
    }

    public void setModificacionUsuario(UsuarioEntity modificacionUsuario) {
        this.modificacionUsuario = modificacionUsuario;
    }
    public String getUrlCotizacion() {
        return urlCotizacion;
    }

    public void setUrlCotizacion(String urlCotizacion) {
        this.urlCotizacion = urlCotizacion;
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
		
		if(this.urlCotizacion != null) {
			String[] arrNombreFichero = this.urlCotizacion.split("\\.");
	    	
	    	if(arrNombreFichero.length >= 2) {
	    		objRetorno = this.urlCotizacion.substring(0, this.urlCotizacion.length() - arrNombreFichero[arrNombreFichero.length-1].length());
	    	}
		}
		
		return objRetorno;
	}
	
	public String getFicheroExtension() {
		String objRetorno =  null;
		
		if(this.urlCotizacion != null) {
			String[] arrNombreFichero = this.urlCotizacion.split("\\.");
	    	
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

    public boolean isAceptado() {
        return aceptado;
    }

    public void setAceptado(boolean aceptado) {
        this.aceptado = aceptado;
    }

    
}