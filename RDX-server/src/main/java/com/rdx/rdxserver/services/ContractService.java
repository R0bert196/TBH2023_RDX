package com.rdx.rdxserver.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rdx.rdxserver.apis.OpenAiApi;
import com.rdx.rdxserver.entities.*;
import com.rdx.rdxserver.repositories.AppUserRepository;
import com.rdx.rdxserver.repositories.ContractRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ContractService {

    private final ContractRepository contractRepository;
    private final AppUserRepository appUserRepository;
    private final WalletService walletService;
    private final OpenAiApi openAiApi;
    private final EmbeddingsService embeddingsService;

    private final ContractAppUserService contractAppUserService;
    private final AppUserService appUserService;


    public ContractService(ContractRepository contractRepository, AppUserRepository appUserRepository, WalletService walletService, OpenAiApi openAiApi, EmbeddingsService embeddingsService, ContractAppUserService contractAppUserService, AppUserService appUserService) {
        this.contractRepository = contractRepository;
        this.appUserRepository = appUserRepository;
        this.walletService = walletService;
        this.openAiApi = openAiApi;
        this.embeddingsService = embeddingsService;
        this.contractAppUserService = contractAppUserService;
        this.appUserService = appUserService;
    }

    public ContractEntity getContractById(int id) {
        return contractRepository.findById(id).orElse(null);
    }

    public ContractEntity registerContract(ContractEntity tempContractEntity, AppUserEntity user) throws JsonProcessingException {
//        if (contractRepository.findById(tempContractEntity.getId()).isPresent()) {
//            return null;
//        }


        //TODO: calculam din temp contract value #cvuri : ex pt 10 egold dam 20 cvuri..
        // ceren ka embeding service sa ne dea cele mai asemenatoare #cvuri care se aseamana cu embedingul contractului
//        List<AppUserEntity> useriPotriviti = embeddingService.sortBy(tempContractEntity.getEmbeddingsEntity().getValues(),tempContractEntity.getBudget())

        //handle wallet save logic
        CompanyEntity company = user.getCompanyEntity();
        WalletEntity wallet = walletService.createAndSaveWallet();

        //handle embeddings save logic
        float[] textEmbeddings = openAiApi.getTextEmbeddings(tempContractEntity.getProfileText());
        EmbeddingsEntity embeddings = embeddingsService.createAndSaveEmbeddings(textEmbeddings);


        //create an embedding score for each person in the database

        appUserService.generateEmbeddingForEveryone(embeddings);

        ContractEntity newContract = ContractEntity
                .builder()
                .profileText(tempContractEntity.getProfileText())
                .budget(tempContractEntity.getBudget())
                .companyEntity(company)
                .walletEntity(wallet)
                .embeddingsEntity(embeddings)
                .startDate(LocalDate.now())
                .paid(false)
                .build();


        ContractEntity contract = contractRepository.save(newContract);
        contractAppUserService.save(user, contract);
        return contract;

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

    public List<ContractEntity> findAllByUserId(int id) {
        return contractRepository.findAllByUserId(id);
    }
}
