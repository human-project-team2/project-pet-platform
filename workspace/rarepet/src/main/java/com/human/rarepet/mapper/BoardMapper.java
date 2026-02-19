package com.human.rarepet.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.human.rarepet.dto.BoardDto;

@Mapper
public interface BoardMapper {

	void insertBoard(BoardDto member);
	
	
}
