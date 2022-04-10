package com.example.demo.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.example.demo.model.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class TokenProvider {
	private static final String SECRET_KEY ="NMA8JPctFuna59f5";
	
	//토큰 생성
	public String create(UserEntity userEntity) {
		//기한은 지금부터 1일로 설정
		Date expireDate = Date.from(
				Instant.now().plus(1,ChronoUnit.DAYS)
				);
		//JMT Token 생성
		return Jwts.builder()
				//header에 들어갈 알고리즘, 시크릿키
				.signWith(SignatureAlgorithm.HS512, SECRET_KEY)
				//payload의 내용
				.setSubject(userEntity.getId()) //sub
				.setIssuer("demo app") //iss
				.setIssuedAt(new Date()) //iat
				.setExpiration(expireDate) //exp
				.compact();
	}
	//토큰 확인
	public String validDateAndGetUserId(String token) {
		Claims claims = Jwts.parser()
				.setSigningKey(SECRET_KEY) //시크릿 키 세팅
				//Base64로 디코딩 및 파싱 후
				//시크릿 키로 헤더와 페이로드로 새 서명 생성 후 기존 서명과 비교
				//위조 시 예외 처리를 날림
				.parseClaimsJws(token)
				//비위조 시 몸통
				.getBody();
		//가져온 내용 중 subject, 즉 id를 리턴한다.
		return claims.getSubject();
	}
}
