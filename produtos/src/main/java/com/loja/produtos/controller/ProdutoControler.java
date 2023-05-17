package com.loja.produtos.controller;

import com.loja.produtos.model.Produto;
import com.loja.produtos.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/")
@CrossOrigin("*")
public class ProdutoControler {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public Iterable<Produto> listar() {
        return produtoService.lista();
    }

    @PostMapping
    public ResponseEntity<Produto> Cadastrar(@RequestBody Produto produto) {
        Produto cadastraProduto = produtoService.cadastra(produto);
        return ResponseEntity.ok(cadastraProduto);  
    }

    @PutMapping
    public ResponseEntity<Produto> altera(@RequestBody Produto produto) {
        Produto alteraProduto = produtoService.altera(produto);
        return ResponseEntity.ok(alteraProduto);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Produto> deleta(@PathVariable Long id) {
        Produto deletaProduto = produtoService.deleta(id);
        return ResponseEntity.ok(deletaProduto);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Produto> buscaProduto(@PathVariable Long id) {
        Produto buscaProduto = produtoService.buscaProduto(id);
        return ResponseEntity.ok(buscaProduto);
    }

}
