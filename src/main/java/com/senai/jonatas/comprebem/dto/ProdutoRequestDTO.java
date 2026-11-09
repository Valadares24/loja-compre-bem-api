package com.senai.jonatas.comprebem.dto;

import com.senai.jonatas.comprebem.entity.Produto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ProdutoRequestDTO(
        @NotBlank(message = "O nome do produto é obrigatório")
        String nome,

        @NotNull(message = "O preço do produto é obrigatório")
        @Positive(message = "O preço deve ser maior que zero")
        BigDecimal preco,

        @NotBlank(message = "A descrição do produto é obrigatória")
        String descricao,

        @PositiveOrZero(message = "O estoque não pode ser negativo")
        Integer estoque
) {
    public Produto toEntity() {
        return Produto.builder()
                .nome(nome.trim())
                .preco(preco)
                .descricao(descricao.trim())
                .estoque(estoque != null ? estoque : 10)
                .build();
    }
}

