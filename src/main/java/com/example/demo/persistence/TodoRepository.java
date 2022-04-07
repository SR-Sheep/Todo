package com.example.demo.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.TodoEntity;

@Repository  //스프링이 관리
//JpaRepository<TodoEntity, String> 첫번째 변수 : 테이블 맵핑 엔티티 / 두번째 변수 : 엔티티의 기본키 타입
public interface TodoRepository extends JpaRepository<TodoEntity, String> {
	//?1은 메서드의 매개변수의 순서 위치 (첫번째를 뜻함)
	@Query(value = "SELECT * FROM Todo t WHERE t.user = ?1", nativeQuery = true)
	List<TodoEntity> findByUser(String user);
}
