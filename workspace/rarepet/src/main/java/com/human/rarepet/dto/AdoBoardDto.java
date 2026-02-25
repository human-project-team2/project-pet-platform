package com.human.rarepet.dto;

import lombok.Data;

@Data
public class AdoBoardDto extends BoardDto {

	//private BoardDto board;
    
    private boolean board_adopt_category; 
    private int board_adopt_done; 
	
}
