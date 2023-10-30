package com.dao;

import com.model.VendingMachine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface VendingMachineDao extends JpaRepository<VendingMachine, Integer> {
    @Query(value = "from VendingMachine where id = :id AND exist = true")
    Optional<VendingMachine> findById(@Param("id") int id);

    @Query(value = "from VendingMachine where exist = true")
    List<VendingMachine> findAll();
}
