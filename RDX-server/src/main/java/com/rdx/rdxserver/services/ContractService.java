package com.rdx.rdxserver.services;

import com.rdx.rdxserver.repositories.ContractRepository;
import org.springframework.stereotype.Service;

@Service
public class ContractService {

    private final ContractRepository contractRepository;

    public ContractService(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }
}
