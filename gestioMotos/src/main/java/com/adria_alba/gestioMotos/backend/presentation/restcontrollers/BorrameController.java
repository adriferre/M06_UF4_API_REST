package com.adria_alba.gestioMotos.backend.presentation.restcontrollers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adria_alba.gestioMotos.backend.integration.repositores.MotoRepository;

@RestController
public class BorrameController {

	@Autowired
	private MotoRepository motoRepository;
	
	@GetMapping("/dto1")
	public Object getMoto1DTO() {
		return motoRepository.getAllMoto1DTO();
	}
}
