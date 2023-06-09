package com.rdx.rdxserver.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rdx.rdxserver.apis.OpenAiApi;
import com.rdx.rdxserver.entities.AppUserEntity;
import com.rdx.rdxserver.entities.ContractAppUserEntity;
import com.rdx.rdxserver.entities.ContractEntity;
import com.rdx.rdxserver.entities.EmbeddingsEntity;
import com.rdx.rdxserver.repositories.AppUserRepository;
import com.rdx.rdxserver.repositories.ContractAppUserRepository;
import com.rdx.rdxserver.repositories.ContractRepository;
import com.rdx.rdxserver.utils.JwtUtil;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class AppUserService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;


    private final AppUserRepository appUserRepository;
    private final ContractRepository contractRepository;
    private final ContractAppUserRepository contractAppUserRepository;
    private final OpenAiApi openAiApi;
    private final EmbeddingsService embeddingsService;

    public AppUserService(AppUserRepository appUserRepository, ContractRepository contractRepository, ContractAppUserRepository contractAppUserRepository, OpenAiApi openAiApi, EmbeddingsService embeddingsService) {
        this.appUserRepository = appUserRepository;
        this.contractRepository = contractRepository;
        this.contractAppUserRepository = contractAppUserRepository;
        this.openAiApi = openAiApi;
        this.embeddingsService = embeddingsService;
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

        float[] textEmbeddings = openAiApi.getTextEmbeddings(idealTextProfile);
        EmbeddingsEntity savedEmbeddings = embeddingsService.createAndSaveEmbeddings(textEmbeddings);

        AppUserEntity newUser = AppUserEntity.builder()
                .name(tempAppUser.getName())
                .email(tempAppUser.getEmail())
                .password(BCrypt.hashpw(tempAppUser.getPassword(), BCrypt.gensalt()))
                .walletEntity(tempAppUser.getWalletEntity())
                .verified(false)
                .textCV(tempAppUser.getTextCV())
                .idealTextProfile(idealTextProfile)
                .embeddingsEntity(savedEmbeddings)
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
        List<ContractAppUserEntity> matchingAppUserEntity = contractAppUserRepository.findByContract_Id(contractId);
        List<AppUserEntity> matchingUsers = new ArrayList<>();

        for (ContractAppUserEntity matchingUser : matchingAppUserEntity) {
            Optional<AppUserEntity> optionalFetchedUser = appUserRepository.findById(matchingUser.getAppUser().getId());
            if (optionalFetchedUser.isEmpty()) continue;
            AppUserEntity savedUser = new AppUserEntity();
            savedUser.setEmail(optionalFetchedUser.get().getEmail());
            savedUser.setName(optionalFetchedUser.get().getName());
            savedUser.setTextCV(optionalFetchedUser.get().getTextCV());
            savedUser.setVerified(optionalFetchedUser.get().getVerified());
            savedUser.setPhoneNr(optionalFetchedUser.get().getPhoneNr());
            matchingUsers.add(savedUser);
            if (budget >= matchingUsers.size()) {
                break;
            }
        }
        return matchingUsers;

    }

    public void generateEmbeddingForEveryone(EmbeddingsEntity embeddings) {
        float[] contractEmbeddings = embeddings.getValues();
        List<AppUserEntity> userListEmbeddings = this.findAll();

        //adds an embedding score to each user id corelated to each contract id

        for (AppUserEntity appUser : userListEmbeddings) {
            ContractAppUserEntity contractAppUserEntity = contractAppUserRepository.findByAppUser_Id(appUser.getId()).get();
            float[] currUserEmbeddings = appUser.getEmbeddingsEntity().getValues();
            float v = cosineSimilarity(contractEmbeddings, currUserEmbeddings);
            contractAppUserEntity.setCosineSimilarity(v);
            contractAppUserRepository.save(contractAppUserEntity);
        }



    }


    // Method to calculate cosine similarity between two vectors
    public static float cosineSimilarity(float[] vectorA, float[] vectorB) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += Math.pow(vectorA[i], 2);
            normB += Math.pow(vectorB[i], 2);
        }
        return (float) (dotProduct / (Math.sqrt(normA) * Math.sqrt(normB)));
    }


    public List<AppUserEntity> getAppUsersForContract(Integer id) {
        return appUserRepository.findByCompanyAssoc_Contract_Id(id);
    }

    public AppUserEntity save(AppUserEntity user) {return appUserRepository.save(user);
    }



    public List<AppUserEntity> getAppUsersForcompany(Integer companyId) {return appUserRepository.getAppUsersForcompany(companyId);};

}
