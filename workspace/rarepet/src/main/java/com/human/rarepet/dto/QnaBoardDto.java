package com.human.rarepet.dto;

import lombok.Data;

@Data
public class QnaBoardDto extends BoardDto {

	//private BoardDto board;
    // 문의 게시판 추가 필드
    private String answerSelected; 
    private boolean boardQuestionCategory; 
    private String answerImgUrl;
	
}
