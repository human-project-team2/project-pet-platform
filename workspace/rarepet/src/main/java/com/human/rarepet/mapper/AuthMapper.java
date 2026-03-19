package com.human.rarepet.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AuthMapper {

	@Select("select count(1) from member where email = #{email}")
	int checkDuplicateEmail(String email);
	
}
