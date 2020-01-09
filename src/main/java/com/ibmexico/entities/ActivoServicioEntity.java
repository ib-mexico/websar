package com.ibmexico.entities;

import java.math.BigDecimal;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.ibmexico.configurations.GeneralConfiguration;

@Entity
@Table(name = "activo_servicio")
public class ActivoServicioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idServicioActivo;

    @Column
    private String descripcion;

    @Column(nullable = true, precision = 14, scale = 2, columnDefinition = "DECIMAL(12,2)")
    private BigDecimal precioEstimado;
    
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "activoServicio", cascade = CascadeType.ALL)
    private List<ActivoServicioProveedorEntity> ActivoServicioProveedor_Servicio=new ArrayList<ActivoServicioProveedorEntity>();

    @Column(nullable = true)
    private Boolean eliminado = false;

    @ManyToOne
    @JoinColumn(name = "id_tipo_activo", nullable = true)
    private CatalogoActivoEntity tipoActivo;

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


    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecioEstimado() {
        return precioEstimado;
    }
    public String getPrecioEstimadoNatural(){
        return "$ "+ GeneralConfiguration.getInstance().getNumberFormat().format(precioEstimado);
    }

    public void setPrecioEstimado(BigDecimal precioEstimado) {
        this.precioEstimado = precioEstimado;
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
	public String getModificacionFechaNatural() {
		return modificacionFecha.format(GeneralConfiguration.getInstance().getDateFormatterNatural());
	}
    public LocalDateTime getModificacionFecha() {
        return modificacionFecha;
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

    public List<ActivoServicioProveedorEntity> getActivoServicioProveedor_Servicio() {
        return ActivoServicioProveedor_Servicio;
    }

    public void setActivoServicioProveedor_Servicio(
            List<ActivoServicioProveedorEntity> activoServicioProveedor_Servicio) {
        ActivoServicioProveedor_Servicio = activoServicioProveedor_Servicio;
    }

    public CatalogoActivoEntity getTipoActivo() {
        return tipoActivo;
    }

    public void setTipoActivo(CatalogoActivoEntity tipoActivo) {
        this.tipoActivo = tipoActivo;
    }

    public int getIdServicioActivo() {
        return idServicioActivo;
    }

    public void setIdServicioActivo(int idServicioActivo) {
        this.idServicioActivo = idServicioActivo;
    }

    public Boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }

}