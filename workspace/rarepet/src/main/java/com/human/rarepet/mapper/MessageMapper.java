package com.human.rarepet.mapper;

import java.util.List;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.human.rarepet.dto.MessageDto;
@Mapper
public interface MessageMapper {

	void sendMessage(MessageDto message);
	
	// partition by 로 보낸, 받은 사람 구분없이 특정 아이디가 들어가는 메시지
	// 중에서 가장 마지막 메시지
	@Select(
			"select *, case when sender_id = #{loggedInId} then 'sent' else 'received' end as direction "
			+ "from "
				+ "(select *, row_number() over (partition by least(sender_id, receiver_id), "
															+ "greatest(sender_id, receiver_id) "
															+ "order by sent_at desc"
											  + ") as rn "
							+ "from message where sender_id = #{loggedInId} or receiver_id = #{loggedInId}"
				+ ") derived_table where rn = 1 order by sent_at desc"
	)
	List<MessageDto> receiveMessage(String loggedInId);
	
	@Select("select *, case "
			+ "when sender_id=#{loggedInId} then 'sent' else 'received' end as direction "
			+ "from message where (receiver_id = #{loggedInId} and sender_id = #{partnerId}) "+
			"or (receiver_id = #{partnerId} and sender_id = #{loggedInId}) "+
			"order by sent_at")
	List<MessageDto> messageDetail(@Param("loggedInId")String loggedInId, @Param("partnerId")String partnerId);

	@Update("update message set is_read = 1 where receiver_id = #{id} and sender_id = #{partnerId} and is_read = 0")
	int markAsRead(@Param("id") String id, @Param("partnerId") String partnerId);
	
}
