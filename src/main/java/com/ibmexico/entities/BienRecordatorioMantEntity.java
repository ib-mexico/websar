package com.ibmexico.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "bien_recordatorio_mantenimiento")
public class BienRecordatorioMantEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idRecordatorioMantenimiento;
    
    @Column(nullable = false, length =150 )
    private String titulo;

    @Lob
    @Column
    private String descripcion;

    @Lob
    @Column
    private String mensaje;

    @Column
    private boolean eliminado;
    
    @Column(nullable = false)
    private LocalDateTime vencimientoFecha;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro;

    @Column(nullable = false)
    private LocalDateTime fechaModificado;

    @OneToOne
    @JoinColumn(name = "id_detalle_mantenimiento", nullable = false)
    private BienDetalleMantenimientoEntity idDetalleMantenimiento;

}