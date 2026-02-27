package com.human.rarepet.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.human.rarepet.dto.AdoBoardDto;
import com.human.rarepet.dto.BoardDto;
import com.human.rarepet.dto.GenBoardDto;
import com.human.rarepet.dto.QnaBoardDto;


@Mapper
public interface BoardMapper {

	void insertBoard(BoardDto member);
	
	void insertBoardGen(GenBoardDto genboard);
	
	void insertBoardQnA(QnaBoardDto qnaboard);
	
	void insertBoardAdo(AdoBoardDto adoboard);

	List<BoardDto> showAllBoard();

	BoardDto showBoardById(int bono);

	List<QnaBoardDto> showAllQnaBoard();

	List<AdoBoardDto> showAllAdoBoard();
}
