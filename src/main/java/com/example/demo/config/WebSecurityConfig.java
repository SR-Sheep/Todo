package com.example.demo.config;

import org.apache.catalina.filters.CorsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;

import com.example.demo.security.JwtAuthenticationFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//http 시큐리치 빌더
		http.cors() //기본 cors 설정, 이미 WebMvcConfig에서 설정했음
		.and()
		//csrf는 현재 사용 안함으로 disable(),
		//csrf() 는 Cross Site Requset Forgery 로 
		//위조 요청에 대해 보호를 한다.
		//그러나 jwt 방식은 서버에 인증정보를 저장하지 않기에 상관없음으로 disable 시킨다.
		.csrf().disable()
		//basic 인증 disable()
		.httpBasic().disable()
		//세션 기반이 아님을 선언 (jwt 방식)
		.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		// "/" 와 "/auth/**" 경로에 대해 인증 검사 안함
		.authorizeRequests()
			.antMatchers("/","/auth/**").permitAll()
		// 나머지 경로에 대해 인증 검사
		.anyRequest()
			.authenticated();
		//매 요청마다 CorsFilter 실행한 후 jwtAuthenticationFilter 실행 => 안됨
		//순서를 변경, CsrfFilter after로 변경하니 작동
//		http.addFilterAfter(jwtAuthenticationFilter, BasicAuthenticationFilter.class);
		http.addFilterAfter(jwtAuthenticationFilter, CsrfFilter.class);
	}
	
	
}
