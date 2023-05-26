package com.loja.produtos.repositorio;

import com.loja.produtos.model.Produto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ProdutoRepositorio extends CrudRepository<Produto,Long> {

    @Query("SELECT MAX(p.id) FROM Produto p")
    Long obterUltimoId();
}
