package com.cflima.cursomc.serivices;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cflima.cursomc.domain.Pagamento;
import com.cflima.cursomc.domain.PagamentoComBoleto;
import com.cflima.cursomc.repositories.PagamentoRepository;

@Service
public class PagamentoService {
	
	@Autowired
	private PagamentoRepository rep;

	public void preencherPagamentoComBoleto(PagamentoComBoleto pagto, Date instante) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(instante);
		cal.add(Calendar.DAY_OF_MONTH, 7);
		pagto.setDataVencimento(cal.getTime());
	}

	public Pagamento insert(Pagamento obj) {
		obj.setId(null);
		obj = rep.save(obj);
		return obj;
	}
	

}
