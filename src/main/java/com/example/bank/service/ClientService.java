package com.example.bank.service;

import com.example.bank.web.dto.request.CreateClientRequest;
import com.example.bank.web.dto.request.UpdateClientRequest;
import com.example.bank.web.dto.response.ClientResponse;

import java.util.List;

public interface ClientService {

    ClientResponse create (CreateClientRequest request);
    ClientResponse findById (Long id);
    List <ClientResponse> findAll();
    ClientResponse update (Long id, UpdateClientRequest request);
    void delete (Long id);
}
