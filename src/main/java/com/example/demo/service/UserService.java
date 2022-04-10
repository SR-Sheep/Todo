package com.example.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.UserEntity;
import com.example.demo.persistence.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	
	//개인 정보 저장
	public UserEntity create(final UserEntity userEntity) {
		//entity가 비어있거나, 메일이 없으면 에러
		if(userEntity == null || userEntity.getEmail()==null) {
			throw new RuntimeException("Invaild argument");
		}
		//이미 메일이 있으면 에러
		final String email = userEntity.getEmail();
		if(userRepository.existsByEmail(email)) {
			log.warn("Email already exists {}",email);
			throw new RuntimeException("Email already exists");
		}
		//모두 통과시, 저장
		return userRepository.save(userEntity);
	}
	
	public UserEntity getByCredentials(final String email, final String password) {
		//이메일과 암호 일치하는 유저 엔티티 리턴
		return userRepository.findByEmailAndPassword(email, password);
	}
}
