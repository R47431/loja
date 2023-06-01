package com.loja.produtos.service;

import com.loja.produtos.model.Produto;
import com.loja.produtos.repositorio.ProdutoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Optional;


@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepositorio produtoRepositorio;

    String diretorio = "C:\\Users\\Joaor\\OneDrive\\Área de Trabalho\\UmNovoComeço\\Loja\\src\\assets\\imagem";


    public String diretorioComNome(Produto produto) {
        String nomeArquivo = produto.getNome() + ".jpg";
        String caminhoCompleto = diretorio + File.separator + nomeArquivo;

        return caminhoCompleto;
    }

    public String diretorioComImagem(Produto produto) {
        String nomeArquivo = produto.getNomeImagem();
        String caminhoCompleto = diretorio + File.separator + nomeArquivo;

        return caminhoCompleto;
    }

    public void validaCampo(Produto produto, MultipartFile imagem){
        if (imagem.isEmpty()) {
            throw new IllegalArgumentException("A imagem do produto é obrigatória.");
        }
        Optional<Produto> produtoExistente  = produtoRepositorio.findByNome(produto.getNome());
        if (produtoExistente.isPresent() && !produtoExistente.get().equals(produto)) {
            throw  new IllegalArgumentException("O nome do produto já está em uso.");
        }
    }

}
