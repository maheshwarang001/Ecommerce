package com.example.clothesformen.repository;

import com.example.clothesformen.Entity.CartList;
import com.example.clothesformen.Entity.ProductList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartRepoLayer extends JpaRepository<CartList,Integer> {

    CartList findByCartID(int i);

    @Query(value = "SELECT SUM(price) FROM cart_list", nativeQuery = true)
    Double getTotalPrice();
}
