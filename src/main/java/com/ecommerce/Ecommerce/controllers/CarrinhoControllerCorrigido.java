package com.ecommerce.Ecommerce.controllers;

import com.ecommerce.Ecommerce.dtos.CarrinhoDto;
import com.ecommerce.Ecommerce.models.ProdutoModel;
import com.ecommerce.Ecommerce.repositories.ProdutoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("carrinho1")
public class CarrinhoControllerCorrigido {


    @Autowired
    private ProdutoRepository produtoRepository;

    @PutMapping
    @Transactional
    public ResponseEntity<String> carrinhoCompra(@RequestBody @Valid CarrinhoDto carrinhoDto){
        List<Long> idDosPRodutos = new ArrayList<>(carrinhoDto.produtos().keySet());
        List<ProdutoModel> produtoModelList = new ArrayList<>(produtoRepository.findAllById(idDosPRodutos));

        for(Long id: idDosPRodutos){
            Optional<ProdutoModel> optionalProdutoModel = produtoRepository.findById(id);
            if(optionalProdutoModel.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id "+ id +" não localizada");
            }
        }

        for(ProdutoModel produtos: produtoModelList){
            Integer quantidadeCarrinho = carrinhoDto.produtos().get(produtos.getId());
            int quantidadeEstoqueAtual = produtos.getQuantidade_estoque();
            if(quantidadeEstoqueAtual<quantidadeCarrinho){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Quantidade insuficiente para id "+produtos.getId()+
                        ". Qtd máxima: "+produtos.getQuantidade_estoque());
            }
        }

        BigDecimal total = BigDecimal.ZERO;
        for(ProdutoModel produtos : produtoModelList){
            Integer quantidadeCarrinho = carrinhoDto.produtos().get(produtos.getId());
            int quantidadeAtualEstoque = produtos.getQuantidade_estoque();
            int quantidadeAtualizadaEstoque = quantidadeAtualEstoque - quantidadeCarrinho;
            produtos.setQuantidade_estoque(quantidadeAtualizadaEstoque);
            produtoRepository.save(produtos);

            BigDecimal preco = produtos.getPreco();
            BigDecimal subtotalProdutos = preco.multiply(BigDecimal.valueOf(quantidadeCarrinho));
            total = total.add(subtotalProdutos);
        }
        return ResponseEntity.status(HttpStatus.OK).body("Compra realizada com sucesso. Total: "+total);
    }
}
