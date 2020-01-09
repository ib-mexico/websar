package com.ibmexico.entities;

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
@Entity
@Table(name = "activo_servicio_proveedor")
public class ActivoServicioProveedorEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idServicioProveedor;

    @ManyToOne
    @JoinColumn(name="id_servicio")
    private ActivoServicioEntity activoServicio;

    @ManyToOne
    @JoinColumn(name="id_proveedor")
    private ProveedorEntity activoProveedor;

    //usuario y fecha cuando crean y modifican
    @Column(nullable = true)
    private LocalDateTime creacionFecha;
            
    @Column(nullable = true)
    private LocalDateTime modificacionFecha;

    @Column(nullable = true)
    private Boolean eliminado = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creacion_id_usuario", nullable = true)
    private UsuarioEntity creacionUsuario;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modificacion_id_usuario", nullable = true)
    private UsuarioEntity modificacionUsuario;
    
    // @ManyToOne
    // @JoinColumn(name = "id_detallemant")
    // private BienDetalleMantenimientoEntity detalleMant;

    public int getIdServicioProveedor() {
        return idServicioProveedor;
    }

    public void setIdServicioProveedor(int idServicioProveedor) {
        this.idServicioProveedor = idServicioProveedor;
    }

    public ActivoServicioEntity getActivoServicio() {
        return activoServicio;
    }

    public void setActivoServicio(ActivoServicioEntity activoServicio) {
        this.activoServicio = activoServicio;
    }

    public ProveedorEntity getActivoProveedor() {
        return activoProveedor;
    }

    public void setActivoProveedor(ProveedorEntity activoProveedor) {
        this.activoProveedor = activoProveedor;
    }

    public LocalDateTime getCreacionFecha() {
        return creacionFecha;
    }

    public void setCreacionFecha(LocalDateTime creacionFecha) {
        this.creacionFecha = creacionFecha;
    }

    public LocalDateTime getModificacionFecha() {
        return modificacionFecha;
    }

    public void setModificacionFecha(LocalDateTime modificacionFecha) {
        this.modificacionFecha = modificacionFecha;
    }

    public Boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
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
 

}