package com.example.bank.domain.client;

import com.example.bank.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;



@Entity
@Table(name ="clients")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)



public class Client extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "clients_seq")
    @SequenceGenerator(
            name = "clients_seq",
            sequenceName = "clients_id_seq",
            allocationSize = 1
    )
    private Long id;

    @Column(name = "full_name",nullable = false)
    private String fullName;

    @Column(nullable = false,unique = true)
    private String email;

    public static Client create(String fullName, String email) {
        return Client.builder()
                .fullName(fullName)
                .email(email)
                .build();
    }

    public void changeFullName(String newFullName) {
        if (newFullName == null || newFullName.isBlank()) {
            throw new IllegalArgumentException("FullName must not be blank");
        }
        this.fullName = newFullName;
    }

    public void changeEmail(String newEmail) {
        if (newEmail == null || newEmail.isBlank()) {
            throw new IllegalArgumentException("Email must not be blank");
        }
        this.email = newEmail;
    }
}


