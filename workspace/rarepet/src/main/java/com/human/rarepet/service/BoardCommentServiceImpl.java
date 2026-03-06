package com.human.rarepet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.human.rarepet.dto.BoardCommentDto;
import com.human.rarepet.mapper.BoardCommentMapper;

import lombok.Setter;

@Service("boardCommentService")
public class BoardCommentServiceImpl implements BoardCommentService {
	@Setter(onMethod_ = { @Autowired } )
	private BoardCommentMapper boardCommentMapper;	
	@Override
	public void writeComment(BoardCommentDto commentDto) {		
		boardCommentMapper.insertBoardComment(commentDto);
	}
	@Override
	public void updateGroupNo(int commentNo, int groupNo) {		
		boardCommentMapper.updateGroupNo(commentNo, groupNo);
	}
	@Override
	public List<BoardCommentDto> findBoardCommentByBoardNo(int boardNo) {		
		List<BoardCommentDto> comments = boardCommentMapper.selectCommentByBoardNo(boardNo);
		return comments;		
	}
	@Override
	public void writeReComment(BoardCommentDto commentDto) {
		
		/*
		// 1. 부모글 조회 -> 그룹번호(groupno), 그룹내 순서번호(step), 들여쓰기 (depth) 적용
				BoardCommentDto parent = boardCommentMapper.selectCommentByCommentNo(commentDto.getCommentNo());
				commentDto.setBoardNo(parent.getBoardNo());
				commentDto.setGroupNo(parent.getGroupNo());
				commentDto.setStep(parent.getStep() + 1);
				commentDto.setDepth(parent.getDepth() + 1);
				
				// 2. 이미 등록된 글 중에서 삽입될 위치 뒤에 있는 글의 step 조정 (1 증가)
				boardCommentMapper.updateStepNo(parent.getGroupNo(), parent.getStep());
				
				// 3. 글쓰기
				boardCommentMapper.insertReComment(commentDto);
			*/
	}
	
	@Override
	public void deleteComment(int commentNo) {
		
		boardCommentMapper.deleteComment(commentNo);
		
	}
	
	@Override
	public void updateComment(BoardCommentDto comment) {
		
		boardCommentMapper.updateComment(comment);
		
	}
}
