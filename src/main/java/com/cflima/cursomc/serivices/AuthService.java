package com.cflima.cursomc.serivices;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.cflima.cursomc.domain.Cliente;
import com.cflima.cursomc.repositories.ClienteRepository;
import com.cflima.cursomc.serivices.exceptions.ObjectNotFoundException;

@Service
public class AuthService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	private Random random = new Random();
	
	public void sendNewPassword(String email) {
		
		Cliente cliente = clienteRepository.findByEmail(email);
		if(cliente == null) {
			throw new ObjectNotFoundException("Email não encontrado");
		}
		
		String newPass = newPassword();
		cliente.setSenha(pe.encode(newPass));
		
		clienteRepository.save(cliente);
		emailService.sendNewPasswordEmail(cliente, newPass);
	}

	private String newPassword() {
		char[] vet = new char[10];
		for(int i=0; i<10; i++) {
			vet[i] = ramdomChar();
		}
		return new String(vet);
	}

	private char ramdomChar() {
		int opt = random.nextInt(3);
		if(opt == 0) {//gera um digito
			return (char) (random.nextInt(10) + 48);
		}else if(opt == 1) {//gera letra maiuscula
			return (char) (random.nextInt(26) + 65);
		}else {//gera letra minuscula
			return (char) (random.nextInt(26) + 97);
		}
	}

}
