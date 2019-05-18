package com.cflima.cursomc.resources;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/auth")
public class AuthResource {

	
	public ResponseEntity<Void> refreshToken(HttpServletResponse response){
		return null;
	}
}
