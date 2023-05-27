package com.loja.produtos.repositorio;

import com.loja.produtos.model.Produto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProdutoRepositorio extends CrudRepository<Produto,Long> {
    Optional<Produto> findByNome( String nome);

}
