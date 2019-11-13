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
@Table(name = "tipo_gasto")
public class TipoGastoEntity{
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idTipoGasto;

    @Column(nullable = true)
    private String nombre;
    
    @Column(nullable = true)
    private boolean eliminado=false;

    @Column(nullable = true)
    private int Orden;

    @Column(nullable = true)
    private LocalDate creacionFecha;

    @Column(nullable = true)
    private LocalDate modificacionFecha;

    public int getIdTipoGasto() {
        return idTipoGasto;
    }

    public void setIdTipoGasto(int idTipoGasto) {
        this.idTipoGasto = idTipoGasto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public String getCreacionFechaNatural(){
        return creacionFecha.format(GeneralConfiguration.getInstance().getDateFormatterNatural());        
    }

	public LocalDate getCreacionFecha() {
		return creacionFecha;
	}

	public void setCreacionFecha(LocalDate creacionFecha) {
		this.creacionFecha = creacionFecha;
    }
    
    public String getModificacionFechaNatural(){
        return modificacionFecha.format(GeneralConfiguration.getInstance().getDateFormatterNatural());        
    }

	public LocalDate getModificacionFecha() {
		return modificacionFecha;
	}

	public void setModificacionFecha(LocalDate modificacionFecha) {
		this.modificacionFecha = modificacionFecha;
	}

    public int getOrden() {
        return Orden;
    }

    public void setOrden(int orden) {
        Orden = orden;
    }

    
}