package com.dao;

import com.model.VendingMachine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendingMachineDao extends JpaRepository<VendingMachine, Integer> {
}
