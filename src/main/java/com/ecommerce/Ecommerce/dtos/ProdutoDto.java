package com.ecommerce.Ecommerce.dtos;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProdutoDto(Long id,@NotBlank @Size(min = 1, max = 255) String nome, @NotBlank @Size(min = 1, max = 1000) String descricao, @NotNull @DecimalMin(value = "0.01") BigDecimal preco, @NotNull @Min(value = 0) int quantidadeEstoque, boolean ativo){
}
