package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.TodoDTO;
import com.example.demo.model.TodoEntity;
import com.example.demo.service.TodoService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("todo")
public class TodoController {
	@Autowired
	private TodoService service;
	
	@GetMapping("/test")
	public ResponseEntity<?> testTodo(){
		String str = service.testService(); //테스트 서비스 사용
		List<String> list = new ArrayList<>();
		list.add(str);
		ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
		return ResponseEntity.ok().body(response);
	}
	//생성 및 출력
	@PostMapping
	public ResponseEntity<?> createTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto){
		try {
			//1) TodoEntity로 변환
			TodoEntity entity = TodoDTO.toEntity(dto);
			
			//2) id를 null로 초기화, 생성 당시 id는 없어야 함
			entity.setId(null);
			
			//3) 사용자 아이디 설정
			entity.setUser(userId);
			
			//4) 서비스를 이용한 Todo Entity 생성
			List<TodoEntity> entities = service.create(entity);
			
			//5) 자바 스트림을 이용하여 리턴된 엔티티 리스트를 todoDTO 리스트로 변환
			List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
			
			//6) 변환된 TodoDTO 리스트를 이용하여 ResponseDTO 초기화
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
			
			//7) ResponseDTO 리턴
			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			
			//8) 예외 경우
			String error = e.getMessage();
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
		}
		
	}
	//user로 등록된 글 검색
	@GetMapping
	public ResponseEntity<?> retrieveTodoList(@AuthenticationPrincipal String userId) {
		//1) 서비스의 메서드 retrieve() 메서드를 이용하여 Todo 리스트를 가져온다
		List<TodoEntity> entities = service.retrieve(userId);
		
		//2) 자바 스트림을 이용하여 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환한다
		List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
		
		//3) 변환된 TodoDTO 리스트를 이용하여 ResponseDTO 초기화
		ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
		
		//4) ResponseDTO 리턴
		return ResponseEntity.ok().body(response);
	}
	//업데이트
	@PutMapping
	public ResponseEntity<?> updateTodo(@AuthenticationPrincipal String userId,@RequestBody TodoDTO dto){
		log.info("업데이트 실행 중");
		//1) dto를 entity 형태로 변환
		TodoEntity entity = TodoDTO.toEntity(dto);
		//2) user 초기화
		entity.setUser(userId);
		
		//3) 서비스를 이용해 entity 업데이트
		List<TodoEntity> entities = service.update(entity);
		
		//4) 자바 스트림을 이용하여 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환한다
		List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
		
		//5) 변환된 TodoDTO 리스트를 이용하여 ResponseDTO 초기화
		ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
		
		//6) ResponseDTO 리턴
		return ResponseEntity.ok().body(response);
	}
	
	//삭제
	@DeleteMapping
	public ResponseEntity<?> deleteTodo(@AuthenticationPrincipal String userId,@RequestBody TodoDTO dto){
		try {
			//1) TodoEntity 변경
			TodoEntity entity = TodoDTO.toEntity(dto);
			
			//2) 임시 사용자 아이디 설정
			entity.setUser(userId);
			
			//3) 서비스를 이용하여 entity 삭제
			List<TodoEntity> entities = service.delete(entity);
			
			//4) 자바 스트림을 이용하여 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환한다
			List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
			
			//5) 변환된 TodoDTO 리스트를 이용하여 ResponseDTO 초기화
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
			
			//6) ResponseDTO 리턴
			return ResponseEntity.ok().body(response);
			
		}catch (Exception e) {
			//7) 예외가 있는 경우 dto 대신 error 메세지 넣어 출력
			String error = e.getMessage();
			ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	
	
}
