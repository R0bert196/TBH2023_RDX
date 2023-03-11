package com.rdx.rdxserver.services;

import com.rdx.rdxserver.entities.ContractEntity;
import com.rdx.rdxserver.repositories.ContractRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContractService {

    private final ContractRepository contractRepository;


    public ContractService(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    public ContractEntity getContractById(int id) {
        return contractRepository.findById(id).orElse(null);
    }

    public ContractEntity registerContract(ContractEntity tempContractEntity) {
        if (contractRepository.findById(tempContractEntity.getId()).isPresent()) return null;
        //TODO: CREATE CONTRACT WALLET !
//        WalletEntity walletEntity = new WalletEntity();
        ContractEntity newContract = ContractEntity
                .builder()
                .profileText(tempContractEntity.getProfileText())
                .companyEntity(tempContractEntity.getCompanyEntity())
                .expirationDate(tempContractEntity.getExpirationDate())
//                .wallet(walletEntity)
                .build();

        //TODO: ADD EMBEDDINGS
        return contractRepository.save(newContract);

    }

    public List<ContractEntity> getAll() {
        return contractRepository.findAll();
    }

    public List<ContractEntity> findByCompanyId(int companyId) {
        return contractRepository.findByCompanyEntity_Id(companyId);
    }

    public ContractEntity update(ContractEntity newContract) {
        Optional<ContractEntity> optionalOldContract = contractRepository.findById(newContract.getId());
        if (optionalOldContract.isEmpty()) return null;
        ContractEntity oldContract = optionalOldContract.get();
        oldContract.setExpirationDate(newContract.getExpirationDate());
        //TODO: Add wallet to change ?
        contractRepository.save(oldContract);
        return oldContract;
    }
}
