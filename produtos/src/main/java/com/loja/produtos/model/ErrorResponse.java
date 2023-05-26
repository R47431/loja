package com.loja.produtos.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    private String messagem;

    public ErrorResponse(String messagem) {
        this.messagem = messagem;
    }
}
