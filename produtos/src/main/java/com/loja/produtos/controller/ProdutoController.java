package com.loja.produtos.controller;

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
    public ResponseEntity<Produto> cadastrar(@ModelAttribute Produto produto, @RequestParam("imagem") MultipartFile imagem) {
        try {
            if (!imagem.isEmpty()) {
                Long ultimoId = produtoRepositorio.obterUltimoId();

                // Incrementa o ID para gerar o próximo nome de arquivo
                Long nomeArquiv = ultimoId != null ? ultimoId + 1 : 1;

                // Criar um nome único para o arquivo de imagem
                String nomeArquivo = nomeArquiv +  ".jpg";

                // Construir o caminho completo para salvar o arquivo
                String diretorioUpload = "C:\\Users\\Joaor\\OneDrive\\Área de Trabalho\\UmNovoComeço\\Loja\\src\\assets\\imagem";
                String caminhoCompleto = diretorioUpload + File.separator + nomeArquivo;

                // Salvar a imagem no diretório
                Files.copy(imagem.getInputStream(), Paths.get(caminhoCompleto), StandardCopyOption.REPLACE_EXISTING);

                // Armazenar apenas o nome do arquivo no objeto Produto
                produto.setNomeImagem(nomeArquivo);
            }

            // Salvar o produto no banco de dados
            Produto produtoCadastrado = produtoRepositorio.save(produto);
            return ResponseEntity.ok(produtoCadastrado);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PutMapping
    public ResponseEntity<Produto> altera(Produto produto) {
        Produto alteraProduto = produtoRepositorio.save(produto);
        return ResponseEntity.ok(alteraProduto);
    }

    @DeleteMapping(path = "/{id}")
    public Produto deleta(@PathVariable Long id) {
        Produto produto = produtoRepositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado com o ID: " + id));

        produtoRepositorio.deleteById(id);
        return produto;
    }

    @GetMapping(path = "/{id}")
    public Optional<Produto> buscaProduto(@PathVariable Long id) {
        return produtoRepositorio.findById(id);
    }
}
