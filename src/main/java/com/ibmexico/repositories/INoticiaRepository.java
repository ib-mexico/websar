package com.ibmexico.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ibmexico.entities.NoticiaEntity;

@Repository("noticiaRepository")
public interface INoticiaRepository extends JpaRepository<NoticiaEntity, Serializable> {
	
	public abstract NoticiaEntity findByIdNoticia(int idNoticia);
	
	@Query("SELECT objNoticia FROM NoticiaEntity objNoticia WHERE objNoticia.importante = false AND objNoticia.eliminado = false order by objNoticia.creacionFecha DESC")
	public abstract List<NoticiaEntity> listNoticias();
	
	@Query("SELECT objNoticia FROM NoticiaEntity objNoticia WHERE objNoticia.importante = true AND objNoticia.eliminado = false order by objNoticia.creacionFecha DESC")
	public abstract List<NoticiaEntity> listNoticiasImportantes();
	
	
	//TABLE
	@Query("SELECT COUNT(objNoticia) FROM NoticiaEntity objNoticia WHERE (objNoticia.titulo like %?1% OR objNoticia.descripcion like %?1%)")	
	public abstract long countForDataTable(String search);				
				
	@Query("SELECT objNoticia FROM NoticiaEntity objNoticia WHERE (objNoticia.titulo like %?1% OR objNoticia.descripcion like %?1%) order by objNoticia.creacionFecha DESC")
	public abstract List<NoticiaEntity> findForDataTable(String search,Pageable page);

}
