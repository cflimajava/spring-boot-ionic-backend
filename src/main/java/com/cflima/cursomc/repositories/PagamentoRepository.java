package com.cflima.cursomc.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cflima.cursomc.domain.Pagamento;

@Repository
public interface PagamentoRepository extends CrudRepository<Pagamento, Integer>{

}
