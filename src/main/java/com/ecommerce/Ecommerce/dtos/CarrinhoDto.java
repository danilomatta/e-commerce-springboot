package com.ecommerce.Ecommerce.dtos;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record CarrinhoDto(@NotNull @NotEmpty Map<Long, Integer> produtos) {
}


