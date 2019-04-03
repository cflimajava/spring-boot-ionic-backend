package com.cflima.cursomc.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cflima.cursomc.domain.Cidade;

@Repository
public interface CidadeRepository extends CrudRepository<Cidade, Integer>{

}
