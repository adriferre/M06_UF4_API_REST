package com.adria_alba.gestioMotos.backend.business.model.dtos;

import java.io.Serializable;

public class Moto1DTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String name;
	private double precio;
	
	public Moto1DTO(String name, double precio) {
		this.name = name;
		this.precio = precio;
	}

	public String getName() {
		return name;
	}

	public double getPrecio() {
		return precio;
	}

	@Override
	public String toString() {
		return "Moto1DTO [name=" + name + ", precio=" + precio + "]";
	}

}
