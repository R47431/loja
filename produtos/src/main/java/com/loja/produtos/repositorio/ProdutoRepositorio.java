package com.loja.produtos.repositorio;

import com.loja.produtos.model.Produto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProdutoRepositorio extends CrudRepository<Produto,Long> {

    @Query("SELECT MAX(p.id) FROM Produto p")
    Long obterUltimoId();

    /*
     @Query("SELECT p.nome FROM Produto p WHERE p.nome = :nome ORDER BY p.id DESC LIMIT 1")
    String obterUltimoNome();
     */

    Optional<Produto> findByNome( String nome);

}
