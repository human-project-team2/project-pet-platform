package com.human.rarepet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.human.rarepet.dto.AdoBoardDto;
import com.human.rarepet.dto.BoardDto;
import com.human.rarepet.dto.GenBoardDto;
import com.human.rarepet.dto.QnaBoardDto;
import com.human.rarepet.mapper.BoardMapper;

@Service("boardService") // 스프링 프레임워크에 클래스를 등록하는 설정 --> new 하지 않고 주입 가능
public class BoardService {
	 
	//@Autowired // 스프링이 관리하는 AuthDao 객체를 주입하세요
	//private BoardDao authDao;
	
	//@Autowired
	//private MemberRepository memberRepository;
	
	@Autowired
	private BoardMapper boardMapper;
	
	
	public void insertBoard(BoardDto board) {		

		// 3. mybatis 사용
		
		boardMapper.insertBoard(board);
	}

	public void insertBoardGen(GenBoardDto board) {
		 
		boardMapper.insertBoardGen(board);		
	}

	public void insertBoardQnA(QnaBoardDto board) {
		
		boardMapper.insertBoardQnA(board);
	}

	public void insertBoardAdo(AdoBoardDto board) {
		
		boardMapper.insertBoardAdo(board);
	}

	public List<BoardDto> showAllBoard() {
		return boardMapper.showAllBoard();
	}

	public BoardDto showBoardById(int bono) {		
		System.out.printf("-----showBoardById" + bono);
		System.out.println("-----showBoardById---------");
		BoardDto dto = boardMapper.showBoardById(bono);		
		return dto;
	}

	public List<QnaBoardDto> showAllQnaBoard() {
		return boardMapper.showAllQnaBoard();		
	}

	public List<AdoBoardDto> showAllAdoBoard() {
		return boardMapper.showAllAdoBoard();
	}

	public BoardDto showGenBoardById(int boardId) {		
		
		BoardDto dto = boardMapper.showGenBoardById(boardId);
		return dto;
	}

	public BoardDto showQnaBoardById(int boardId) {
		
		BoardDto dto = boardMapper.showQnaBoardById(boardId);
		return dto;
	}

}














