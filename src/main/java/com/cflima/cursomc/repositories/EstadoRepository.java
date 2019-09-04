package com.cflima.cursomc.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cflima.cursomc.domain.Estado;

@Repository
public interface EstadoRepository extends CrudRepository<Estado, Integer>{
	
	@Transactional(readOnly=true)
	public List<Estado> findAllByOrderByNome();

}
