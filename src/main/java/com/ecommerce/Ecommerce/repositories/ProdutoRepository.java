package com.ecommerce.Ecommerce.repositories;

import com.ecommerce.Ecommerce.models.ProdutoModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<ProdutoModel, Long> {

}

