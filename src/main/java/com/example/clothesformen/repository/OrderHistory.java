package com.example.clothesformen.repository;

import com.example.clothesformen.Entity.AdminOrders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderHistory extends JpaRepository<AdminOrders,Long> {
}
