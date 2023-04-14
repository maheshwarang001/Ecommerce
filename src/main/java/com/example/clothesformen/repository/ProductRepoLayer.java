package com.example.clothesformen.repository;


import com.example.clothesformen.Entity.ProductList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepoLayer extends JpaRepository<ProductList,Long> {
    List<ProductList> findAll();
    List<ProductList> findByGenre(String s);

}
