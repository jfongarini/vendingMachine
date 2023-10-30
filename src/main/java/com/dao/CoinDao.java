package com.dao;

import com.model.Coin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CoinDao extends JpaRepository<Coin, Integer> {
    @Query(value = "from Coin where name = :name AND exist = true")
    Optional<Coin> findByName(@Param("name") String name);

    @Query(value = "from Coin where coinId = :id AND exist = true")
    Optional<Coin> findById(@Param("id") int id);

    @Query(value = "from Coin where exist = true")
    List<Coin> findAll();
}
