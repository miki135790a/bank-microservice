package com.example.bank.service;

import com.example.bank.web.dto.request.TransferRequest;
import com.example.bank.web.dto.response.TransferResponse;

public interface TransferService {
    TransferResponse initiateTransfer(TransferRequest request);
    TransferResponse findById(Long id);
}