package com.human.rarepet.controller;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.human.rarepet.common.Util;
import com.human.rarepet.dto.AdoBoardDto;
import com.human.rarepet.dto.BoardAdoptionDto;
import com.human.rarepet.dto.BoardDto;
import com.human.rarepet.dto.GenBoardDto;
import com.human.rarepet.dto.MemberDto;
import com.human.rarepet.service.BoardService;
import com.human.rarepet.service.MarketService;
import com.human.rarepet.service.MemberService;

import jakarta.servlet.http.HttpSession;

import com.human.rarepet.dto.PagerDto;
import com.human.rarepet.dto.ProductDto;
import com.human.rarepet.dto.QnaBoardDto;
import com.human.rarepet.security.RarepetUserDetails;

@Controller
public class BoardController {
	
	private final int PAGE_SIZE = 3; 	// 한 페이지에 표시되는 데이터 개수
	private final int PAGER_SIZE = 3;	// 한 번에 표시할 페이지 번호 개수
	
	@Autowired
	private BoardService boardService;
	
	@Autowired
	private MarketService marketService;
	
	@Autowired
	private MemberService memberService;
	
	@GetMapping(path = {"/board/write"})
	public String showWriteForm(BoardDto board) {
		
		System.out.println("-------------->write form");
		
		
		return "board/write";		
	}	
	
	final String UPLOAD_PATH = "d:\\uploaded_images";
	
	@PostMapping(path = {"/board/write"})
	public String writeBoard(
			@AuthenticationPrincipal RarepetUserDetails userDetails , 
			@RequestParam("picture") MultipartFile picture,
			GenBoardDto board) {
		Date writeDate = new Date();
		
		System.out.printf("-------------->Gen writetitle " + board.getBoardTitle());
		
		if(userDetails == null)
		{
			return "redirect:/auth/login";		
		}		
		
		if (picture != null && picture.getOriginalFilename().length() > 0) {
			
			String savedFileName = Util.makeUniqueFileName(picture.getOriginalFilename());
			
			System.out.println(picture.getOriginalFilename());
			System.out.println(savedFileName);
			
			File file = new File(UPLOAD_PATH, savedFileName);
			String imgUrl = "/images/" + savedFileName;		
		
			MemberDto member = userDetails.getMember();
			if(member != null) {
				String id = member.getId();
				System.out.printf("====================write Community ID\n" + id);
			
				try {
								
					board.setBoardWriteDate(writeDate);
					board.setImgUrl(imgUrl);
					board.setOriginalFileName(picture.getOriginalFilename());
					board.setSavedFileName(savedFileName);
					picture.transferTo(file);
					board.setBoardLikeCount(1);
					board.setBoardViewCount(1);
					
					// board.getBoardWriteDate();
					boardService.insertBoard(board);
					boardService.insertBoardGen(board);
					
				}   catch (Exception ex) {
					ex.printStackTrace();
					
				}
			}
		
		}
		return "redirect:/board/community";		
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
		
		// List<BoardDto> boards = boardService.findBoardByPage(pageNo, PAGE_SIZE);
		List<BoardDto> boards = boardService.showAllBoard();
		// int boardCount = boardService.findBoardCount(); // 페이지 갯수를 계산하기 위한 데이터 조회
		int boardCount = 1;
		int pagerBlock = 1;
		int start = 1;
		int end = 1;
		
		// 페이저 (페이지 번호 목록) 표시를 위한 데이터 처리
		//int pagerBlock = (pageNo - 1) / PAGER_SIZE;
		//int start = (pagerBlock * PAGER_SIZE) + 1;
		//int end = start + PAGER_SIZE;
		
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
		
		//model.addAttribute("boards", boards);
		model.addAttribute("pager", pager);
		model.addAttribute("pageNo", pageNo);
		
		/*
		 * int bono = 15;
		 * 
		 * BoardDto board = boardService.showBoardById(bono);
		 * 
		 * if(board != null) { board_exist = 1;
		 * System.out.printf("-------------->board id " + board.getBoardId()); }
		 */		
		
		// List<BoardDto> boards = boardService.showAllBoard();
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
	
	// detail delete
	@GetMapping(path = { "/board/delete" })
	public String delete(
			@RequestParam(value = "boardId", defaultValue="-1") int boardId
			) {
		
		/*
		if (boardId == -1 || pageNo == -1) {
			return "redirect:/board/list";
		}
*/
		System.out.println(boardId);
		System.out.println("delete GEN \n");
		boardService.deleteBoardGenByBoardId(boardId);
		boardService.deleteBoardByBoardId(boardId);
		System.out.println("delete \n");
		//System.out.println();
		
		return "redirect:community";
		/*
		 * return "redirect:list?pageNo=" + pageNo;
		 */
	}
	
	@GetMapping(path = { "/board/edit" })
	public String showEditForm(	@RequestParam(value = "boardId", defaultValue="-1") int boardId,						 		
						 		Model model) {
		
		if (boardId == -1) {
			return "redirect:/board/boardlist";
		}
		
		BoardDto board = boardService.showBoardById(boardId);
		
		if (board == null) {
			return "redirect:/board/boardlist";
		}
		System.out.println(board);
		
		model.addAttribute("board", board);		
			
		return "board/edit";
	}
	
	@PostMapping(path = { "/board/update" })
	public String doUpdate(
			BoardDto board) {
		
		BoardDto orgboard = boardService.showBoardById(board.getBoardId());
		orgboard.setBoardContent(board.getBoardContent());
		orgboard.setBoardTitle(board.getBoardTitle());
		//System.out.println("update GEN \n");
		System.out.println(orgboard);
		//System.out.println(board);
		
		System.out.println("update\n");
		boardService.updateBoard(orgboard);
					
		return "redirect:/board/detail?boardId=" + board.getBoardId();
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
	public String writeQBoard(
			@AuthenticationPrincipal RarepetUserDetails userDetails , 
			@RequestParam("picture") MultipartFile picture,
			QnaBoardDto board) {
		Date writeDate = new Date();
		
		if(userDetails == null)
		{
			return "redirect:/auth/login";		
		}		
		
		if (picture != null && picture.getOriginalFilename().length() > 0) {
			
			String savedFileName = Util.makeUniqueFileName(picture.getOriginalFilename());
			
			System.out.println(picture.getOriginalFilename());
			System.out.println(savedFileName);
			
			File file = new File(UPLOAD_PATH, savedFileName);
			String imgUrl = "/images/" + savedFileName;		
		
			MemberDto member = userDetails.getMember();
			if(member != null) {
				String id = member.getId();
				System.out.printf("====================write Community ID\n" + id);
			
				try {
								
					board.setBoardWriteDate(writeDate);
					board.setImgUrl(imgUrl);
					board.setOriginalFileName(picture.getOriginalFilename());
					board.setSavedFileName(savedFileName);
					picture.transferTo(file);
					board.setBoardLikeCount(2);
					board.setBoardViewCount(2);
					
					boardService.insertBoard(board);
					
					board.setAnswerSelected("1");
					board.setBoardQuestionCategory(true);
					boardService.insertBoardQnA(board);
					
				}   catch (Exception ex) {
					ex.printStackTrace();
					
				}
			}
		
		}				
	
		return "redirect:/board/qnalist";	
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
		
		BoardDto board = boardService.showBoardById(boardId);
		BoardDto qnaboard = boardService.showQnaBoardById(boardId);		
		if (qnaboard == null) {
			System.out.println("--------------------Qna NULL--  ");	
			return "redirect:/board/qnalist";
		}
		System.out.println("QnA Ok");
		
		model.addAttribute("board", board);
		model.addAttribute("pageNo", pageNo);
			
		return "board/qna"; 
		
	}	
	
	// detail deleteqna
		@GetMapping(path = { "/board/deleteqna" })
		public String deleteqna(
				@RequestParam(value = "boardId", defaultValue="-1") int boardId
				) {
			System.out.println("deleteqna\n");
			/*
			if (boardId == -1 || pageNo == -1) {
				return "redirect:/board/list";
			}
	*/
			System.out.println(boardId);
			System.out.println("delete QnA \n");
			boardService.deleteBoardQnaByBoardId(boardId);
			boardService.deleteBoardByBoardId(boardId);						
			
			return "redirect:qnalist";
			/*
			 * return "redirect:qnalist?pageNo=" + pageNo;
			 */
		}
	
	@GetMapping(path = { "/board/editqna" })
	public String showEditFormQna(	@RequestParam(value = "boardId", defaultValue="-1") int boardId,						 		
						 		Model model) {
		
		if (boardId == -1) {
			return "redirect:/board/boardlist";
		}
		
		BoardDto board = boardService.showBoardById(boardId);
		
		if (board == null) {
			return "redirect:/board/boardlist";
		}
		System.out.println(board);
		
		model.addAttribute("board", board);		
			
		return "board/qnaedit";
	}
	
	@PostMapping(path = { "/board/qnaupdate" })
	public String doUpdateQna(
			BoardDto board) {
		
		BoardDto orgboard = boardService.showBoardById(board.getBoardId());
		orgboard.setBoardContent(board.getBoardContent());
		orgboard.setBoardTitle(board.getBoardTitle());
		//System.out.println("update GEN \n");
		System.out.println(orgboard);
		//System.out.println(board);
		
		System.out.println("update\n");
		boardService.updateBoard(orgboard);
					
		return "redirect:/board/qna?boardId=" + board.getBoardId();
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
	public String writeABoard(
			@AuthenticationPrincipal RarepetUserDetails userDetails , 
			@RequestParam("picture") MultipartFile picture,
			AdoBoardDto board) {
		Date writeDate = new Date();
		
		if(userDetails == null) 
		{
			return "redirect:/auth/login";		
		}		
		
		if (picture != null && picture.getOriginalFilename().length() > 0) {
			
			String savedFileName = Util.makeUniqueFileName(picture.getOriginalFilename());
			
			System.out.println(picture.getOriginalFilename());
			System.out.println(savedFileName);
			
			File file = new File(UPLOAD_PATH, savedFileName);
			String imgUrl = "/images/" + savedFileName;		
		
			MemberDto member = userDetails.getMember();
			if(member != null) {
				String id = member.getId();
				System.out.printf("====================write Adoption ID\n" + id);
			
				try {
								
					board.setBoardWriteDate(writeDate);
					board.setImgUrl(imgUrl);
					board.setOriginalFileName(picture.getOriginalFilename());
					board.setSavedFileName(savedFileName);
					picture.transferTo(file);
					board.setBoardLikeCount(2);
					board.setBoardViewCount(2);					
					
					board.setBoardAdoptCategory(true);
					board.setBoardAdoptDone(1);
					
//					git diff HEAD~2 HEAD 
					
					boardService.insertBoard(board);
					boardService.insertBoardAdo(board);					
					
				}   catch (Exception ex) {
					ex.printStackTrace();
					
				}
			}
		
		}		
		return "redirect:/board/adoptionlist";		
	}
	
	
	@PostMapping(path = {"/board/adoptionapply"})
	public String adoptionApply(
			@AuthenticationPrincipal RarepetUserDetails userDetails , 
			@RequestParam(value = "boardId", defaultValue = "-1") int boardId,
			@RequestParam(value = "adoptMessage", defaultValue = "") String adoptMessage
			) {
		Date writeDate = new Date();
		
		System.out.println(adoptMessage);
		
		if(userDetails == null) 
		{
			return "redirect:/auth/login";		
		}		
		
		System.out.printf("adoptApply boardId\n" + boardId);
		
		BoardDto orgboard = boardService.showBoardById(boardId);
		MemberDto member = userDetails.getMember();
		BoardAdoptionDto adoptapply = new BoardAdoptionDto();
		if(member != null) {
			String id = member.getId();
			System.out.printf("====================write Adoption ID\n" + id);
		
			adoptapply.setAdoptApplyMessage(adoptMessage);
			adoptapply.setBoardId(boardId);
			adoptapply.setId(id);
		
			boardService.insertAdoApplyBoard(adoptapply);
			//boardService.insertBoardAdo(board);					
				
		}
		
		return "redirect:/board/adoptionlist";		
	}
	
	@GetMapping (path = {"/board/adoaprove"}) 
	public String adoptionAproveForm(
			@AuthenticationPrincipal RarepetUserDetails userDetails ,
			@RequestParam(value = "boardId", defaultValue = "-1") int boardId, 
			@RequestParam(value = "pageNo", defaultValue = "-1") int pageNo,
			Model model) {
				
		System.out.println("--------------------Adoption Prove Form");
		
		/*
		 * if (boardId == -1 || pageNo == -1) { return "redirect:/board/adoptionlist"; }
		 */			
		
		BoardDto board = boardService.showBoardById(boardId);
		BoardDto adoboard = boardService.showAdoBoardById(boardId);		
		if (adoboard == null) {
			System.out.println("--------------------Adoption NULL--  ");	
			return "redirect:/board/adoptionlist";
		}
		
		System.out.println(adoboard);
		
		BoardAdoptionDto adoapply = boardService.showAdoApplyBoardById(boardId);	
		System.out.println(adoapply);
		
		if (userDetails != null) {
			MemberDto loginUser = userDetails.getMember();
			MemberDto user = memberService.getUserById(loginUser.getId());
			model.addAttribute("user", user);
		}
		
		model.addAttribute("board", board);
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("adoboard", adoboard);
		model.addAttribute("adoapply", adoapply);	
		System.out.println("Adoption prove Form \n");
			
		return "board/adoption"; 
		
	}
	
	@PostMapping(path = {"/board/adoaprove"})
	public String adoptionAprove(
			@AuthenticationPrincipal RarepetUserDetails userDetails , 
			@RequestParam(value = "boardId", defaultValue = "-1") int boardId
			) {
		Date writeDate = new Date();
		String name = null;
		String writer = null;
		
		if(userDetails == null) 
		{
			return "redirect:/auth/login";		
		}		
		
		System.out.printf("adoptAprove boardId\n" + boardId);
		
		BoardDto board = boardService.showBoardById(boardId);
		AdoBoardDto adoboard = boardService.showAdoBoardById(boardId);	
		
		if (adoboard == null) {
			System.out.println("--------------------Adoption NULL--  ");	
			return "redirect:/board/adoptionlist";
		}
		
		System.out.println("Adoption prove");
		System.out.println(adoboard);
		
		BoardAdoptionDto adoapply = boardService.showAdoApplyBoardById(boardId);	
		System.out.println(adoapply);
		int adoptDone = adoapply.getBoardAdoptDone();
		if (adoptDone == 2)
		{
			System.out.println("분양완료 상태임 \n");
			return "redirect:adoptionlist";
		}
		
		System.out.println("Adoption prove Done 1");
		
		if (userDetails != null) {
			MemberDto loginUser = userDetails.getMember();
			MemberDto user = memberService.getUserById(loginUser.getId());		
			
			name = user.getId();
			writer = board.getBoardWriterId();
			if(!name.equals(writer))
			{
				System.out.println(user.getId());
				System.out.println(board.getBoardWriterId());
				System.out.println("name not match \n");
				
				return "redirect:adoptionlist";
			}
			adoapply.setBoardAdoptDone(2);
			// update api 
			boardService.updateAdoBoardById(adoapply);
			System.out.println("Adoption prove Done 2");
		}
		
		// boardAdoptDone to 2
		
		return "redirect:/board/adoptionlist";		
	}
	
	@GetMapping (path = {"/board/adoption"}) 
	public String boardadoption(
			@AuthenticationPrincipal RarepetUserDetails userDetails ,
			@RequestParam(value = "boardId", defaultValue = "-1") int boardId, 
			@RequestParam(value = "pageNo", defaultValue = "-1") int pageNo,
			Model model) {
				
		System.out.println("--------------------Adoption");
		
		/*
		 * if (boardId == -1 || pageNo == -1) { return "redirect:/board/adoptionlist"; }
		 */			
		
		BoardDto board = boardService.showBoardById(boardId);
		BoardDto adoboard = boardService.showAdoBoardById(boardId);		
		if (adoboard == null) {
			System.out.println("--------------------Adoption NULL--  ");	
			return "redirect:/board/adoptionlist";
		}
		System.out.println("Adoption Ok");
		System.out.println(adoboard);
		
		BoardAdoptionDto adoapply = boardService.showAdoApplyBoardById(boardId);	
		System.out.println(adoapply);
		
		if (userDetails != null) {
			MemberDto loginUser = userDetails.getMember();
			MemberDto user = memberService.getUserById(loginUser.getId());
			model.addAttribute("user", user);
		}
		
		model.addAttribute("board", board);
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("adoboard", adoboard);
		model.addAttribute("adoapply", adoapply);			
			
		return "board/adoption"; 
		
	}
	
	// adoption.html deleteado
	@GetMapping(path = { "/board/deleteado" })
	public String deleteado(
			@AuthenticationPrincipal RarepetUserDetails userDetails ,
			@RequestParam(value = "boardId", defaultValue="-1") int boardId
			) {
		
		/*
		if (boardId == -1 || pageNo == -1) {
			return "redirect:/board/list";
		}
*/
		if (userDetails != null) {
			String name = null;
			String writer = null;
			
			MemberDto loginUser = userDetails.getMember();
			MemberDto user = memberService.getUserById(loginUser.getId());
			BoardDto board = boardService.showBoardById(boardId);
			
			name = user.getId();
			writer = board.getBoardWriterId();
			if(!name.equals(writer))
			{
				System.out.println(user.getId());
				System.out.println(board.getBoardWriterId());
				System.out.println("name not match \n");
				
				return "redirect:adoptionlist";
			}					
			System.out.println(user.getName());						
			System.out.println(board.getBoardWriterId());
		}
		
		System.out.println(boardId);
		System.out.println("delete Ado \n");
		boardService.deleteBoardAdoByBoardId(boardId);
		boardService.deleteBoardByBoardId(boardId);						
		
		return "redirect:adoptionlist";
		/*
		 * return "redirect:adolist?pageNo=" + pageNo;
		 */
	}
	
	@GetMapping(path = { "/board/adoedit" })
	public String showEditFormAdo(	@RequestParam(value = "boardId", defaultValue="-1") int boardId,						 		
						 		Model model) {
		
		if (boardId == -1) {
			return "redirect:/board/boardlist";
		}
		
		BoardDto board = boardService.showBoardById(boardId);
		
		if (board == null) {
			return "redirect:/board/boardlist";
		}
		System.out.println(board);
		
		model.addAttribute("board", board);		
			
		return "board/adoedit";
	}
	
	@PostMapping(path = { "/board/adoupdate" })
	public String doUpdateAdo(
			BoardDto board) {
		
		BoardDto orgboard = boardService.showBoardById(board.getBoardId());
		orgboard.setBoardContent(board.getBoardContent());
		orgboard.setBoardTitle(board.getBoardTitle());
		//System.out.println("update GEN \n");
		System.out.println(orgboard);
		//System.out.println(board);
		
		System.out.println("update\n");
		boardService.updateBoard(orgboard);
					
		return "redirect:/board/adoption?boardId=" + board.getBoardId();
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
			
		BoardDto board = boardService.showBoardById(boardId);
		GenBoardDto genboard = boardService.showGenBoardById(boardId);
		if (genboard == null) {
			return "redirect:/board/boardlist";
		}
		//board.setBoardLikeCount(2);
		
		model.addAttribute("board", board);
		model.addAttribute("pageNo", pageNo);
		
		return "board/detail"; 
		
	}

}
