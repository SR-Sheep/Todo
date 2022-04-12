package com.example.demo.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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
	//인증
	public UserEntity getByCredentials(final String email, final String password, final PasswordEncoder encoder) {
		final UserEntity originalUser=userRepository.findByEmail(email);
		//matches 메서드 이용하여 salt를 고려하여 패스워드가 같은지 확인
		if(originalUser!=null&&encoder.matches(password, originalUser.getPassword())) {
			return originalUser;
		}
		return null;
	}
	
	//모든 유저 보기
	public List<UserEntity> getAllUserInfo(){
		return userRepository.findAll();
	}
}
