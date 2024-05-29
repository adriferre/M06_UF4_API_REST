package com.adria_alba.gestioMotos.backend.integration.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import com.adria_alba.gestioMotos.backend.business.model.Familia;
import com.adria_alba.gestioMotos.backend.business.model.Moto;
import com.adria_alba.gestioMotos.backend.business.model.dtos.Moto1DTO;
import com.adria_alba.gestioMotos.backend.integration.repositores.MotoRepository;

@DataJpaTest
@Sql(scripts= {"/data/h2/schema_test.sql","/data/h2/data_test.sql"})
public class MotoRepositoryTest {

	@Autowired
	private MotoRepository motoRepository;
	
	private Moto moto1;
	private Moto moto2;
	private Moto moto3;
	private Moto moto4;
	
	@BeforeEach
	void init() {
		initObjects();
	}
	
	@Test
	void obtenemos_motos_entre_rango_de_precios_en_orden_ascendente() {
		
		List<Moto> motos = motoRepository.findByPrecioBetweenOrderByPrecioAsc(2000.0, 25000.0);
		
		assertEquals(2, motos.size());
			
		assertTrue(moto4.equals(motos.get(0)));
		assertTrue(moto2.equals(motos.get(1)));
		
	}
	
	@Test
	void obtenermos_motos_descatalogadas_por_familia() {
		
		List<Moto> motos = motoRepository.findDescatalogadosByFamilia(Familia.DEPORTIVAS);
		
		assertEquals(1, motos.size());
		
		assertTrue(moto1.equals(motos.get(0)));
	}
	
	@Test
	void obtenemos_todas_las_Moto1DTO() {
		
		List<Moto1DTO> motos1DTO = motoRepository.getAllMoto1DTO();
		
		for(Moto1DTO moto1DTO: motos1DTO) {
			System.err.println(moto1DTO);
		}
	}
	
	// **************************************************************
	//
	// Private Methods
	//
	// **************************************************************
	
	private void initObjects() {
		
		moto1 = new Moto();
		moto2 = new Moto();
		moto3 = new Moto();
		moto4 = new Moto();
		
		moto1.setId(100L);
		moto2.setId(101L);
		moto3.setId(102L);
		moto4.setId(103L);
		
	}

}