package com.ibmexico.entities;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ibmexico.configurations.GeneralConfiguration;

@Entity
@Table(name = "tipo_gasto_cl")
public class TipoGastoClEntity{
    
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idTipoGastoCl;

    @Column(nullable = true)
    private String descripcion;

    @Column(nullable = true)
    private boolean eliminado = false;

    @Column(nullable = true)
    private int orden;

    @Column(nullable = true)
    private LocalDate creacionFecha;

    @Column(nullable = true)
    private LocalDate modificacionFecha;

    public int getIdTipoGastoCl() {
        return idTipoGastoCl;
    }

    public void setIdTipoGastoCl(int idTipoGastoCl) {
        this.idTipoGastoCl = idTipoGastoCl;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public LocalDate getCreacionFecha() {
        return creacionFecha;
    }

    public String getCreacionFechaNatural(){
        return creacionFecha.format(GeneralConfiguration.getInstance().getDateFormatterNatural());        
    }
    
    public void setCreacionFecha(LocalDate creacionFecha) {
        this.creacionFecha = creacionFecha;
    }

    public LocalDate getModificacionFecha() {
        return modificacionFecha;
    }

    public String getModificacionFechaNatural(){
        return modificacionFecha.format(GeneralConfiguration.getInstance().getDateFormatterNatural());        
    }
    
    public void setModificacionFecha(LocalDate modificacionFecha) {
        this.modificacionFecha = modificacionFecha;
    }

}