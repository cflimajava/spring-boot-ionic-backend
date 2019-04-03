package com.cflima.cursomc.serivices;

import org.springframework.mail.SimpleMailMessage;

import com.cflima.cursomc.domain.Pedido;

public interface EmailService {
	
	void sendOrderConfrimationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);

}
