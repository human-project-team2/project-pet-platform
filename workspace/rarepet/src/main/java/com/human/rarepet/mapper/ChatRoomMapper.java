package com.human.rarepet.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.human.rarepet.dto.ChatMessageDto;

@Mapper
public interface ChatRoomMapper {
	
	@Select("SELECT * FROM chat_message "
			+ "WHERE chat_room_id = #{chatRoomId} "
			+ "ORDER BY timestamp ASC "
			+ "LIMIT 100")
	List<ChatMessageDto> getChatMessagesByChatRoomId(String chatRoomId);
	
	@Insert("INSERT INTO chat_message (chat_room_id, sender, content, timestamp) "
            + "VALUES (#{chatRoomId}, #{sender}, #{content}, #{timestamp})")
	void insertChatMessage(ChatMessageDto chatMessage);
	
}
