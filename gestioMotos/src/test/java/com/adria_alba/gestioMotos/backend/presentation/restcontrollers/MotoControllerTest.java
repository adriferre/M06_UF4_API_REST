package com.adria_alba.gestioMotos.backend.presentation.restcontrollers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.adria_alba.gestioMotos.backend.business.model.Familia;
import com.adria_alba.gestioMotos.backend.business.model.Moto;
import com.adria_alba.gestioMotos.backend.business.services.MotoServices;
import com.adria_alba.gestioMotos.backend.presentation.config.RespuestaError;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers=MotoController.class)
public class MotoControllerTest {
	
	@Autowired
	private MockMvc miniPostman;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private MotoServices motoServices;
	
	private Moto moto1;
	private Moto moto2;
	
	@BeforeEach
	void init() {
		initObjects();
	}
	
	@Test
	void pedimos_todas_las_motos() throws Exception {
		
		// Arrange
		
		List<Moto> motos = Arrays.asList(moto1, moto2);
		when(motoServices.getAll()).thenReturn(motos);
		
		// Act
		
		MvcResult respuesta = miniPostman.perform(get("/motos").contentType("application/json"))
											.andExpect(status().isOk())
											.andReturn();
		
		String responseBody = respuesta.getResponse().getContentAsString();
		String respuestaEsperada = objectMapper.writeValueAsString(motos);
		
		// Assert
		
		assertThat(responseBody).isEqualToIgnoringWhitespace(respuestaEsperada);
		
	}
	
	@Test
	void pedimos_todas_las_motos_entre_rango_precios() throws Exception {
				
		List<Moto> motos = Arrays.asList(moto1, moto2);
		when(motoServices.getBetweenPriceRange(1000, 50000)).thenReturn(motos);
			
		MvcResult respuesta = miniPostman.perform(get("/motos").param("min", "1000")
																   .param("max","50000")
																   .contentType("application/json"))
											.andExpect(status().isOk())
											.andReturn();
		
		String responseBody = respuesta.getResponse().getContentAsString();
		String respuestaEsperada = objectMapper.writeValueAsString(motos);
		
		assertThat(responseBody).isEqualToIgnoringWhitespace(respuestaEsperada);
		
	}
	
	@Test
	void obtenemos_moto_a_partir_de_su_id() throws Exception{
		
		when(motoServices.read(100L)).thenReturn(Optional.of(moto1));
		
		MvcResult respuesta = miniPostman.perform(get("/motos/100").contentType("application/json"))
									.andExpect(status().isOk())
									.andReturn();
		
		String responseBody = respuesta.getResponse().getContentAsString();
		String respuestaEsperada = objectMapper.writeValueAsString(moto1);
		
		assertThat(responseBody).isEqualToIgnoringWhitespace(respuestaEsperada);
	
	}
	
	@Test
	void solicitamos_moto_a_partir_de_un_id_inexistente() throws Exception {
		
		when(motoServices.read(100L)).thenReturn(Optional.empty());
		
		MvcResult respuesta = miniPostman.perform(get("/motos/100").contentType("application/json"))
									.andExpect(status().isNotFound())
									.andReturn();
		
		String responseBody = respuesta.getResponse().getContentAsString();
		String respuestaEsperada = objectMapper.writeValueAsString(new RespuestaError("No se encuentra la moto con id 100"));
		
		assertThat(responseBody).isEqualToIgnoringWhitespace(respuestaEsperada);
	}
	
	@Test
	void crea_moto_ok() throws Exception {
		
		moto1.setId(null);
		
		when(motoServices.create(moto1)).thenReturn(1033L);
		
		String requestBody = objectMapper.writeValueAsString(moto1);
		
		miniPostman.perform(post("/productos").content(requestBody).contentType("application/json"))
						.andExpect(status().isCreated())
						.andExpect(header().string("Location","http://localhost/motos/1033"));
	}
	
	@Test
	void crear_moto_con_id_NO_NULL() throws Exception{
		
		when(motoServices.create(moto1)).thenThrow(new IllegalStateException("Problema con el id..."));
		
		String requestBody = objectMapper.writeValueAsString(moto1);
		
		MvcResult respuesta = miniPostman.perform(post("/motos").content(requestBody).contentType("application/json"))
						.andExpect(status().isBadRequest())
						.andReturn();
		
		String responseBody = respuesta.getResponse().getContentAsString();
		String respuestaEsperada = objectMapper.writeValueAsString(new RespuestaError("Problema con el id..."));
		
		assertThat(responseBody).isEqualToIgnoringWhitespace(respuestaEsperada);
	}
	
	@Test
	void eliminamos_moto_ok() throws Exception{
		
		miniPostman.perform(delete("/motos/789")).andExpect(status().isNoContent());
		
		verify(motoServices, times(1)).delete(789L);
	}
	
	@Test
	void eliminamos_moto_no_existente() throws Exception{
		
		Mockito.doThrow(new IllegalStateException("xxxx")).when(motoServices).delete(789L);
		
		MvcResult respuesta = miniPostman.perform(delete("/moto/789"))
								.andExpect(status().isNotFound())
								.andReturn();
		
		String responseBody = respuesta.getResponse().getContentAsString();
		String respuestaEsperada = objectMapper.writeValueAsString(new RespuestaError("No se encuentra la moto con id [789]. No se ha podido eliminar."));
		
		assertThat(responseBody).isEqualToIgnoringWhitespace(respuestaEsperada);
		
	}
	
	// **************************************************************
	//
	// Private Methods
	//
	// **************************************************************
	
	private void initObjects() {
		
		moto1 = new Moto();
		moto1.setId(100L);
		moto1.setNombre("KTM");
		moto1.setDescatalogado(false);
		moto1.setPrecio(5500.0);
		moto1.setFamilia(Familia.NAKED);
		moto1.setModelo("DUKE 125");
		
		moto2 = new Moto();
		moto2.setId(101L);
		moto2.setNombre("Suzuki");
		moto2.setDescatalogado(true);
		moto2.setPrecio(22000.0);
		moto2.setFamilia(Familia.DEPORTIVAS);
		moto2.setModelo("Hayabusa");
		
	}
	
}
