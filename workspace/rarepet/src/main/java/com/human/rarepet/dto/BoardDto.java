package com.human.rarepet.dto;

import java.sql.Date;

//import com.human.rarepet.dto.MemberDto;

import lombok.Data;

@Data
public class BoardDto {

	private int board_id;
	private String board_title;
	private String board_content;	
	private String board_writer_id;
	private int board_like_count;
	private int board_view_count;
	private java.util.Date board_write_date;
	
	//private String email;
	//private MemberDto writer;
}
