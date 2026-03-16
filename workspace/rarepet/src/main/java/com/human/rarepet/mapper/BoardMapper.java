package com.human.rarepet.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.human.rarepet.dto.AdoBoardDto;
import com.human.rarepet.dto.BoardAdoptionDto;
import com.human.rarepet.dto.BoardDto;
import com.human.rarepet.dto.GenBoardDto;
import com.human.rarepet.dto.QnaBoardAnswerDto;
import com.human.rarepet.dto.QnaBoardDto;

@Mapper
public interface BoardMapper {

	void insertBoard(BoardDto member);
	
	void insertBoardGen(GenBoardDto genboard);
	
	void insertBoardQnA(QnaBoardDto qnaboard);
	
	void insertBoardAdo(AdoBoardDto adoboard);

	List<BoardDto> showAllBoard();

	BoardDto showBoardById(int bono);

	List<GenBoardDto> showAllGenBoard();
	
	List<QnaBoardDto> showAllQnaBoard();

	List<AdoBoardDto> showAllAdoBoard();	

	GenBoardDto showGenBoardById(int boardId);

	BoardDto showQnaBoardById(int boardId);

	AdoBoardDto showAdoBoardById(int boardId);

	void deleteBoard(int boardId);

	void deleteBoardGen(int boardId);

	void deleteBoardQnA(int boardId);

	void deleteBoardAdo(int boardId);

	void updateBoard(BoardDto board);

	void insertBoardAdoApply(BoardAdoptionDto adoptapply);

	BoardAdoptionDto showAdoApplyBoardById(int boardId);

	void updateBoardAdo(AdoBoardDto adoapply);

	void insertBoardQnaApply(QnaBoardAnswerDto qnaapply);
	
	void increaseBoardLikeCount(int boardId);

	void increaseBoardViewCount(int boardId);
	
}
