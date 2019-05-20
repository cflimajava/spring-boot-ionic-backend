package com.cflima.cursomc.serivices;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cflima.cursomc.domain.Cidade;
import com.cflima.cursomc.domain.Cliente;
import com.cflima.cursomc.domain.Endereco;
import com.cflima.cursomc.domain.enums.Perfil;
import com.cflima.cursomc.domain.enums.TipoCliente;
import com.cflima.cursomc.dto.ClienteDTO;
import com.cflima.cursomc.dto.ClienteNewDTO;
import com.cflima.cursomc.repositories.ClienteRepository;
import com.cflima.cursomc.repositories.EnderecoRepository;
import com.cflima.cursomc.security.UserSS;
import com.cflima.cursomc.serivices.exceptions.AuthorizationException;
import com.cflima.cursomc.serivices.exceptions.DataIntegrityException;
import com.cflima.cursomc.serivices.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository rep;
	
	@Autowired
	private EnderecoRepository repEnd;
	
	private BCryptPasswordEncoder passwordEncoder;

	public Cliente find(Integer id) {
		
		UserSS user = UserService.authenticated();
		if(user == null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado");
		}
		
		Optional<Cliente> obj = rep.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado !!! ID: " + id + ", Tipo: " + Cliente.class.getName()));
	}
	
	@Transactional
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = rep.save(obj);
		repEnd.saveAll(obj.getEnderecos());
		return obj;
	}
	
	public Cliente update(Cliente obj) {
		Cliente newObj = find(obj.getId());
		updateDate(newObj, obj);
		return rep.save(newObj);
	}


	public void delete(Integer id) {
		find(id);
		try {
			rep.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir porque há pedidos relacionados");
		}
	}

	public List<Cliente> findAll() {
		return (List<Cliente>) rep.findAll();
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return rep.findAll(pageRequest);
	}
	
	public Cliente fromDTO(ClienteDTO objDTO) {
		return new Cliente(objDTO.getId(), objDTO.getNome(), objDTO.getEmail(), null, null, null);
	}
	
	public Cliente fromDTO(ClienteNewDTO objDTO) {
		Cliente cliente = new Cliente(null, objDTO.getNome(), objDTO.getEmail(), objDTO.getCpfOuCnpj(),
				TipoCliente.toEnum(objDTO.getTipo()), passwordEncoder.encode(objDTO.getSenha()));
		Cidade cidade = new Cidade(objDTO.getCidadeId(), null, null);
		Endereco end = new Endereco(null, objDTO.getLogradouro(), objDTO.getNumero(), objDTO.getComplemento(),
				objDTO.getBairro(), objDTO.getCep(), cliente, cidade);
		cliente.getEnderecos().add(end);
		cliente.getTelefones().add(objDTO.getTelefone1());
		if(objDTO.getTelefone2() != null) {
			cliente.getTelefones().add(objDTO.getTelefone2());
		}
		if(objDTO.getTelefone3() != null) {
			cliente.getTelefones().add(objDTO.getTelefone3());
		}
		
		
		return cliente;
	}
	
	private void updateDate(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
}
