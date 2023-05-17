package com.loja.produtos.service;

import com.loja.produtos.model.Produto;
import com.loja.produtos.repositorio.ProdutoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepositorio produtoRepositorio;

    public Iterable<Produto> lista() {
        return produtoRepositorio.findAll();
    }

    public Produto cadastra(Produto produto) {
        return produtoRepositorio.save(produto);
    }

    public Produto altera(Produto produto) {
        return produtoRepositorio.save(produto);
    }

    public Produto deleta(Long id) {
        Produto produto = produtoRepositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado com o ID: " + id));

        produtoRepositorio.deleteById(id);
        return produto;
    }

    public Produto buscaProduto(Long id) {
        return produtoRepositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado com o ID:" + id));
    }
}
