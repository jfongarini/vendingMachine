package com.dao;

import com.model.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface OperationDao extends JpaRepository<Operation, Integer> {
    @Query(value = "from Operation o where date BETWEEN :startDate AND :endDate")
    public List<Operation> getAllBetweenDates(@Param("startDate") @Temporal Date startDate, @Param("endDate") @Temporal Date endDate);

}
