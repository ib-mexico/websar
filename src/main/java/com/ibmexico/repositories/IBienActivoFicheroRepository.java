package com.ibmexico.repositories;

import java.io.Serializable;
import java.util.List;

import com.ibmexico.entities.BienActivoFicheroEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("bienActivoFicheroRepository")
public interface IBienActivoFicheroRepository extends JpaRepository<BienActivoFicheroEntity, Serializable>{

    public abstract List<BienActivoFicheroEntity> findByBienActivo_IdActivoMobiliario(int idBienActivo);

    @Query("SELECT  objFichero FROM BienActivoFicheroEntity objFichero")
    public abstract List<BienActivoFicheroEntity> lstBienActivoFichero();

    @Query("SELECT objFichero FROM BienActivoFicheroEntity objFichero WHERE objFichero.bienActivo.idActivoMobiliario=?1")
    public abstract List<BienActivoFicheroEntity> findByIdActivo(int idActivoMobiliario);

}
