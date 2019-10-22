package com.ibmexico.repositories;

import java.io.Serializable;
import java.util.List;

import com.ibmexico.entities.BienActivoEntity;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("bienActivoRepository")
public interface IBienActivoRepository extends JpaRepository<BienActivoEntity, Serializable>{


    public abstract BienActivoEntity findByIdRecursoActivo(int idRecursoActivo);

    //listar Activos con la condicion del id del catalogo
    @Query("SELECT objActivo FROM BienActivoEntity objActivo WHERE objActivo.idActivo.idCatalogoActivo=?1 AND objActivo.enMantenimiento!=true AND objActivo.estatus=true ")
    public abstract List<BienActivoEntity> findByIdCatalogoActivo(int idCatalogoActivo);

    //listar Activos con la condicion de idCatalogo, y no definir que esten en manto
    @Query("SELECT objActivo FROM BienActivoEntity objActivo WHERE objActivo.idActivo.idCatalogoActivo=?1 AND objActivo.estatus=true ")
    public abstract List<BienActivoEntity> findByIdCatalogoActivoAll(int idCatalogoActivo);



    // Listar activos con estatus habilitado
    @Query("SELECT objActivo FROM BienActivoEntity objActivo WHERE objActivo.estatus=true AND objActivo.enMantenimiento!=true")
    public abstract List<BienActivoEntity> lstCatalogoActivo();


    //count mediante tipo de catalogo
    @Query("SELECT COUNT(objActivo) FROM BienActivoEntity objActivo WHERE objActivo.idActivo.idCatalogoActivo=?1")
    public abstract int countForCatalogo(int idCatalogo);

    //TABLE
	@Query("SELECT COUNT(objActivo) FROM BienActivoEntity objActivo")	
	public abstract long countForDataTable();
				
	@Query("SELECT objActivo FROM BienActivoEntity objActivo  WHERE objActivo.estatus=true")
    public abstract List<BienActivoEntity> findForDataTable(Pageable page);

    @Query("SELECT COUNT(objActivo) FROM BienActivoEntity objActivo WHERE objActivo.estatus=true AND (objActivo.idActivo.nombre LIKE %?1%  OR objActivo.modelo LIKE %?1%) order by objActivo.creacionFecha DESC")
    public abstract long countForDataTable(String search);

    @Query("SELECT objActivo FROM BienActivoEntity objActivo WHERE objActivo.estatus=true AND (objActivo.idActivo.nombre LIKE %?1%  OR objActivo.modelo LIKE %?1%) order by objActivo.creacionFecha DESC")
    public abstract List<BienActivoEntity> findForDataTable(String search, Pageable page);
    
}