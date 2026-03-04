package com.human.rarepet.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.human.rarepet.dto.AdoBoardDto;
import com.human.rarepet.dto.BoardDto;
import com.human.rarepet.dto.GenBoardDto;
import com.human.rarepet.service.BoardService;
import com.human.rarepet.service.MarketService;
import com.human.rarepet.dto.PagerDto;
import com.human.rarepet.dto.ProductDto;
import com.human.rarepet.dto.QnaBoardDto;

@Controller
public class BoardController {
	
	private final int PAGE_SIZE = 3; 	// 한 페이지에 표시되는 데이터 개수
	private final int PAGER_SIZE = 3;	// 한 번에 표시할 페이지 번호 개수
	
	@Autowired
	private BoardService boardService;
	
	@Autowired
	private MarketService marketService;
	
	@GetMapping(path = {"/board/write"})
	public String showWriteForm(BoardDto board) {
		
		System.out.println("-------------->write form");
		
		
		return "board/write";		
	}
	
	@PostMapping(path = {"/board/write"})
	public String writeBoard(GenBoardDto board) {
		Date writeDate = new Date();
		
		System.out.printf("-------------->Gen writetitle " + board.getBoardTitle());
		
		board.setBoardWriteDate(writeDate);
		board.setBoardLikeCount(1);
		board.setBoardViewCount(1);
		
		// board.getBoardWriteDate();
		boardService.insertBoard(board);
		boardService.insertBoardGen(board);
		return "redirect:/board/boardlist";		
	}
	
	@GetMapping (path = {"/board/boardlist"}) 
	public String list(Model model) {
		
		System.out.println("--------------------Board");		
			
		return "board/boardlist"; 
		
	}
	
	@GetMapping (path = {"/board/community"}) 
	public String boardlist(
			@RequestParam(value="pageNo", defaultValue="1") int pageNo,
			Model model) {
		int board_exist = 0;
		System.out.println("--------------------Community page");
		/* 
		List<BoardDto> boards = boardService.findBoardByPage(pageNo, PAGE_SIZE);
		int boardCount = boardService.findBoardCount(); // 페이지 갯수를 계산하기 위한 데이터 조회
		
		// 페이저 (페이지 번호 목록) 표시를 위한 데이터 처리
		int pagerBlock = (pageNo - 1) / PAGER_SIZE;
		int start = (pagerBlock * PAGER_SIZE) + 1;
		int end = start + PAGER_SIZE;
		
		PagerDto pager = new PagerDto();
		pager.setPageNo(pageNo);
		pager.setPageSize(PAGE_SIZE);
		pager.setPagerSize(PAGER_SIZE);
		pager.setDataCnt(boardCount);
		pager.setPageStart(start);
		pager.setPageEnd(end);
		//pager.setLinkUrl(LINK_URL);
		int pageCnt = (boardCount / PAGE_SIZE) + ((boardCount % PAGE_SIZE) > 0 ? 1 : 0);
		pager.setPageCnt(pageCnt);
		
		model.addAttribute("boards", boards);
		model.addAttribute("pager", pager);
		model.addAttribute("pageNo", pageNo);
		*/		
		/*
		 * int bono = 15;
		 * 
		 * BoardDto board = boardService.showBoardById(bono);
		 * 
		 * if(board != null) { board_exist = 1;
		 * System.out.printf("-------------->board id " + board.getBoardId()); }
		 */		
		
		List<BoardDto> boards = boardService.showAllBoard();
		List<GenBoardDto> genboards = boardService.showAllGenBoard();
		
		for(GenBoardDto genboard : genboards) {
			if(genboard != null) {
				board_exist = 1;
				
				for(BoardDto board : boards) {
					System.out.println("\n");
					if(board.getBoardId() == genboard.getBoardId())
					{
						
						genboard.setBoardTitle(board.getBoardTitle());
						genboard.setBoardContent(board.getBoardContent());						
						genboard.setBoardWriterId(board.getBoardWriterId());
						genboard.setBoardWriteDate(board.getBoardWriteDate());
						genboard.setBoardViewCount(board.getBoardViewCount());
						
					}
				}				
			}			
		}
		
		if(board_exist == 1) {
			model.addAttribute("boards", genboards);
		} else {
			System.out.println("-------------->genboard null\n");
		}
		
		
		// 여기에 입력 게시판 페이지 관련 from listWithPaging demoweb2 BoardController.java
		return "board/community"; 
		
	}
	
	@GetMapping (path = {"/board/qnalist"}) 
	public String boardqnalist(Model model) {
		
		int board_exist = 0;
		
		System.out.println("--------------------QnAList");
		
		List<BoardDto> boards = boardService.showAllBoard();
		List<QnaBoardDto> qnaboards = boardService.showAllQnaBoard();		
		
		for(QnaBoardDto qnaboard : qnaboards) {
			if(qnaboard != null) {
				board_exist = 1;
				System.out.printf("-------------->qnaboard title \n" + qnaboard.getBoardTitle());
				for(BoardDto board : boards) {
					System.out.println("\n");
					if(board.getBoardId() == qnaboard.getBoardId())
					{
						
						qnaboard.setBoardTitle(board.getBoardTitle());
						qnaboard.setBoardContent(board.getBoardContent());						
						qnaboard.setBoardWriterId(board.getBoardWriterId());
						qnaboard.setBoardWriteDate(board.getBoardWriteDate());
						qnaboard.setBoardViewCount(board.getBoardViewCount());
						
					}
				}				
			}			
		}
		if(board_exist == 1) {
			model.addAttribute("qnaboards", qnaboards);
		} else {
			System.out.println("-------------->qnaboard null\n");
		}
			
		return "board/qnalist"; 
		
	}		
	
	@GetMapping(path = {"/board/qwrite"})
	public String showQWriteForm(QnaBoardDto board) {
		//public String showQWriteForm(BoardDto board) {
		
		System.out.println("-------------->write QnAform");
		
		
		return "board/qwrite";		
	}
	
	@PostMapping(path = {"/board/qwrite"})
	public String writeQBoard(QnaBoardDto board) {
		Date writeDate = new Date();
				
		board.setBoardWriteDate(writeDate);
		board.setBoardLikeCount(2);
		board.setBoardViewCount(2);
		
		boardService.insertBoard(board);
		
		board.setAnswerSelected("1");
		board.setBoardQuestionCategory(true);
		boardService.insertBoardQnA(board);		
		return "redirect:/board/boardlist";		
	}
	
	@GetMapping (path = {"/board/qna"}) 
	public String boardqna(
			@RequestParam(value = "boardId", defaultValue = "-1") int boardId, 
			@RequestParam(value = "pageNo", defaultValue = "-1") int pageNo,
			Model model) {	
				
		System.out.printf("--------------------Qna--  " + boardId);	
			
		/*
		 * if (boardId == -1 || pageNo == -1) { return "redirect:/board/boardlist"; }
		 */			
		
		BoardDto board = boardService.showQnaBoardById(boardId);		
		if (board == null) {
			System.out.println("--------------------Qna NULL--  ");	
			return "redirect:/board/qnalist";
		}
		System.out.println("QnA Ok");
		
		model.addAttribute("board", board);
		model.addAttribute("pageNo", pageNo);
			
		return "board/qna"; 
		
	}		
	
	@GetMapping (path = {"/board/adoptionlist"}) 
	public String boardadoptionlist(Model model) {

		int board_exist = 0;
		System.out.println("--------------------Adoptionlist-----");
		
		List<BoardDto> boards = boardService.showAllBoard();
		List<AdoBoardDto> adoboards = boardService.showAllAdoBoard();		
		
		for(AdoBoardDto adoboard : adoboards) {
			if(adoboard != null) {
				board_exist = 1;
				System.out.println(adoboard);
				System.out.println("\n");
				
				for(BoardDto board : boards) {
					
					if(board.getBoardId() == adoboard.getBoardId())
					{
										
						adoboard.setBoardTitle(board.getBoardTitle());
						adoboard.setBoardContent(board.getBoardContent());						
						adoboard.setBoardWriterId(board.getBoardWriterId());
						adoboard.setBoardWriteDate(board.getBoardWriteDate());
						adoboard.setBoardViewCount(board.getBoardViewCount());
						
						System.out.println(adoboard);
						System.out.println("\n");
					}
				}				
			}			
		}
		if(board_exist == 1) {
			model.addAttribute("adoboards", adoboards);
		} else {
			System.out.println("-------------->adoboard null\n");
		}
			
		return "board/adoptionlist"; 
		
	}
	
	@GetMapping(path = {"/board/adoptionwrite"})
	public String showAWriteForm(BoardDto board) {
		
		System.out.println("-------------->write Adoptionform");
		
		
		return "board/adoptionwrite";		
	}
	
	@PostMapping(path = {"/board/adoptionwrite"})
	public String writeABoard(AdoBoardDto board) {
		Date writeDate = new Date();
		
		board.setBoardWriteDate(writeDate);
		board.setBoardLikeCount(1);
		board.setBoardViewCount(1);
		board.setBoardAdoptCategory(true);
		board.setBoardAdoptDone(1);
		
//		git diff HEAD~2 HEAD 
		
		boardService.insertBoard(board);
		boardService.insertBoardAdo(board);
		return "redirect:/board/boardlist";		
	}
	
	@GetMapping (path = {"/board/adoption"}) 
	public String boardadoption(
			@RequestParam(value = "boardId", defaultValue = "-1") int boardId, 
			@RequestParam(value = "pageNo", defaultValue = "-1") int pageNo,
			Model model) {
		
		System.out.println("--------------------Adoption");
		
		/*
		 * if (boardId == -1 || pageNo == -1) { return "redirect:/board/adoptionlist"; }
		 */			
		
		BoardDto board = boardService.showAdoBoardById(boardId);		
		if (board == null) {
			System.out.println("--------------------Adoption NULL--  ");	
			return "redirect:/board/adoptionlist";
		}
		System.out.println("Adoption Ok");
		
		model.addAttribute("board", board);
		model.addAttribute("pageNo", pageNo);
			
		return "board/adoption"; 
		
	}
	
	@GetMapping (path = {"/board/detail"}) 
	public String boarddetail(
			@RequestParam(value = "boardId", defaultValue = "-1") int boardId, 
			@RequestParam(value = "pageNo", defaultValue = "-1") int pageNo,
			Model model) {
		System.out.println("Gen");
		System.out.printf("--------------------DetailBo--  " + boardId);		
			
		/*
		 * if (boardId == -1 || pageNo == -1) { return "redirect:/board/boardlist"; }
		 */		
		// if(board.getBoardId() == adoboard.getBoardId())
		// BoardDto board = boardService.showBoardById(boardId);
				
		BoardDto board = boardService.showGenBoardById(boardId);
		if (board == null) {
			return "redirect:/board/boardlist";
		}
		
		model.addAttribute("board", board);
		model.addAttribute("pageNo", pageNo);
		
		return "board/detail"; 
		
	}

}
