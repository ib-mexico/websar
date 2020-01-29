package com.ibmexico.repositories;

import java.io.Serializable;
import java.util.List;

import com.ibmexico.entities.OpnNegocioColaboradorEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("opnNegocioColaboradorRepository")
public interface IOpnNegocioColaboradorRepository extends JpaRepository<OpnNegocioColaboradorEntity, Serializable>{

    public abstract OpnNegocioColaboradorEntity findByOpnNegocio_IdOportunidadNegocio(int idOpnNegocio);

    @Query("SELECT OpnColaborador FROM OpnNegocioColaboradorEntity OpnColaborador WHERE OpnColaborador.opnNegocio.idOportunidadNegocio = ?1")
    public abstract List<OpnNegocioColaboradorEntity> lstColaboradorOPN(int idOpn);

    @Query("SELECT Opnc FROM OpnNegocioColaboradorEntity Opnc WHERE Opnc.usuarioColaborador.idUsuario = ?1 AND Opnc.opnNegocio.oportunidadNegocioEstatus.idOportunidadNegocioEstatus = ?2  AND Opnc.opnNegocio.empresa.idEmpresa = ?3 AND Opnc.opnNegocio.eliminado = 0 ORDER By Opnc.opnNegocio.creacionFecha DESC")
    public abstract List<OpnNegocioColaboradorEntity> lstOpnUsuario(int idColaborador, int idOpnEstatus, int idEmpresa);

    @Query("SELECT Opnc FROM OpnNegocioColaboradorEntity Opnc WHERE Opnc.usuarioColaborador.idUsuario = ?1 AND Opnc.opnNegocio.oportunidadNegocioEstatus.idOportunidadNegocioEstatus = ?2  AND Opnc.opnNegocio.empresa.idEmpresa = ?3 AND  CONVERT(Opnc.opnNegocio.creacionFecha, DATE) like %?4% AND Opnc.opnNegocio.eliminado = 0 ORDER By Opnc.opnNegocio.creacionFecha DESC")
    public abstract List<OpnNegocioColaboradorEntity> lstOpnUsuarioAnio(int idColaborador, int idOpnEstatus, int idEmpresa, int anio);

    @Query("SELECT Opnc FROM OpnNegocioColaboradorEntity Opnc WHERE Opnc.usuarioColaborador.idUsuario = ?1 AND Opnc.opnNegocio.oportunidadNegocioEstatus.idOportunidadNegocioEstatus = ?2  AND Opnc.opnNegocio.empresa.idEmpresa = ?3 AND  CONVERT(Opnc.opnNegocio.creacionFecha, DATE) not like %?4% AND Opnc.opnNegocio.eliminado = 0 ORDER By Opnc.opnNegocio.creacionFecha DESC")
    public abstract List<OpnNegocioColaboradorEntity> lstOpnUsuarioHistorico(int idColaborador, int idOpnEstatus, int idEmpresa, int anio);


}