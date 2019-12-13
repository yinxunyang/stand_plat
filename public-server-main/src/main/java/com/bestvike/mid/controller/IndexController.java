package com.bestvike.mid.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class IndexController {
	@RequestMapping("/Blog")
	public String index() {
		return "forward:index.html";
	}
}
