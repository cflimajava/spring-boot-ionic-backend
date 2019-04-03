package com.cflima.cursomc.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cflima.cursomc.domain.Estado;

@Repository
public interface EstadoRepository extends CrudRepository<Estado, Integer>{

}
