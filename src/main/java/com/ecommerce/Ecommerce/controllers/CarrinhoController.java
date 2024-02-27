package com.ecommerce.Ecommerce.controllers;

import com.ecommerce.Ecommerce.dtos.CarrinhoDto;
import com.ecommerce.Ecommerce.models.ProdutoModel;
import com.ecommerce.Ecommerce.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping
@Validated
public class CarrinhoController {


    @Autowired
    private ProdutoRepository produtoRepository;

    @PutMapping("/carrinho")
    @Transactional
    public ResponseEntity<String> atualizarCarrinho(@RequestBody CarrinhoDto carrinhoDto) {
        BigDecimal total = BigDecimal.ZERO;

        for (ProdutoModel produto : produtoRepository.findAllById(carrinhoDto.produtos().keySet())) {
            Long produtoId = produto.getId();
            Integer quantidadeCarrinho = carrinhoDto.produtos().get(produtoId);

            if (quantidadeCarrinho != null) {
                int quantidadeEstoqueAtual = produto.getQuantidade_estoque();

                if (quantidadeCarrinho <= quantidadeEstoqueAtual) {
                    int quantidadeEstoqueAtualizada = quantidadeEstoqueAtual - quantidadeCarrinho;
                    produto.setQuantidade_estoque(quantidadeEstoqueAtualizada);
                    produtoRepository.save(produto);

                    BigDecimal subtotalProduto = produto.getPreco().multiply(BigDecimal.valueOf(quantidadeCarrinho));
                    total = total.add(subtotalProduto);
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Estoque insuficiente para o produto com ID " + produtoId +
                                    ". Quantidade máxima permitida: " + quantidadeEstoqueAtual);
                }
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body("Compra realizada com sucesso. Total: " + total);

    }
}