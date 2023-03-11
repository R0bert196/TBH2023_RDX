package com.rdx.rdxserver.services;

import com.rdx.rdxserver.entities.AppUserEntity;
import com.rdx.rdxserver.entities.CompanyEntity;
import com.rdx.rdxserver.entities.ContractEntity;
import com.rdx.rdxserver.entities.WalletEntity;
import com.rdx.rdxserver.repositories.AppUserRepository;
import com.rdx.rdxserver.repositories.ContractRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContractService {

    private final ContractRepository contractRepository;
    private final AppUserRepository appUserRepository;
    private final WalletService walletService;


    public ContractService(ContractRepository contractRepository, AppUserRepository appUserRepository, WalletService walletService) {
        this.contractRepository = contractRepository;
        this.appUserRepository = appUserRepository;
        this.walletService = walletService;
    }

    public ContractEntity getContractById(int id) {
        return contractRepository.findById(id).orElse(null);
    }

    public ContractEntity registerContract(ContractEntity tempContractEntity, AppUserEntity user) {
//        if (contractRepository.findById(tempContractEntity.getId()).isPresent()) {
//            return null;
//        }


        //TODO: calculam din temp contract value #cvuri : ex pt 10 egold dam 20 cvuri..
        // ceren ka embeding service sa ne dea cele mai asemenatoare #cvuri care se aseamana cu embedingul contractului
//        List<AppUserEntity> useriPotriviti = embeddingService.sortBy(tempContractEntity.getEmbeddingsEntity().getValues(),tempContractEntity.getBudget())
        CompanyEntity company = user.getCompanyEntity();
        WalletEntity wallet = walletService.createAndSaveWallet();


        ContractEntity newContract = ContractEntity
                .builder()
                .profileText(tempContractEntity.getProfileText())
                .budget(tempContractEntity.getBudget())
                .companyEntity(company)
                .walletEntity(wallet)
                .paid(false)
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
//        oldContract.setExpirationDate(newContract.getExpirationDate());
        //TODO: Add wallet to change ?
        contractRepository.save(oldContract);
        return oldContract;
    }
}
