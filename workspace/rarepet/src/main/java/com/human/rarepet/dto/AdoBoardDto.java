package com.human.rarepet.dto;

import lombok.Data;

@Data
public class AdoBoardDto extends BoardDto {

	//private BoardDto board;
    
    private boolean boardAdoptCategory; 
    private int boardAdoptDone; 
    private String answerImgUrl;
	
}
