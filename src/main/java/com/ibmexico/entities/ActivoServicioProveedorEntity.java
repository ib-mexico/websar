package com.ibmexico.entities;

import javax.persistence.Entity;
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
    private ActivoProveedorEntity activoProveedor;

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

    public ActivoProveedorEntity getActivoProveedor() {
        return activoProveedor;
    }

    public void setActivoProveedor(ActivoProveedorEntity activoProveedor) {
        this.activoProveedor = activoProveedor;
    }
 

}