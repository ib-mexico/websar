package com.ibmexico.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ibmexico.entities.ResguardoTipoEntity;
import com.ibmexico.repositories.IResguardoTipoRepository;

@Service("resguardoTipoService")
public class ResguardoTipoService {
	
	@Autowired
	@Qualifier("resguardoTipoRepository")
	private IResguardoTipoRepository resguardoTipoRepository;
	
	public List<ResguardoTipoEntity> listAll() {
		return resguardoTipoRepository.findAll();
	}
	
	public ResguardoTipoEntity findByIdResguardoTipo(int idResguardoTipo) {
		return resguardoTipoRepository.findByIdResguardoTipo(idResguardoTipo);
	}

}
