package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.model.UserEntity;
import com.example.demo.security.TokenProvider;
import com.example.demo.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
public class UserController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private TokenProvider tokenProvider;
	
	//회원 가입
	@PostMapping("/signup")
	public ResponseEntity<?> responseUser(@RequestBody UserDTO userDTO){
		try {
			//요청을 이용해 저장할 사용자 만들기
			
			log.info(userDTO.getPassword());
			
			UserEntity user = UserEntity.builder()
					.email(userDTO.getEmail())
					.user(userDTO.getUser())
					.password(userDTO.getPassword())
					.build();
			
			log.info(userDTO.getPassword());
			
			//서비스를 이용해 리포지터리에 저장
			UserEntity registeredUser = userService.create(user);
			//유저정보 가져오기 (1개)
			UserDTO responsedUser = UserDTO.builder()
					.email(registeredUser.getEmail())
					.id(registeredUser.getId())
					.user(registeredUser.getUser())
					.password(registeredUser.getPassword())
					.build();
			//ok 리턴
			return ResponseEntity.ok().body(responsedUser);
					
		} catch (Exception e) {
			//에러 시 에러 메세지 발생
			ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
//			log.warn("controller : "+e.getMessage());
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}
	//로그인
	@PostMapping("/signin")
	public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO){
		UserEntity user = userService.getByCredentials(userDTO.getEmail(), userDTO.getPassword());
		if(user!=null) {
			//토큰 생성
			final String token = tokenProvider.create(user);
			final UserDTO responseUserDTO = UserDTO.builder()
					.email(user.getEmail())
					.id(user.getId())
					.user(user.getUser())
					.token(token)
					.build();
			return ResponseEntity.ok(responseUserDTO);
		} else {
			ResponseDTO responseDTO = ResponseDTO.builder()
					.error("Login failed.")
					.build();
			return ResponseEntity.badRequest().body(responseDTO);
		}
		
		
	}
	
}
