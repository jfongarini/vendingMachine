package com.dao;

import com.model.Coin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoinDao extends JpaRepository<Coin, Integer> {
    Optional<Coin> findByName(String name);
}
