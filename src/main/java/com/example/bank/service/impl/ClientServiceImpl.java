package com.example.bank.service.impl;


import com.example.bank.domain.client.Client;
import com.example.bank.repository.ClientRepository;
import com.example.bank.service.ClientService;
import com.example.bank.web.dto.request.CreateClientRequest;
import com.example.bank.web.dto.request.UpdateClientRequest;
import com.example.bank.web.dto.response.ClientResponse;
import com.example.bank.web.mapper.ClientMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {


    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Override
    @Transactional
    public ClientResponse create(CreateClientRequest request) {
        Client client = clientMapper.toEntity(request);
        Client saved = clientRepository.save(client);
        return clientMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ClientResponse findById(Long id) {
        Client client = findClientById(id);
        return clientMapper.toResponse(client);
    }

    @Override
    @Transactional(readOnly = true)
    public List <ClientResponse> findAll () {
        return clientRepository.findAll().stream().map(clientMapper::toResponse).toList();
    }

    @Override
    @Transactional
    public ClientResponse update (Long id, UpdateClientRequest request) {
        Client client = findClientById(id);
        if (request.fullName() != null) {
            client.changeFullName(request.fullName());
        }
        if (request.email() != null) {
            client.changeEmail(request.email());
        }
        return clientMapper.toResponse(client);

    }

    @Override
    @Transactional
    public void delete(Long id) {
        clientRepository.deleteById(id);
    }


    private Client findClientById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client with id " + id + " not found"));
    }


}
