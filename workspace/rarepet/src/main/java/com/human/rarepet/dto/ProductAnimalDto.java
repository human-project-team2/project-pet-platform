package com.human.rarepet.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductAnimalDto extends ProductDto {

	private int animalId;
	private String gender;
	private int animalAge;
	
}
