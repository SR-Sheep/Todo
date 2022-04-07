package com.example.demo.controller;


import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.TestRequestBodyDTO;


@RestController
@RequestMapping("test") //리소스
public class TestController {
	// /test 와 매핑
	@GetMapping
	public String testController() {
		return "Hello World";
	}
	
	// /test, method가 post 면 매핑
	@PostMapping
	public String postTestController() {
		return "Hello Post World";
	}
	
	// /test/testGetMapping 과 매핑 ( 이하에서는 생략 )
	@GetMapping("/testGetMapping")
	public String testControllerWithPath() {
		return "Hello GetMapping World";
	}
	//PathVariable 이용, /test/1 에서 1을 id로 받아와서 사용
	//required = false : /test/ 도 작동함 (null로 받아옴, 여기서는 int형이므로 에러가 날 것임)
	@GetMapping("/{id}")
	public String testControllerWithPathVariables(@PathVariable(required = false) int id) {
		return "Hello World! ID : "+id;
	}
	
	//RequestParam 이용, /test/testRequestParam?id=1 형태를 받음 
	@GetMapping("/testRequestParam")
	public String testControllerWithRequsetParam(@RequestParam(required = false) Integer id) {
		if(id==null) id=-1;
		return "Hello World! ID : "+id;
	}
	
	//RequestBody 이용, json 형태 입력을 바로 DTO로 받음
	@GetMapping("/testRequestBody")
	public String testControllerRequestBody(@RequestBody TestRequestBodyDTO testRequestBodyDTO ) {
		return "Hello World! ID : "+ testRequestBodyDTO.getId() + " Massage : "+testRequestBodyDTO.getMassage();
	}
	
	//ResponseBody 이용, 리턴을 json 형태로 리턴
	@GetMapping("/testResponseBody")
	public ResponseDTO<String> testControllerResponseBody(){
		List<String> list = new ArrayList<>();
		list.add("Hello World! I'm ResponseDTO");
		list.add("It's Error");
		ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
		return response;
	}
	
	//ResponseEntity 사용, HTTP 응답의 바디 + status 나 header 조작시 사용
	@GetMapping("/testResponseEntity")
	public ResponseEntity<?> testControllerResponseEntity(){
		List<String> list = new ArrayList<String>();
		list.add("Hello World! I'm RespnseEntity. And you got 400!");
		ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
		//http status를 400으로 설정
		return ResponseEntity.badRequest().body(response);
	}
	
}
