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
import com.human.rarepet.dto.QnaBoardAnswerDto;
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
	public String showWriteForm(BoardDto board,
			@AuthenticationPrincipal RarepetUserDetails userDetails ,
			Model model) {
		
		System.out.println("-------------->write form");
		
		if(userDetails == null)
		{
			return "redirect:/auth/login";		
		}
		
		MemberDto member = userDetails.getMember();
		if(member != null) {
			MemberDto user = memberService.getUserById(member.getId());
			model.addAttribute("user", user);
		}
		
		return "board/write";		
	}	
	
	final String UPLOAD_PATH = "d:\\uploaded_images";
	
	@PostMapping(path = {"/board/write"})
	public String writeBoard(
			@AuthenticationPrincipal RarepetUserDetails userDetails , 
			@RequestParam("picture") MultipartFile picture,
			GenBoardDto board,
			Model model) {
		Date writeDate = new Date();
		
		System.out.printf("-------------->Gen writetitle \n" + board.getBoardTitle());
		
		if(userDetails == null)
		{
			return "redirect:/auth/login";		
		}	
		
		System.out.println(board);
		
		if (picture != null && picture.getOriginalFilename().length() > 0) {
			
			String savedFileName = Util.makeUniqueFileName(picture.getOriginalFilename());
			
			System.out.println("test \n");
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
					board.setBoardLikeCount(0);
					board.setBoardViewCount(0);
					
					// board.getBoardWriteDate();
					boardService.insertBoard(board);
					boardService.insertBoardGen(board);
					
				}   catch (Exception ex) {
					ex.printStackTrace();
					
				}
			}
		
		} else {
			System.out.println("test ERR\n");
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
			@RequestParam(value = "keyword", defaultValue = "") String keyword,
			Model model) {

		System.out.println("--------------------Community page");

		List<BoardDto> boards = boardService.showAllBoard(); // 좋아요순 정렬
		List<GenBoardDto> genboards = boardService.showAllGenBoard();

		int boardCount = genboards.size();
		int pagerBlock = 1;
		int start = 1;
		int end = 1;

		PagerDto pager = new PagerDto();
		pager.setPageNo(pageNo);
		pager.setPageSize(PAGE_SIZE);
		pager.setPagerSize(PAGER_SIZE);
		pager.setDataCnt(boardCount);
		pager.setPageStart(start);
		pager.setPageEnd(end);
		int pageCnt = (boardCount / PAGE_SIZE) + ((boardCount % PAGE_SIZE) > 0 ? 1 : 0);
		pager.setPageCnt(pageCnt);

		model.addAttribute("pager", pager);
		model.addAttribute("pageNo", pageNo);

		// 핵심: boards 순서(좋아요순)를 유지하면서 general 게시판 글만 추출
		List<GenBoardDto> orderedGenBoards = new java.util.ArrayList<>();

		for (BoardDto board : boards) {
			for (GenBoardDto genboard : genboards) {
				if (board.getBoardId() == genboard.getBoardId()) {
					genboard.setBoardTitle(board.getBoardTitle());
					genboard.setBoardContent(board.getBoardContent());
					genboard.setBoardWriterId(board.getBoardWriterId());
					genboard.setBoardWriteDate(board.getBoardWriteDate());
					genboard.setBoardViewCount(board.getBoardViewCount());
					genboard.setBoardLikeCount(board.getBoardLikeCount());
					genboard.setImgUrl(board.getImgUrl());
					orderedGenBoards.add(genboard);
					break;
				}
			}
		}
		
		// 검색어가 있으면 제목 / 내용 / 작성자 기준으로 필터링
	    List<GenBoardDto> filteredBoards = new java.util.ArrayList<>();

	    if (keyword != null && !keyword.trim().isEmpty()) {
	        String searchKeyword = keyword.trim().toLowerCase();

	        for (GenBoardDto board : orderedGenBoards) {
	            String title = board.getBoardTitle() != null ? board.getBoardTitle().toLowerCase() : "";
	            String content = board.getBoardContent() != null ? board.getBoardContent().toLowerCase() : "";
	            String writer = board.getBoardWriterId() != null ? board.getBoardWriterId().toLowerCase() : "";

	            if (title.contains(searchKeyword) ||
	                content.contains(searchKeyword) ||
	                writer.contains(searchKeyword)) {
	                filteredBoards.add(board);
	            }
	        }
	    } else {
	        filteredBoards = orderedGenBoards;
	    }

		// model.addAttribute("boards", orderedGenBoards);
		model.addAttribute("boards", filteredBoards);

		return "board/community";
	}

	@PostMapping(path = {"/board/like"})
	public String likeBoard(
			@AuthenticationPrincipal RarepetUserDetails userDetails,
			@RequestParam(value = "boardId", defaultValue = "-1") int boardId,
			@RequestParam(value = "boardType", defaultValue = "community") String boardType
			) {

		if (userDetails == null) {
			return "redirect:/auth/login";
		}

		if (boardId == -1) {
			return "redirect:/board/community";
		}

		boardService.increaseBoardLikeCount(boardId);
		
		if ("qna".equals(boardType)) {
	        return "redirect:/board/qna?boardId=" + boardId;
	    }

	    if ("adoption".equals(boardType)) {
	        return "redirect:/board/adoption?boardId=" + boardId;
	    }

		return "redirect:/board/detail?boardId=" + boardId;
	}
		
	// detail delete
	@GetMapping(path = { "/board/delete" })
	public String delete(
			@AuthenticationPrincipal RarepetUserDetails userDetails,
			@RequestParam(value = "boardId", defaultValue="-1") int boardId
			) {
		
		if(userDetails == null)
		{
			return "redirect:/auth/login";		
		}
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
	
	
	@GetMapping(path = {"/board/qnalist"})
	public String boardqnalist(Model model) {

	    System.out.println("--------------------QnAList");

	    List<BoardDto> boards = boardService.showAllBoard();      // 좋아요순 정렬
	    List<QnaBoardDto> qnaboards = boardService.showAllQnaBoard();

	    List<QnaBoardDto> orderedQnaBoards = new java.util.ArrayList<>();

	    for (BoardDto board : boards) {
	        for (QnaBoardDto qnaboard : qnaboards) {
	            if (board.getBoardId() == qnaboard.getBoardId()) {
	            	
	            	System.out.println(qnaboard);
	                qnaboard.setBoardTitle(board.getBoardTitle());
	                qnaboard.setBoardContent(board.getBoardContent());
	                qnaboard.setBoardWriterId(board.getBoardWriterId());
	                qnaboard.setBoardWriteDate(board.getBoardWriteDate());
	                qnaboard.setBoardViewCount(board.getBoardViewCount());
	                qnaboard.setBoardLikeCount(board.getBoardLikeCount());
	                qnaboard.setAnswerSelected(qnaboard.getAnswerSelected());
	                qnaboard.setImgUrl(board.getImgUrl());
	                orderedQnaBoards.add(qnaboard);
	                break;
	            }
	        }
	    }

	    model.addAttribute("qnaboards", orderedQnaBoards);

	    return "board/qnalist";
	}		
	
	@GetMapping(path = {"/board/qwrite"})
	public String showQWriteForm(QnaBoardDto board,
			@AuthenticationPrincipal RarepetUserDetails userDetails ,
			Model model) {
		
		System.out.println("-------------->qwrite form");
		
		if(userDetails == null)
		{
			return "redirect:/auth/login";		
		}
		
		MemberDto member = userDetails.getMember();
		if(member != null) {
			MemberDto user = memberService.getUserById(member.getId());
			model.addAttribute("user", user);
		}
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
					board.setBoardLikeCount(0);
					board.setBoardViewCount(0);
					
					boardService.insertBoard(board);
					
					board.setAnswerSelected("0");
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
			@AuthenticationPrincipal RarepetUserDetails userDetails ,
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
		
		if(userDetails != null)
		{
			MemberDto member = userDetails.getMember();
			if(member != null) {
				MemberDto user = memberService.getUserById(member.getId());
				model.addAttribute("user", user);
			}					
		}		
		
		
		// ★ 조회수 증가
	    boardService.increaseBoardViewCount(boardId);
	    
	    QnaBoardAnswerDto qnaAnswer = boardService.showQnaAnswerBoardById(boardId);
	    System.out.println(qnaAnswer);
		System.out.println("QnA Ok");
		
		model.addAttribute("board", board);
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("qnaAnswer", qnaAnswer);
			
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
	
	@PostMapping(path = {"/board/qnaapply"})
	public String qnaApply(
			@AuthenticationPrincipal RarepetUserDetails userDetails , 
			@RequestParam(value = "boardId", defaultValue = "-1") int boardId,
			@RequestParam(value = "qnaMessage", defaultValue = "") String qnaMessage
			) {
		Date writeDate = new Date();
		
		System.out.println(qnaMessage);
		
		if(userDetails == null) 
		{
			return "redirect:/auth/login";		
		}		
		
		System.out.printf("qnaApply boardId\n" + boardId);
		System.out.printf("qnaApply message\n" + boardId);
		
		BoardDto orgboard = boardService.showBoardById(boardId);
		MemberDto member = userDetails.getMember();
		// BoardAdoptionDto adoptapply = new BoardAdoptionDto();
		
		QnaBoardAnswerDto qnaapply = new QnaBoardAnswerDto();
		if(member != null) {
			String id = member.getId();
			System.out.printf("====================write Adoption ID\n" + id);
		
			qnaapply.setContent(qnaMessage);
			qnaapply.setBoardId(boardId);
			qnaapply.setId(id);
			
			System.out.println(qnaapply);
		
			boardService.insertQnaApplyBoard(qnaapply);
			//boardService.insertAdoApplyBoard(adoptapply);
			//boardService.insertBoardAdo(board);					
				
		}
		
		return "redirect:/board/qnalist";		
	}
	// qnaapply
	
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

	    System.out.println("--------------------Adoptionlist-----");

	    List<BoardDto> boards = boardService.showAllBoard();   // 좋아요순 정렬
	    List<AdoBoardDto> adoboards = boardService.showAllAdoBoard();

	    List<AdoBoardDto> orderedAdoBoards = new java.util.ArrayList<>();

	    for (BoardDto board : boards) {
	        for (AdoBoardDto adoboard : adoboards) {
	            if (board.getBoardId() == adoboard.getBoardId()) {
	                adoboard.setBoardTitle(board.getBoardTitle());
	                adoboard.setBoardContent(board.getBoardContent());
	                adoboard.setBoardWriterId(board.getBoardWriterId());
	                adoboard.setBoardWriteDate(board.getBoardWriteDate());
	                adoboard.setBoardViewCount(board.getBoardViewCount());
	                adoboard.setBoardLikeCount(board.getBoardLikeCount());
	                adoboard.setImgUrl(board.getImgUrl());
	                orderedAdoBoards.add(adoboard);
	                break;
	            }
	        }
	    }

	    model.addAttribute("adoboards", orderedAdoBoards);

	    return "board/adoptionlist";
	}
	
	@GetMapping(path = {"/board/adoptionwrite"})
	public String showAWriteForm(BoardDto board,
			@AuthenticationPrincipal RarepetUserDetails userDetails ,
			Model model) {
		
		System.out.println("-------------->awrite form");
		
		if(userDetails == null)
		{
			return "redirect:/auth/login";		
		}
		
		MemberDto member = userDetails.getMember();
		if(member != null) {
			MemberDto user = memberService.getUserById(member.getId());
			model.addAttribute("user", user);
		}
		
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
					board.setBoardLikeCount(0);
					board.setBoardViewCount(0);					
					
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
		
		// ★ 조회수 증가
	    boardService.increaseBoardViewCount(boardId);
		
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
			@AuthenticationPrincipal RarepetUserDetails userDetails,
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
		
		if(userDetails != null)
		{
			MemberDto member = userDetails.getMember();
			if(member != null) {
				MemberDto user = memberService.getUserById(member.getId());
				model.addAttribute("user", user);
			}					
		}		
		
		// ★ 조회수 증가
	    boardService.increaseBoardViewCount(boardId);
	    
		//board.setBoardLikeCount(2);
		
		model.addAttribute("board", board);
		model.addAttribute("pageNo", pageNo);
		
		return "board/detail"; 
		
	}

}
