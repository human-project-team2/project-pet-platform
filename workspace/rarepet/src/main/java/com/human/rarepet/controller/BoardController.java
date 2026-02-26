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
		System.out.println("-------------->writeBoard form");
		System.out.printf("-------------->writetitle " + board.getBoard_title());
		System.out.printf("-------------->writecontent " + board.getBoard_content());
		System.out.printf("-------------->writeid " + board.getBoard_id());
		System.out.printf("-------------->generalcategory " + board.isBoard_general_category());
		
		board.setBoard_write_date(writeDate);
		board.setBoard_like_count(1);
		board.setBoard_view_count(1);
		
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
		int bono = 1;

		BoardDto board = boardService.showBoardById(bono);
		
		if(board != null) {
			board_exist = 1;				
			System.out.printf("-------------->board id " + board.getBoard_id());
		}
		
		// BoardDto board = boardService.showAllBoard(bono);
		// List<BoardDto> boards = boardService.showAllBoard();		
		
		/*
		for(BoardDto board : boards) {
			if(board != null) {
				board_exist = 1;				
				System.out.printf("-------------->board id " + board.getBoard_id());
			}			
		}
		if(board_exist == 1) {
			model.addAttribute("boards", boards);
		} else {
			System.out.println("-------------->board null\n");
		}
		
		
		List<ProductDto> products = marketService.showAllProduct();
		
		for(ProductDto product : products) {
			if(product != null) {
				board_exist = 1;				
				System.out.printf("-------------->product title " + product.getProductName());
			}			
		}
		if(board_exist == 00) {
			System.out.println("-------------->product null\n");
		}
		*/
		
		
		/*
		 * model.addAttribute("boards", boards);
		 */	
		// 여기에 입력 게시판 페이지 관련 from listWithPaging demoweb2 BoardController.java
		return "board/community"; 
		
	}
	
	@GetMapping (path = {"/board/qnalist"}) 
	public String boardqnalist(Model model) {
		
		System.out.println("--------------------QnAList");
			
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
		System.out.println("-------------->writeBoard form");
		System.out.printf("-------------->writetitle " + board.getBoard_title());
		System.out.printf("-------------->writecontent " + board.getBoard_content());
		System.out.printf("-------------->writeid " + board.getBoard_id());
		System.out.printf("-------------->answer " + board.getAnswer_selected());
		
		board.setBoard_write_date(writeDate);
		board.setBoard_like_count(1);
		board.setBoard_view_count(1);
		
		boardService.insertBoard(board);
		//board.setBoard_id(board.getBoard_id());
		board.setAnswer_selected("1");
		board.setBoard_question_category(true);
		boardService.insertBoardQnA(board);		
		return "redirect:/board/boardlist";		
	}
	
	@GetMapping (path = {"/board/qna"}) 
	public String boardqna(Model model) {
		
		System.out.println("--------------------QnA");
			
		return "board/qna"; 
		
	}		
	
	@GetMapping (path = {"/board/adoptionlist"}) 
	public String boardadoptionlist(Model model) {
		
		System.out.println("--------------------Adoptionlist-----");
			
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
		System.out.println("-------------->writeBoard form");
		System.out.printf("-------------->writetitle " + board.getBoard_title());
		System.out.printf("-------------->writecontent " + board.getBoard_content());
		System.out.printf("-------------->writeid " + board.getBoard_id());
		System.out.printf("-------------->adopt done " + board.getBoard_adopt_done());
		board.setBoard_write_date(writeDate);
		board.setBoard_like_count(1);
		board.setBoard_view_count(1);
		
		boardService.insertBoard(board);
		boardService.insertBoardAdo(board);
		return "redirect:/board/boardlist";		
	}
	
	@GetMapping (path = {"/board/adoption"}) 
	public String boardadoption(Model model) {
		
		System.out.println("--------------------Adoption");
			
		return "board/adoption"; 
		
	}
	
	@GetMapping (path = {"/board/detail"}) 
	public String boarddetail(Model model) {
		
		System.out.println("--------------------Detail");
			
		return "board/detail"; 
		
	}

}
