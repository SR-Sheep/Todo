package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.TodoEntity;
import com.example.demo.persistence.TodoRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j //로그 어노테이션
@Service
public class TodoService {
	@Autowired
	private TodoRepository repository;
	
	public String testService() {
		//TodoEntity 생성
		TodoEntity entity = TodoEntity.builder().title("My first todo item").build();
		//TodoEntity 저장
		repository.save(entity);
		//TodoEntity 검색
		TodoEntity savedEntity = repository.findById(entity.getId()).get();
		//제목 출력
		return savedEntity.getTitle();
	}
	
	//Todo 생성
	public List<TodoEntity> create(final TodoEntity entity){
		log.info("Entity userId : {}", entity.getUser());
		log.info("Entity title : {}", entity.getTitle());
		//1) 검증
		validate(entity);
		//2) 저장
		repository.save(entity);
		
		log.info("Entity Id : {} is saved", entity.getId());
		
		//3) 해당 리스트 출력
		return repository.findByUser(entity.getUser());
		
	}
	//다른메서드에서도 계속 쓰이기에 리팩토링
	//엔티티 검증
	private void validate(final TodoEntity entity) {
		if(entity==null) {
			log.warn("Entity cannot be null");
			throw new RuntimeException("Entity cannot be null.");
		}
		if(entity.getUser()==null) {
			log.warn("Unknown user");
			throw new RuntimeException("Unknown user.");
		}
	}
	//user 로 Todo 조회
	public List<TodoEntity> retrieve(final String user) {
		return repository.findByUser(user);
	}
	
	//업데이트(수정)
	public List<TodoEntity> update(final TodoEntity entity){
		//1) 저장할 entity 유효성 검사
		validate(entity);
		
		//2)넘겨받은 entity의 User를 이용하여 TodoEntity를 가져옴
		final Optional<TodoEntity> original = repository.findById(entity.getId());
		
		//오리지날이 존재하면
		original.ifPresent(todo->{
			//3) 반환된 TodoEntity가 존재하면 값을 새 entity 값으로 덮어씌우기
			todo.setTitle(entity.getTitle());
			todo.setDone(entity.isDone());
			
			log.info(entity.getTitle() + " "+ entity.isDone());
			
			//4)데이터 베이스에 새 값 넣기
			repository.save(todo);
			
		});
		//5) 사용자의 모든 리스트 리턴
		return retrieve(entity.getUser());
	}
	//삭제
	public List<TodoEntity> delete(final TodoEntity entity){
		//1) 저장할 entity 유효성 검사
		validate(entity);
		
		try {
			//2) 엔티티 삭제
			repository.delete(entity);
		} catch (Exception e) {
			//3) exception 발생 시 id와 exception을 로깅
			log.error("error deleting entity : "+entity.getId());
			//4) 컨트롤러로 exception을 보냄, DB 내부 로직을 캡슐화 하기 위해 e를 리턴하지 않고 새 exception 오브젝트 리턴
			throw new RuntimeException("error deleting entity : "+entity.getId());
		}
		
		//5) 새 Todo 리스트를 가져와 리턴
		return retrieve(entity.getUser());
	}
	
}
