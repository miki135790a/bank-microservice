package com.example.bank.web.controller;


import com.example.bank.service.TransferService;
import com.example.bank.web.dto.request.TransferRequest;
import com.example.bank.web.dto.response.TransferResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public TransferResponse initiateTransfer (@Valid @RequestBody TransferRequest request) {
        return transferService.initiateTransfer(request);
    }

    @GetMapping("/{id}")
    public TransferResponse getById(@PathVariable Long id) {
        return transferService.findById(id);
    }
}
