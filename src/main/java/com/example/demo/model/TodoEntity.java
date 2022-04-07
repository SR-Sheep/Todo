package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity //엔티티, 데이터베이스 한 개체임을 명시
@Table(name = "Todo") //테이블 이름, 없으면 엔티티 이름, 없으면 클래스 이름을 데이터베이스와 맵핑
public class TodoEntity {
	@Id //고유키 설정
	@GeneratedValue(generator = "system-uuid")  //id 자동 생성, generator : id 생성 방식 지정
	@GenericGenerator(name = "system-uuid", strategy = "uuid") //id 생성방식, name : 방식 이름, strategy : 방법
	private String id; //오브젝트 아이디
	private String title;
	private String user;
	
	
//	private String userId; //오브젝트 생성한 사용자 아이디
	private boolean done; //완료 여부, 완료한 경우 true
}
