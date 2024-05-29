package com.adria_alba.gestioMotos.backend.presentation.restcontrollers;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.adria_alba.gestioMotos.backend.business.model.Moto;
import com.adria_alba.gestioMotos.backend.business.services.MotoServices;
import com.adria_alba.gestioMotos.backend.presentation.config.PresentationException;
import com.adria_alba.gestioMotos.backend.presentation.config.RespuestaError;

@RestController
@RequestMapping("/motos")
public class MotoController {

	@Autowired
	private MotoServices motoServices;
		
	@GetMapping
	public List<Moto> getAll(@RequestParam(name = "min", required = false) Double min, 
								 @RequestParam(name = "max", required = false) Double max){
		
		List<Moto> motos = null;
		
		if(min != null && max != null) {
			motos = motoServices.getBetweenPriceRange(min, max);
		} else {
			motos = motoServices.getAll();
		}
			
		return motos;
	}
		
	@GetMapping("/{id}")
	public Moto read(@PathVariable Long id) {
		
		Optional<Moto> optional = motoServices.read(id);
		
		if(!optional.isPresent()) {
			throw new PresentationException("No se encuentra la moto con id " + id, HttpStatus.NOT_FOUND);
		}
		
		return optional.get();
	}
	
	@PostMapping
	public ResponseEntity<?> create(@RequestBody Moto moto, UriComponentsBuilder ucb) {
		
		Long codigo = null;
		
		try {
			codigo = motoServices.create(moto);
		} catch(IllegalStateException e) {
			throw new PresentationException(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		URI uri = ucb.path("/motos/{codigo}").build(codigo);
		
		return ResponseEntity.created(uri).build();
	}
		
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		
		try {
			motoServices.delete(id);
		} catch(IllegalStateException e) {
			throw new PresentationException("No se encuentra la moto con id [" + id + "]. No se ha podido eliminar.", HttpStatus.NOT_FOUND);
		}
		
	}
	
	@PutMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void update(@RequestBody Moto moto) {
		
		try {
			motoServices.update(moto);
		} catch(IllegalStateException e) {
			throw new PresentationException(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		
	}
}
