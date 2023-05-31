package com.loja.produtos.controller;

import com.loja.produtos.model.Produto;
import com.loja.produtos.repositorio.ProdutoRepositorio;


import com.loja.produtos.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;


@RestController
@RequestMapping("/")
@CrossOrigin("*")
public class ProdutoController {

    @Autowired
    private ProdutoRepositorio produtoRepositorio;

    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public Iterable<Produto> listar() {
        return produtoRepositorio.findAll();
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@ModelAttribute Produto produto, MultipartFile imagem) {
        try {
            if (imagem.isEmpty()) {
                return ResponseEntity.badRequest().body("A imagem do produto é obrigatória.");
            }
            Optional<Produto> nomeExistente = produtoRepositorio.findByNome(produto.getNome());
            if (nomeExistente.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("O nome do produto já está em uso.");
            }
            String diretorio = produtoService.diretorioComNome(produto);

            Files.copy(imagem.getInputStream(), Paths.get(diretorio), StandardCopyOption.REPLACE_EXISTING);
            produto.setNomeImagem(produto.getNome() + ".jpg");

            Produto produtoCadastrado = produtoRepositorio.save(produto);
            return ResponseEntity.ok(produtoCadastrado);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro ao processar o cadastro.");
        }
    }

    @PutMapping
    public ResponseEntity<?> altera(Produto produto, MultipartFile imagem) {
        try {
            Optional<Produto> produtoExistente = produtoRepositorio.findByNome(produto.getNome());
            if (produtoExistente.isPresent() && !produtoExistente.get().getId().equals(produto.getId())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("O nome do produto já está em uso.");
            }
            String diretorio = produtoService.diretorioComNome(produto);

            Files.copy(imagem.getInputStream(), Paths.get(diretorio), StandardCopyOption.REPLACE_EXISTING);
            produto.setNomeImagem(produto.getNome() + ".jpg");

            Produto alteraProduto = produtoRepositorio.save(produto);
            return ResponseEntity.ok(alteraProduto);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro ao processar o Alteracao.");
        }
    }

    @DeleteMapping(path = "/{id}")
    public Produto deleta(@PathVariable Long id) {
        Produto produto = produtoRepositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado com o ID: " + id));
        String diretorio = produtoService.diretorioComImagem(produto);
        File arquivoImagem = new File(diretorio);
        if (arquivoImagem.exists()) {
            arquivoImagem.delete();
        }
        produtoRepositorio.deleteById(id);
        return produto;
    }

    @GetMapping(path = "/{nome}")
    public Optional<Produto> buscaProduto(@PathVariable String nome) {
        return produtoRepositorio.findByNome(nome);
    }
}