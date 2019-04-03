package com.cflima.cursomc.serivices;

import java.util.Date;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cflima.cursomc.domain.ItemPedido;
import com.cflima.cursomc.domain.PagamentoComBoleto;
import com.cflima.cursomc.domain.Pedido;
import com.cflima.cursomc.domain.enums.EstadoPagamento;
import com.cflima.cursomc.repositories.ItemPedidoRepository;
import com.cflima.cursomc.repositories.PagamentoRepository;
import com.cflima.cursomc.repositories.PedidoRepository;
import com.cflima.cursomc.serivices.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository rep;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private PagamentoService pagamentoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private EmailService emailService;
	

	public Pedido find(Integer id) {
		Optional<Pedido> obj = rep.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado !!! ID: " + id + ", Tipo: " + Pedido.class.getName()));
	}

	@Transactional 
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		if(obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			pagamentoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		obj.setCliente(clienteService.find(obj.getCliente().getId()));
		obj = rep.save(obj);
		pagamentoRepository.save(obj.getPagamento());
		
		for (ItemPedido item : obj.getItens()) {
			item.setDesconto(0.0);
			item.setProduto(produtoService.find(item.getProduto().getId()));
			item.setPreco(item.getProduto().getPreco());
			item.setPedido(obj);
		};
		
		itemPedidoRepository.saveAll(obj.getItens());
		emailService.sendOrderConfrimationEmail(obj);
		
		return obj;
	}
}
