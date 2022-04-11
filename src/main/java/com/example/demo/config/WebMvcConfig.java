package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration //스프링 빈으로 등록
public class WebMvcConfig implements WebMvcConfigurer{
	//유효시간 설정, 1시간
	private final long MAX_AGE_SECS = 3600;
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**") //모든 경로에 대해
		.allowedOrigins("http://localhost:3000") //origin이 http://localhost:3000인 경우
		//get, post, put, delete, patch, options 메서드 허용
		.allowedMethods("GET","POST","PUT","DELETE","PATCH","OPTIONS")
		//모든 헤더 허용
		.allowedHeaders("*")
		//인증 관련 정보 허용
		.allowCredentials(true)
		//유효시간 설정
		.maxAge(MAX_AGE_SECS);
	}
}
