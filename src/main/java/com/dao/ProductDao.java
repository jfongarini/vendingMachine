package com.dao;

import com.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductDao extends JpaRepository<Product, Integer> {
    @Query(value = "from Product where name = :name AND exist = true")
    Optional<Product> findByName(@Param("name") String name);

    @Query(value = "from Product where productId = :id AND exist = true")
    Optional<Product> findById(@Param("id") int id);

    @Query(value = "from Product where exist = true")
    List<Product> findAll();
}
