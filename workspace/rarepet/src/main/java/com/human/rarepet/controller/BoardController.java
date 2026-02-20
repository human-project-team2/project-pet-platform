package com.human.rarepet.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.human.rarepet.dto.BoardDto;
import com.human.rarepet.service.BoardService;

@Controller
public class BoardController {
	
	@Autowired
	private BoardService boardService;
	
	@GetMapping(path = {"/board/write"})
	public String showWriteForm(BoardDto board) {
		
		System.out.println("-------------->write form");
		
		
		return "board/write";		
	}
	
	@PostMapping(path = {"/board/write"})
	public String writeBoard(BoardDto board) {
		Date writeDate = new Date();
		System.out.println("-------------->writeBoard form");
		System.out.printf("-------------->writetitle " + board.getBoard_title());
		System.out.printf("-------------->writecontent " + board.getBoard_content());
		System.out.printf("-------------->writeid " + board.getBoard_id());
		board.setBoard_write_date(writeDate);
		board.setBoard_like_count(1);
		board.setBoard_view_count(1);
		// board.getBoardWriteDate();
		boardService.insertBoard(board);
		return "redirect:/board/boardlist";		
	}
	
	@GetMapping (path = {"/board/boardlist"}) 
	public String list(Model model) {
		
		System.out.println("--------------------Board");
			
		return "board/boardlist"; 
		
	}
	
	@GetMapping (path = {"/board/community"}) 
	public String boardlist(Model model) {
		
		System.out.println("--------------------Community");
			
		return "board/community"; 
		
	}
	
	@GetMapping (path = {"/board/qnalist"}) 
	public String boardqnalist(Model model) {
		
		System.out.println("--------------------QnAList");
			
		return "board/qnalist"; 
		
	}		
	
	@GetMapping(path = {"/board/qwrite"})
	public String showQWriteForm(BoardDto board) {
		
		System.out.println("-------------->write QnAform");
		
		
		return "board/qwrite";		
	}
	
	@PostMapping(path = {"/board/qwrite"})
	public String writeQBoard(BoardDto board) {
		Date writeDate = new Date();
		System.out.println("-------------->writeBoard form");
		System.out.printf("-------------->writetitle " + board.getBoard_title());
		System.out.printf("-------------->writecontent " + board.getBoard_content());
		System.out.printf("-------------->writeid " + board.getBoard_id());
		board.setBoard_write_date(writeDate);
		board.setBoard_like_count(1);
		board.setBoard_view_count(1);
		
		boardService.insertBoard(board);
		return "redirect:/board/boardlist";		
	}
	
	@GetMapping (path = {"/board/qna"}) 
	public String boardqna(Model model) {
		
		System.out.println("--------------------QnA");
			
		return "board/qna"; 
		
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
