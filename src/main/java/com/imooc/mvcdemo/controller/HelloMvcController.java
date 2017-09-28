package com.imooc.mvcdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hello")
public class HelloMvcController {
	
	@RequestMapping("/mvc")
	//这里经过@RequestMapping配置之后可以由浏览器访问localhost:8080/hello/mvc
	public String helloMvc() {
		//返回到home.jsp，进行显示
		return "home";
	}
}
