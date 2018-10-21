package com.nilotpal.api.userapi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Home redirection to swagger api documentation
 */
@RestController
public class SwaggerUIController {
	@RequestMapping(value = "/")
	public String redirectToSwaggerUI() {
		return "redirect:swagger-ui.html";
	}
}
