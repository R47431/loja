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
    public ResponseEntity<?> cadastrar(Produto produto, MultipartFile imagem) {
        try {
            produtoService.validaCampo(produto,imagem);

            String diretorio = produtoService.diretorioComNome(produto);

            Files.copy(imagem.getInputStream(), Paths.get(diretorio), StandardCopyOption.REPLACE_EXISTING);
            produto.setNomeImagem(produto.getNome() + ".jpg");

            Produto produtoCadastrado = produtoRepositorio.save(produto);
            return ResponseEntity.ok(produtoCadastrado);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro ao processar o cadastro.");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        }
    }

    @PutMapping
    public ResponseEntity<?> altera(Produto produto, MultipartFile imagem) {
        try {
            produtoService.validaCampo(produto,imagem);

            String diretorio = produtoService.diretorioComNome(produto);

            Files.copy(imagem.getInputStream(), Paths.get(diretorio), StandardCopyOption.REPLACE_EXISTING);
            produto.setNomeImagem(produto.getNome() + ".jpg");

            Produto alteraProduto = produtoRepositorio.save(produto);
            return ResponseEntity.ok(alteraProduto);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro ao processar o Alteracao.");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleta(@PathVariable Long id) {
        try {
            Optional<Produto> findId = produtoRepositorio.findById(id);

            if (findId.isPresent()) {
                String diretorio = produtoService.diretorioComImagem(findId.get());

                File arquivoImagem = new File(diretorio);
                if (arquivoImagem.exists()) {
                    arquivoImagem.delete();
                }

                produtoRepositorio.deleteById(id);

                return ResponseEntity.ok().body("produto deletado");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro ao excluir o produto.");
        }
    }

    @GetMapping(path = "/{nome}")
    public ResponseEntity<?> buscaProduto(@PathVariable String nome) {
        try {
            if (nome.isEmpty()) {
                return ResponseEntity.badRequest().body("nao pode esta vazio");
            }

            Optional<Produto> produtoEncontrado = produtoRepositorio.findByNome(nome);
            if (produtoEncontrado.isPresent()) {
                return ResponseEntity.ok(produtoEncontrado.get());
            }else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro ao buscar o produto.");
        }

    }












    //EM CASO DE POLUIÇÃO USE
    @DeleteMapping("/all")
    public void teate() {
        try {
            Iterable<Produto> produtos = produtoRepositorio.findAll();

            for (Produto produto : produtos) {
                String diretorio = produtoService.diretorioComImagem(produto);

                File arquivoImagem = new File(diretorio);
                if (arquivoImagem.exists()) {
                    arquivoImagem.delete();
                }
            }

            produtoRepositorio.deleteAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}