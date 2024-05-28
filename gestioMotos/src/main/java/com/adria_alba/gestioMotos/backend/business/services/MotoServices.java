package com.adria_alba.gestioMotos.backend.business.services;

import java.util.List;
import java.util.Optional;

import com.adria_alba.gestioMotos.backend.business.model.Moto;


public interface MotoServices {

	Long create(Moto moto);	    // C
	Optional<Moto> read(Long id);   // R
	void update(Moto moto);
	void delete(Long id);
	
	List<Moto> getAll();
	List<Moto> getBetweenPriceRange(double min, double max);
}
