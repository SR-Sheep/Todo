package com.example.demo.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
	UserEntity findByEmail(String email); //이메일로 유저 가져오기
	Boolean existsByEmail(String email);//이메일 존재 여부 확인
	UserEntity findByEmailAndPassword(String email, String password); //이메일, 비밀번호로 유저 정보 가져오기
	@Query(value = "SELECT * FROM UserEntity", nativeQuery = true)
	List<UserEntity> findAllUser();
	
}
