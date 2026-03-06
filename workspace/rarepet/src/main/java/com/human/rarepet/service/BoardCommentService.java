package com.human.rarepet.service;

import java.util.List;

import com.human.rarepet.dto.BoardCommentDto;

public interface BoardCommentService {

	void writeComment(BoardCommentDto commentDto);

	void updateGroupNo(int commentNo, int commentNo2);

	List<BoardCommentDto> findBoardCommentByBoardNo(int boardNo);

	void writeReComment(BoardCommentDto commentDto);

	void deleteComment(int commentNo);

	void updateComment(BoardCommentDto comment);
}
