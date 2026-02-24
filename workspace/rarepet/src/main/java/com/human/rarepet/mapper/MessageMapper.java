package com.human.rarepet.mapper;

import java.util.List;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.human.rarepet.dto.MessageDto;
@Mapper
public interface MessageMapper {

	void sendMessage(MessageDto message);
	
	List<MessageDto> receiveMessage(String receiverId);
	
	@Select("select * from message where (receiver_id = #{receiverId} and sender_id = #{senderId}) "+
			"or (receiver_id = #{senderId} and sender_id = #{receiverId}) "+
			"order by sent_at")
	List<MessageDto> messageDetail(@Param("receiverId")String receiverId, @Param("senderId")String senderId);

}
