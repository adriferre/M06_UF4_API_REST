package com.adria_alba.gestioMotos.backend.business.services.impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adria_alba.gestioMotos.backend.business.model.Moto;
import com.adria_alba.gestioMotos.backend.business.services.MotoServices;
import com.adria_alba.gestioMotos.backend.integration.repositores.MotoRepository;

@Service
public class MotoServicesImpl implements MotoServices{

	@Autowired
	private MotoRepository motoRepository;
	
	@Override
	@Transactional
	public Long create(Moto moto) {
		if(moto.getId() != null) {
			throw new IllegalStateException("No se puede crear una moto con código not null");
		}
		
		Long id = System.currentTimeMillis();
		moto.setId(id);
		
		motoRepository.save(moto);
		
		return id;
	}

	@Override
	public Optional<Moto> read(Long id) {
		return motoRepository.findById(id);
	}

	@Override
	public List<Moto> getAll() {
		return motoRepository.findAll();
	}

	@Override
	@Transactional
	public void update(Moto moto) {
		Long id = moto.getId();
		
		if(id == null) {
			throw new IllegalStateException("No se puede actualizar una moto con código not null");
		}
		
		boolean existe = motoRepository.existsById(id);
		
		if(!existe) {
			throw new IllegalStateException("La moto con código " + id + " no existe. No se puede actualizar.");
		}
		
		motoRepository.save(moto);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		motoRepository.deleteById(id);
	}

	@Override
	public List<Moto> getBetweenPriceRange(double min, double max) {
		return motoRepository.findByPrecioBetweenOrderByPrecioAsc(min, max);
	}

}
