package com.human.rarepet.dto;

import lombok.Data;

@Data
public class GenBoardDto extends BoardDto {

	//private BoardDto board;
    // 일반 게시판 추가 필드
     
    private boolean boardGeneralCategory;
	
}
