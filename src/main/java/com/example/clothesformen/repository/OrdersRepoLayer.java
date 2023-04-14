package com.example.clothesformen.repository;

import com.example.clothesformen.Entity.AdminOrders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdersRepoLayer extends JpaRepository<AdminOrders,Long> {

    List<AdminOrders> findAll();

}
