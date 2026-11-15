package com.example.bank.repository;

import com.example.bank.domain.operation.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRepository extends JpaRepository<Operation, Long> {
}