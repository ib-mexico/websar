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
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ibmexico.configurations.GeneralConfiguration;

@Table(name = "activo_servicio_proveedor_mantenimiento2")
@Entity
public class ActivoServicioProveedorMant2Entity {
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
    
    @Column(nullable = true)
    private boolean pagado = false;

    /**Para el apartado de pagos */
    @Column(nullable=true)
    private LocalDate fechaPago;

    @Column(nullable = true)
    private String url_comprobante;

    @Column(nullable = true)
    private LocalDateTime creacionFechaPago;

    @Column(nullable = true)
    private String folioCotizacion;

    @Column(nullable = true)
    private Boolean isGasto;
    
    @Column(nullable= true)
    private Boolean isGastoParcial;
    
    @Column(nullable = true)
    private Boolean isEliminado;

    @Column(nullable = true)
    private LocalDateTime modificacionFechaPago;
    /**End pagos */
    
    @ManyToOne
    @JoinColumn(name = "id_activo_servicio_proveedor", nullable = true)
    private ActivoServicioProveedorEntity  activoServicioProveedor;

    @ManyToOne
    @JoinColumn(name = "id_bien_detalle_mantenimiento", nullable = true)
    private BienDetalleMantenimientoEntity bienDetalleMant;


    /*Campos adicionales de gastos generales */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proveedor", nullable = true)
    private ProveedorEntity proveedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_Factura", nullable = true)
    private FacturaEntity facturaGasto;

    /**Data de lugar con google maps */
    @Column(nullable = true)
    private String ciudad;

    @Column(nullable = true)
    private String estado;

    @Column(nullable = true)
    private String pais;

    @Column(nullable = true)
    private String formatted_address;
    //fecha_pago existe

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_tipo_gasto", nullable = true)
    private TipoGastoEntity tipoGasto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_clasificacion_gasto", nullable = true)
    private ClasificacionTipoGastoEntity clasificacionTipoGasto;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="id_empresa", nullable = true)
    private EmpresaEntity empresa;
    

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = true)
    private UsuarioEntity usuario;

    @Column(nullable = true)
    private Boolean perteneceCotizacion=false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_cotizacion", nullable = true)
    private CotizacionEntity cotizacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cotizacion_Fichero", nullable = true)
    private CotizacionFicheroEntity cotizacionFichero;


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

    public String getSubtotalNatural(){
        return "$ "+ GeneralConfiguration.getInstance().getNumberFormat().format(precioServicioProveedor);
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
    
    LocalDate creacionFechaDate;

    public LocalDate creacionFecha(){
        String fecha=this.creacionFecha.format(GeneralConfiguration.getInstance().getDateFormatter());
        String[] arrFecha=fecha.split("-");
        int year = Integer.parseInt(arrFecha[0]);
        int month = Integer.parseInt(arrFecha[1]);
        int day = Integer.parseInt(arrFecha[2]);
        return creacionFechaDate=LocalDate.of(year, month, day);
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

	public boolean isPagado() {
		return pagado;
	}

	public void setPagado(boolean pagado) {
		this.pagado = pagado;
	}


    // formateo de la fecha 
    public String getFechaPagoNatural() {
        String fecha="";
        if(fechaPago!=null){
            fecha=fechaPago.format(GeneralConfiguration.getInstance().getDateFormatterNatural());
        }
        return fecha;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getUrl_comprobante() {
        return url_comprobante;
    }

    public void setUrl_comprobante(String url_comprobante) {
        this.url_comprobante = url_comprobante;
    }


    public String getCreacionFechaPagoNatural() {
        return creacionFecha.format(GeneralConfiguration.getInstance().getDateFormatterNatural());
    }
    public LocalDateTime getCreacionFechaPago() {
        return creacionFechaPago;
    }

    public void setCreacionFechaPago(LocalDateTime creacionFechaPago) {
        this.creacionFechaPago = creacionFechaPago;
    }

    // formateo de la fecha 
    public String getModificacionFechaPagoNatural() {
         return modificacionFecha.format(GeneralConfiguration.getInstance().getDateFormatterNatural());
    }
        
    public LocalDateTime getModificacionFechaPago() {
        return modificacionFechaPago;
    }

    public void setModificacionFechaPago(LocalDateTime modificacionFechaPago) {
        this.modificacionFechaPago = modificacionFechaPago;
    }

    public ProveedorEntity getProveedor() {
        return proveedor;
    }

    public void setProveedor(ProveedorEntity proveedor) {
        this.proveedor = proveedor;
    }

    public TipoGastoEntity getTipoGasto() {
        return tipoGasto;
    }

    public void setTipoGasto(TipoGastoEntity tipoGasto) {
        this.tipoGasto = tipoGasto;
    }

    public ClasificacionTipoGastoEntity getClasificacionTipoGasto() {
        return clasificacionTipoGasto;
    }

    public void setClasificacionTipoGasto(ClasificacionTipoGastoEntity clasificacionTipoGasto) {
        this.clasificacionTipoGasto = clasificacionTipoGasto;
    }

    public UsuarioEntity getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioEntity usuario) {
        this.usuario = usuario;
    }

    public Boolean getPerteneceCotizacion() {
        return perteneceCotizacion;
    }

    public void setPerteneceCotizacion(Boolean perteneceCotizacion) {
        this.perteneceCotizacion = perteneceCotizacion;
    }

    public CotizacionEntity getCotizacion() {
        return cotizacion;
    }

    public void setCotizacion(CotizacionEntity cotizacion) {
        this.cotizacion = cotizacion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public EmpresaEntity getEmpresa() {
        return empresa;
    }

    public void setEmpresa(EmpresaEntity empresa) {
        this.empresa = empresa;
    }

    public FacturaEntity getFacturaGasto() {
        return facturaGasto;
    }

    public void setFacturaGasto(FacturaEntity facturaGasto) {
        this.facturaGasto = facturaGasto;
    }

    public String getFolioCotizacion() {
        return folioCotizacion;
    }

    public void setFolioCotizacion(String folioCotizacion) {
        this.folioCotizacion = folioCotizacion;
    }

    public Boolean getIsGasto() {
        return isGasto;
    }

    public void setIsGasto(Boolean isGasto) {
        this.isGasto = isGasto;
    }

    public Boolean getIsEliminado() {
        return isEliminado;
    }

    public void setIsEliminado(Boolean isEliminado) {
        this.isEliminado = isEliminado;
    }

    public CotizacionFicheroEntity getCotizacionFichero() {
        return cotizacionFichero;
    }

    public void setCotizacionFichero(CotizacionFicheroEntity cotizacionFichero) {
        this.cotizacionFichero = cotizacionFichero;
    }

    public Boolean getIsGastoParcial() {
        return isGastoParcial;
    }

    public void setIsGastoParcial(Boolean isGastoParcial) {
        this.isGastoParcial = isGastoParcial;
    }

    
}