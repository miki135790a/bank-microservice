package com.example.bank.web.controller;


import com.example.bank.service.ClientService;
import com.example.bank.web.dto.request.CreateClientRequest;
import com.example.bank.web.dto.request.UpdateClientRequest;
import com.example.bank.web.dto.response.ClientResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping ("/clients")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClientResponse create(@Valid @RequestBody CreateClientRequest request) {
        return clientService.create(request);
    }

    @GetMapping("/{id}")
    public ClientResponse getById (@PathVariable Long id) {
        return clientService.findById(id);
    }

    @PatchMapping("/{id}")
    public ClientResponse update (@PathVariable Long id, @Valid @RequestBody UpdateClientRequest request) {
        return clientService.update(id, request);
    }

    @GetMapping
    public List<ClientResponse> getAll() {
        return clientService.findAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete (@PathVariable Long id) {
        clientService.delete(id);
    }
}

