package com.newlecture.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/*")
public class HomeController {

	@RequestMapping("index")
	/*@ResponseBody*/
	public String index(Model model) {
		
		model.addAttribute("model", "Hello Spring 4");
		
		return "index";
		//return "/WEB-INF/views/index.jsp";
	}
}
