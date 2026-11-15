package com.example.bank.domain.operation;


import com.example.bank.domain.BaseEntity;
import com.example.bank.domain.account.Account;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "operations")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Operation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "operations_seq")
    @SequenceGenerator(name = "operations_seq", sequenceName = "operations_id_seq",allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "from_account_id",updatable = false)
    private Account fromAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "to_account_id",updatable = false)
    private Account toAccount;

    @Column(nullable = false,precision = 19,scale = 2,updatable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private OperationType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OperationStatus status;

    @Column(name = "failure_reason")
    private String failureReason;

    public static Operation createTransfer(Account fromAccount, Account toAccount, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (fromAccount != null && toAccount != null && Objects.equals(fromAccount.getId(), toAccount.getId())) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }

        return Operation.builder()
                .fromAccount(fromAccount)
                .toAccount(toAccount)
                .amount(amount)
                .type(OperationType.TRANSFER)
                .status(OperationStatus.PENDING)
                .build();
    }

    public void markCompleted() {
        checkPendingStatus();
        this.status = OperationStatus.COMPLETED;
    }

    public void markFailed(String reason) {
        checkPendingStatus();
        this.status = OperationStatus.FAILED;
        this.failureReason = reason;
    }

    private void checkPendingStatus () {
        if (!Objects.equals(this.status, OperationStatus.PENDING)) {
            throw new IllegalStateException("Only PENDING operations can be completed. Current: " + this.status);
        }

    }





}
