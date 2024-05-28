package com.adria_alba.gestioMotos.backend.integration.repositores;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.adria_alba.gestioMotos.backend.business.model.Moto;
import com.adria_alba.gestioMotos.backend.business.model.dtos.Moto1DTO;
import com.adria_alba.gestioMotos.backend.business.model.Familia;

public interface MotoRepository extends JpaRepository<Moto, Long>{
	
	List<Moto> findByPrecioBetweenOrderByPrecioAsc(double min, double max);
	
	@Query("SELECT m FROM Moto m WHERE m.descatalogado = true AND m.familia = :familia")
	List<Moto> findDescatalogadosByFamilia(Familia familia);
	
	@Query("SELECT new com.adria_alba.gestioMotos.backend.business.model.dtos.Moto1DTO"
		   + "(CONCAT(m.nombre, ' - ', "
		   + "        m.familia), "
		   + "        m.precio) "
		   + "FROM Moto m")
	List<Moto1DTO> getAllMoto1DTO();

}
