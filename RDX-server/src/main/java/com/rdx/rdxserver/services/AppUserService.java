package com.rdx.rdxserver.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rdx.rdxserver.apis.OpenAiApi;
import com.rdx.rdxserver.entities.AppUserEntity;
import com.rdx.rdxserver.entities.ContractAppUserEntity;
import com.rdx.rdxserver.entities.ContractEntity;
import com.rdx.rdxserver.repositories.AppUserRepository;
import com.rdx.rdxserver.repositories.ContractAppUserRepository;
import com.rdx.rdxserver.repositories.ContractRepository;
import com.rdx.rdxserver.utils.JwtUtil;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class AppUserService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;


    private final AppUserRepository appUserRepository;
    private final ContractRepository contractRepository;
    private final ContractAppUserRepository contractAppUserRepository;
    private final OpenAiApi openAiApi;

    public AppUserService(AppUserRepository appUserRepository, ContractRepository contractRepository, ContractAppUserRepository contractAppUserRepository, OpenAiApi openAiApi) {
        this.appUserRepository = appUserRepository;
        this.contractRepository = contractRepository;
        this.contractAppUserRepository = contractAppUserRepository;
        this.openAiApi = openAiApi;
    }

    public AppUserEntity getUserById(int id) {
        return appUserRepository.findById(id)
                .map(user -> {
                    user.setPassword("");
                    return user;
                })
                .orElse(null);
    }

    public AppUserEntity registerUser(AppUserEntity tempAppUser) throws JsonProcessingException {
       if (appUserRepository.findByEmail(tempAppUser.getEmail()).isPresent()) {
           return null;
       }

        String idealTextProfile = openAiApi.getIdealProfileForDescription(tempAppUser.getTextCV());

        AppUserEntity newUser = AppUserEntity.builder()
                .name(tempAppUser.getName())
                .email(tempAppUser.getEmail())
                .password(BCrypt.hashpw(tempAppUser.getPassword(), BCrypt.gensalt()))
                .walletEntity(tempAppUser.getWalletEntity())
                .verified(false)
                .textCV(tempAppUser.getTextCV())
                .idealTextProfile(idealTextProfile)
                .build();
       appUserRepository.save(newUser);
       //todo sa facem embeddingul si sa il salvam dupa tot in metoda asta
        return newUser;
    }

    public List<AppUserEntity> findAll() {
        return appUserRepository.findAll().stream()
                .peek(appUserEntity -> appUserEntity.setPassword(""))
                .collect(Collectors.toList());
    }

    public boolean authenticateUser(String email, String password) {
        AppUserEntity user = appUserRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return false;
        }
        return BCrypt.checkpw(password, user.getPassword());
    }


    public AppUserEntity getUserByToken(String token) {
        String email = JwtUtil.getEmailFromToken(token.substring(7), SECRET_KEY);
        return appUserRepository.findByEmail(email).orElse(null);
    }

    public AppUserEntity getUserByEmail(String email) {
        return appUserRepository.findByEmail(email)
                .map(user -> {
                    user.setPassword(null);
                    return user;
                })
                .orElse(null);
    }

    public List<AppUserEntity> getMatchingCvs(int contractId) {


        ContractEntity contract = contractRepository.findById(contractId).get();

        float budget = contract.getBudget();
        List<ContractAppUserEntity> matchingAppUserEntity = contractAppUserRepository.findAllByContractId(contractId);
        List<AppUserEntity> matchingUsers = new ArrayList<>();

        for (ContractAppUserEntity matchingUser : matchingAppUserEntity) {
            matchingUsers.add(appUserRepository.findById(matchingUser.getAppUser().getId()).get());
            if (budget >= matchingUsers.size()) {
                break;
            }
        }
        return matchingUsers;

    }
}
