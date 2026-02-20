package com.human.rarepet.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.human.rarepet.dto.MessageDto;
@Mapper
public interface MessageMapper {

	void sendMessage(MessageDto message);
	
	List<MessageDto> receiveMessage(String receiverId);
}
