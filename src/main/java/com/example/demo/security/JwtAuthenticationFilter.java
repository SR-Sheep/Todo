package com.example.demo.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	@Autowired
	private TokenProvider tokenProvider;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			//요청에서 토큰 가져오기
			String token=parseBearerToken(request);
			log.info("Filter is running... token : {}",token);
			//토큰 검사
			if(token!=null&&!token.equalsIgnoreCase("null")) {
				//userId 가져오기, 위조된 경우 예외 처리됨
				String userId = tokenProvider.validDateAndGetUserId(token);
				log.info("Authenticated user ID : "+userId);
				//인증 완료 정보 넣기
				AbstractAuthenticationToken authentication
				= new UsernamePasswordAuthenticationToken(userId, null, AuthorityUtils.NO_AUTHORITIES );
				//인증 완료 정보에 리퀘스트 세팅
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				//SecurityContextHolder에 securityContext 등록
				
				//빈 securityContext 생성
				SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
				//securityContext에 인증정보 넣기
				securityContext.setAuthentication(authentication);
				//SecurityContextHolder에 securityContext 등록
				SecurityContextHolder.setContext(securityContext);
			}
		} catch (Exception e) {
			logger.error("Could not set user authentication in security context",e);
		}
		filterChain.doFilter(request, response);
	}
	//토큰 가져오기
	private String parseBearerToken(HttpServletRequest request) {
		//http 요청의 헤더 부분 중 Authorization 파싱하여 Bearer 토큰을 리턴한다.
		String bearerToken = request.getHeader("Authorization");
		//헤더의 모든 내용 확인
//		Enumeration eHeader =  request.getHeaderNames();
//		while(eHeader.hasMoreElements()) {
//			String name = (String)eHeader.nextElement();
//			String value = request.getHeader(name);
//			log.info("{} : {}",name,value);
//		}
		
//		log.info("BearerToken is "+bearerToken);
		if(StringUtils.hasText(bearerToken)&&bearerToken.startsWith("Bearer ")) {
			//앞 7글자 ("Bearer ") 이후 문자 리턴 (Token 내용만 리턴)
			return bearerToken.substring(7);
		}
		//토큰이 없으면 null 리턴
		return null;
	}
}
