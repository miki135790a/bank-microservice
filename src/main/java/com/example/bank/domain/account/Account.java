package com.example.bank.domain.account;

import com.example.bank.domain.BaseEntity;
import com.example.bank.domain.client.Client;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table( name = "accounts")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)

public class Account extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accounts_seq")
    @SequenceGenerator(
            name = "accounts_seq",
            sequenceName = "accounts_id_seq",
            allocationSize = 1
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false, updatable = false)
    private Client client;

    @Column(nullable = false, unique = true, updatable = false)
    private String number;

    @Column(nullable = false,precision = 19,scale = 2)
    private BigDecimal balance;

    @Column(nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @Column(name = "closed_at")
    private LocalDateTime closedAt;

    public static Account create(Client client, String number, Currency currency) {
        return Account.builder()
                .client(client)
                .number(number)
                .currency(currency)
                .balance(BigDecimal.ZERO)
                .status(AccountStatus.ACTIVE)
                .build();
    }

    public void close () {
        canBeClosed();
        this.status = AccountStatus.CLOSED;
        this.closedAt = LocalDateTime.now();
    }

    public void debit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (this.balance.compareTo(amount) < 0) {
            throw new com.example.bank.web.exception.InsufficientFundsException(
                    "Insufficient funds: balance=" + this.balance + ", required=" + amount);
        }
        this.balance = this.balance.subtract(amount);
    }

    public void credit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        this.balance = this.balance.add(amount);
    }

    public void checkActive() {
        if (this.status != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Account " + this.number + " is not active. Status: " + this.status);
        }
    }


    private void canBeClosed () {
        if (this.status == AccountStatus.CLOSED) {
            throw new IllegalStateException("Account is already closed");
        }
        if (this.status == AccountStatus.BLOCKED) {
            throw new IllegalStateException("Cannot close blocked account. Unblock it first.");
        }
        if (this.balance.compareTo(BigDecimal.ZERO) > 0) {
            throw new IllegalStateException("Cannot close account with non-zero balance: " + this.balance);
        }
    }




}
