package com.human.rarepet.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class NotificationServiceImplement implements NotificationService{
	
	//CopyOnWriteArrayList: 배열을 변경할 때, 변경되기 전의 배열을 복사.
	//읽기와 쓰기가 동시에 진행될 때, thread safety 에 의해 locking 되는 것을 해결.
	// Thread-safe list to hold all active "pipes" ??
	//private final List<SseEmitter> allEmitters = new CopyOnWriteArrayList<>();
	//다음으로 대체
	//특정 사용자에게 보낼 때. thread safety -> ConcurrentHashMap -> 부분적 locking
	private final Map<String, SseEmitter> userMap = new ConcurrentHashMap<>();
		
	//특정 사용자 연결
	public SseEmitter connect(String id) {
		
		//SseEmitter emitter = new SseEmitter(Long.MAX_Value); // 무한 유지.
		SseEmitter emitter = new SseEmitter(30 * 60 * 1000L); //30분 유지.
		
        //만료되거나 쓰지 못할 emitter 정리. 
        emitter.onCompletion(() -> removeEmitter(id, emitter));
        emitter.onTimeout(() -> removeEmitter(id, emitter));
        emitter.onError((ex) -> removeEmitter(id, emitter));
        
        userMap.put(id, emitter);
        
        //처음 heartbeat 보내기, 처음 연결자체 문제가 생길 수도 있으므로
        try {
        	emitter.send(SseEmitter.event().name("connect").data("연결됨."));
        } catch (IOException ex) {
        	removeEmitter(id, emitter);
        }
        
        return emitter;
    }
	
	private void removeEmitter(String id, SseEmitter emitter) {
		userMap.remove(id, emitter);
	}
	
	//ping을 주기적으로 보내서 연결 유지?....
	//application.properties 에서 30분 유지는 서버 측에서의 설정
	//ping을 보내는 것은 클라이언트 쪽에서 idle connection을 끝내는 경우
	
	@Scheduled(fixedRate = 30000)
	public void heartbeat() {
	    
		userMap.forEach((id, emitter) -> {
	    
			try {
	            emitter.send(SseEmitter.event().name("ping").data("keep-alive"));
	        } catch (IOException e) {
	            removeEmitter(id, emitter);
	        }
			
	    });
	}
	
	@Override
	public void broadcast(String message) { //현재 연결되어 있는 모든 클라이언트에 알림 보내기.
		
		userMap.forEach((id, emitter) -> {
			
			try {
				emitter.send(SseEmitter.event().name("broadcast").data(message));
			} catch(IOException ex) {
				removeEmitter(id, emitter); //오류 발생 시 삭제.
			}
			
		});
	}

	@Override
	public void sendToUser(String id, String message) { //특정 사용자에게 보내는 알림.
		
		SseEmitter emitter = userMap.get(id);
		if(emitter != null) {
			try {
				emitter.send(SseEmitter.event().name("private").data(message));
			} catch (IOException ex) {
				removeEmitter(id, emitter); //오류 발생 시 삭제.
			}
		}
	}
	
	
	
	
	
}
