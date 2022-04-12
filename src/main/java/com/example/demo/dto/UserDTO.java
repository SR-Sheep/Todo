package com.example.demo.dto;

import com.example.demo.model.TodoEntity;
import com.example.demo.model.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
	private String token;
	private String email;
	private String user;
	private String password;
	private String id;
	
	public UserDTO(final UserEntity entity) {
		this.email = entity.getEmail();
		this.user = entity.getUser();
	}
	
}
