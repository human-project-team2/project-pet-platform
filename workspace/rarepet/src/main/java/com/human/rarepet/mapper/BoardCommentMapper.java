package com.human.rarepet.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.human.rarepet.dto.BoardCommentDto;

@Mapper
public interface BoardCommentMapper {

	// @Insert("sql") // ~Mapper.xml 파일의 내용을 대체
	@Insert("INSERT INTO comment (id, board_id)	VALUES ( #{ writer }, #{ boardId } )")
	@Options(useGeneratedKeys = true, keyColumn = "comment_id", keyProperty = "commentNo")
		// comment_id 에 자동 증가 넣으라는 모습 
		// db:comment_id, dto:private int commentNo;
	void insertBoardComment(BoardCommentDto commentDto);
		
	/*
	 * 
	
	@Insert("INSERT INTO tbl_boardcomment (board_no, email, content, write_date, "
			+ "modify_date, deleted, group_no, step, depth) " +
			"VALUES (#{ boardNo }, #{ writer }, #{ content }, #{ writeDate }, "
			+ "#{ modifyDate }, #{ deleted }, -1, 1, 0)")
	@Options(useGeneratedKeys = true, keyColumn = "comment_no", keyProperty = "commentNo")
		// comment_no 에 자동 증가 넣으라는 모습 
		// db:comment_no, dto:private int commentNo;
	void insertBoardComment(BoardCommentDto commentDto);
	
	*/
	
	@Update("UPDATE comment " +
			"SET group_No = #{ groupNo } " +
			"WHERE comment_id = #{ commentNo }")
	void updateGroupNo(@Param("commentNo")int commentNo, @Param("groupNo")int groupNo);
	
	@Select("SELECT comment_id, id, board_id " + 
			"FROM comment " +
			"WHERE comment_id = #{ commentNo }")
	List<BoardCommentDto> selectCommentByBoardNo(int boardNo);
	
	//alias as 생략가능
	//@Select("SELECT comment_no, board_no, email as writer, content, write_date, modify_date, group_no, step, depth " + 
			//"FROM tbl_boardcomment " +
			//"WHERE comment_no = #{ commentNo }")
	
	@Select("SELECT comment_id, id, board_id " + 
			"FROM comment " +
			"WHERE comment_id = #{ commentNo }")
	BoardCommentDto selectCommentByCommentNo(int commentNo);

	@Update("UPDATE comment " +
			"SET deleted = TRUE " +
			"WHERE comment_id = #{ commentNo }")
	void updateStepNo(@Param("groupNo") int groupNo, @Param("step") int step);

	@Insert("INSERT INTO comment (comment_id, id, board_id)"
			+ "VALUES (#{ commentNo }, #{ writer }, #{ boardId } }")	
	void insertReComment(BoardCommentDto commentDto);
	
	@Update("UPDATE comment " +
			"SET deleted = TRUE " +
			"WHERE comment_id = #{ commentNo }")
	void deleteComment(int commentNo);
	
	@Update("UPDATE comment " +
			"SET content = #{ content } " +
			"WHERE comment_id = #{ commentNo }")
	void updateComment(BoardCommentDto comment);	
	
}
