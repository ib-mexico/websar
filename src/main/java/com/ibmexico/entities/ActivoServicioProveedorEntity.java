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
    private ActivoServicioEntity idServicio;

    @ManyToOne
    @JoinColumn(name="id_proveedor")
    private ActivoProveedorEntity idProveedor;

    @ManyToOne
    @JoinColumn(name = "id_detallemant")
    private BienDetalleMantenimientoEntity detalleMant;

    public int getIdServicioProveedor() {
        return idServicioProveedor;
    }

    public void setIdServicioProveedor(int idServicioProveedor) {
        this.idServicioProveedor = idServicioProveedor;
    }

    public ActivoServicioEntity getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(ActivoServicioEntity idServicio) {
        this.idServicio = idServicio;
    }

    public ActivoProveedorEntity getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(ActivoProveedorEntity idProveedor) {
        this.idProveedor = idProveedor;
    }

    public BienDetalleMantenimientoEntity getDetalleMant() {
        return detalleMant;
    }

    public void setDetalleMant(BienDetalleMantenimientoEntity detalleMant) {
        this.detalleMant = detalleMant;
    }


    

}