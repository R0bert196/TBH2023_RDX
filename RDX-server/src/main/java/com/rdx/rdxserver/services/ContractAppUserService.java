package com.rdx.rdxserver.services;

import com.rdx.rdxserver.entities.AppUserEntity;
import com.rdx.rdxserver.entities.ContractAppUserEntity;
import com.rdx.rdxserver.entities.ContractEntity;
import com.rdx.rdxserver.repositories.ContractAppUserRepository;
import org.springframework.stereotype.Service;

@Service
public class ContractAppUserService {

    private final ContractAppUserRepository contractAppUserRepository;

    public ContractAppUserService(ContractAppUserRepository contractAppUserRepository) {
        this.contractAppUserRepository = contractAppUserRepository;
    }


    public ContractAppUserEntity save(AppUserEntity user, ContractEntity contract) {
        ContractAppUserEntity contractAppUserEntity = ContractAppUserEntity.builder()
                .appUser(user)
                .contract(contract)
                .build();
        return contractAppUserRepository.save(contractAppUserEntity);
    }

}
