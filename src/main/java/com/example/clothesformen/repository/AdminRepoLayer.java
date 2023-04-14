package com.example.clothesformen.repository;

import com.example.clothesformen.Entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepoLayer extends JpaRepository<Admin,Long> {

    Admin findByUsername(String s);
}
