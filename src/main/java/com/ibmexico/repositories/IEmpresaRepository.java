package com.ibmexico.repositories;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ibmexico.entities.EmpresaEntity;

@Repository("empresaRepository")
public interface IEmpresaRepository extends JpaRepository<EmpresaEntity, Serializable> {

	public abstract EmpresaEntity findByIdEmpresa(int idEmpresa);
	
	@Query("SELECT objEmpresa FROM EmpresaEntity objEmpresa")
	public abstract List<EmpresaEntity> listEmpresas();
    
    @Query("SELECT COUNT(objEmpresa) FROM EmpresaEntity  objEmpresa WHERE objEmpresa.eliminado = 0")
    public abstract long countForDataTable();
 
    @Query("SELECT COUNT(objEmpresa) FROM EmpresaEntity  objEmpresa WHERE objEmpresa.eliminado = 0  AND (objEmpresa.empresa like %?1% OR objEmpresa.dominio like %?1% OR objEmpresa.clave LIKE %?1%)")
    public abstract long countForDataTable(String search);

    @Query("SELECT COUNT(objEmpresa) FROM EmpresaEntity objEmpresa WHERE objEmpresa.eliminado = 0  AND (objEmpresa.empresa like %?1% OR objEmpresa.dominio like %?1% OR objEmpresa.clave LIKE %?1%) AND CONVERT(objEmpresa.creacionFecha, DATE) BETWEEN ?2 AND ?3")
    public abstract long countForDataTable(String search, LocalDate ldFechaInicio, LocalDate ldFechaFin);
   
 
    @Query("SELECT objEmpresa FROM EmpresaEntity objEmpresa WHERE objEmpresa.eliminado = 0 order by objEmpresa.idEmpresa ASC")
    public abstract List<EmpresaEntity> findForDataTable(Pageable page);

    @Query("SELECT objEmpresa FROM EmpresaEntity objEmpresa WHERE objEmpresa.eliminado = 0  AND (objEmpresa.empresa like %?1% OR objEmpresa.dominio like %?1% OR objEmpresa.clave LIKE %?1%) order by objEmpresa.idEmpresa ASC")
    public abstract List<EmpresaEntity> findForDataTable(String search, Pageable page);

    @Query("SELECT objEmpresa FROM EmpresaEntity objEmpresa WHERE objEmpresa.eliminado = 0  AND  (objEmpresa.empresa like %?1% OR objEmpresa.dominio like %?1% OR objEmpresa.clave LIKE %?1%) AND CONVERT(objEmpresa.creacionFecha, DATE) BETWEEN ?2 AND ?3 order by objEmpresa.idEmpresa ASC")
    public abstract List<EmpresaEntity> findForDataTable(String search, LocalDate ldFechaInicio, LocalDate ldFechaFin, Pageable page);

}
