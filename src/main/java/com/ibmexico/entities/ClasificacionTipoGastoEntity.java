package com.ibmexico.entities;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "clasificacion_tipogasto")
public class ClasificacionTipoGastoEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idClasificacion;
    
    @Column(nullable = true)
    private String nombre;
    
    @Column(nullable = true)
    private boolean eliminado=false;
    
    @Column(nullable = true)
    private LocalDate creacionFecha;
    
    @Column(nullable = true)
    private LocalDate modificacionFecha;
    
    @ManyToOne
    @JoinColumn(name="id_tipo_gasto")
    private TipoGastoEntity tipoGasto;

	public int getIdClasificacion() {
		return idClasificacion;
	}

	public void setIdClasificacion(int idClasificacion) {
		this.idClasificacion = idClasificacion;
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

	public LocalDate getCreacionFecha() {
		return creacionFecha;
	}

	public void setCreacionFecha(LocalDate creacionFecha) {
		this.creacionFecha = creacionFecha;
	}

	public LocalDate getModificacionFecha() {
		return modificacionFecha;
	}

	public void setModificacionFecha(LocalDate modificacionFecha) {
		this.modificacionFecha = modificacionFecha;
	}

	public TipoGastoEntity getTipoGasto() {
		return tipoGasto;
	}

	public void setTipoGasto(TipoGastoEntity tipoGasto) {
		this.tipoGasto = tipoGasto;
	}

}  