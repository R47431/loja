package com.loja.produtos.controller;

import com.loja.produtos.model.ErrorResponse;
import com.loja.produtos.model.Produto;
import com.loja.produtos.repositorio.ProdutoRepositorio;


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

    @GetMapping
    public Iterable<Produto> listar() {
        return produtoRepositorio.findAll();
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@ModelAttribute Produto produto, MultipartFile imagem) {
        try {
            if (!imagem.isEmpty()) {
                Long ultimoId = produtoRepositorio.obterUltimoId();
                Long nomeId = ultimoId != null ? ultimoId + 1 : 1;
                String nomeArquivo = nomeId + ".jpg";

                String diretorioUpload = "C:\\Users\\Joaor\\OneDrive\\Área de Trabalho\\UmNovoComeço\\Loja\\src\\assets\\imagem";
                String caminhoCompleto = diretorioUpload + File.separator + nomeArquivo;

                Files.copy(imagem.getInputStream(), Paths.get(caminhoCompleto), StandardCopyOption.REPLACE_EXISTING);

                produto.setNomeImagem(nomeArquivo);
            }
            Optional<Produto> produtoExistentee = produtoRepositorio.findByNome(produto.getNome());
            if (produtoExistentee.isPresent()) {
                ErrorResponse  errorResposta = new ErrorResponse("O nome do produto já está em uso.");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResposta);
            }
            Produto produtoCadastrado = produtoRepositorio.save(produto);
            return ResponseEntity.ok(produtoCadastrado);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PutMapping
    public ResponseEntity<?> altera(Produto produto) {

        Optional<Produto> produtoExistentee = produtoRepositorio.findByNome(produto.getNome());
        if (produtoExistentee.isPresent()) {
            ErrorResponse  errorResposta = new ErrorResponse("O nome do produto já está em uso.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResposta);
        }
        Produto alteraProduto = produtoRepositorio.save(produto);
        return ResponseEntity.ok(alteraProduto);
    }

    @DeleteMapping(path = "/{id}")
    public Produto deleta(@PathVariable Long id) {

        Produto produto = produtoRepositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado com o ID: " + id));
        String nomeImagem = produto.getNomeImagem();

        if (nomeImagem != null && !nomeImagem.isEmpty()) {
            String diretorioUpload = "C:\\Users\\Joaor\\OneDrive\\Área de Trabalho\\UmNovoComeço\\Loja\\src\\assets\\imagem";
            String caminhoCompleto = diretorioUpload + File.separator + nomeImagem;

            File arquivoImagem = new File(caminhoCompleto);
            if (arquivoImagem.exists()) {
                arquivoImagem.delete();
            }
        }
        produtoRepositorio.deleteById(id);
        return produto;
    }

    @GetMapping(path = "/{nome}")
    public Optional<Produto> buscaProduto(@PathVariable String nome) {
        return produtoRepositorio.findByNome(nome);
    }
}
