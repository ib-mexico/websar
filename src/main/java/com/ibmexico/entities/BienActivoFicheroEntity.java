package com.ibmexico.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "bienActivo_ficheros")
public class BienActivoFicheroEntity{

    @Id
    @GeneratedValue
    private int idBienActivoFichero;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private BienActivoEntity bienActivo;

    @Column
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creacion_id_usuario", nullable = false)
    private UsuarioEntity creacionUsuario;

    @Column
    private LocalDateTime creacionFecha;


    public int getIdBienActivoFichero() {
        return idBienActivoFichero;
    }

    public void setIdBienActivoFichero(int idBienActivoFichero) {
        this.idBienActivoFichero = idBienActivoFichero;
    }

    public BienActivoEntity getBienActivo() {
        return bienActivo;
    }

    public void setBienActivo(BienActivoEntity bienActivo) {
        this.bienActivo = bienActivo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getFicheroNombre(){
        String objRetorno=null;
        if(this.url!=null){
            String[] arrNombreFichero=this.url.split("\\.");
            if(arrNombreFichero.length >= 2){
                objRetorno=this.url.substring(0, this.url.length() - arrNombreFichero[arrNombreFichero.length-1].length());
            }
        }
        return objRetorno;
    }

    public String getFicheroFullName() {
		String objRetorno =  null;
		
		if(getFicheroNombre() != null && getFicheroExtension() != null) {
			objRetorno = getFicheroNombre() + getFicheroExtension();
		}
		
		return objRetorno;
    }
    
    public String getFicheroExtension() {
		String objRetorno =  null;
		
		if(this.url != null) {
			String[] arrNombreFichero = this.url.split("\\.");
	    	
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
    

    public UsuarioEntity getCreacionUsuario() {
        return creacionUsuario;
    }

    public void setCreacionUsuario(UsuarioEntity creacionUsuario) {
        this.creacionUsuario = creacionUsuario;
    }

    public LocalDateTime getCreacionFecha() {
        return creacionFecha;
    }

    public void setCreacionFecha(LocalDateTime creacionFecha) {
        this.creacionFecha = creacionFecha;
    }

    
}