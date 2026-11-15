package com.example.bank.web.mapper;

import com.example.bank.domain.client.Client;
import com.example.bank.web.dto.request.CreateClientRequest;
import com.example.bank.web.dto.response.ClientResponse;
import org.springframework.stereotype.Component;


@Component
public class ClientMapper {
    public Client toEntity(CreateClientRequest request) {
        return Client.create(request.fullName(), request.email());
    }


    public ClientResponse toResponse (Client client){
        return new ClientResponse(
                client.getId(),
                client.getFullName(),
                client.getEmail(),
                client.getCreatedAt(),
                client.getUpdatedAt()
        );
    }


}
