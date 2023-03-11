package com.rdx.rdxserver.services;

import com.rdx.rdxserver.entities.AppUserEntity;
import com.rdx.rdxserver.repositories.AppUserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class AppUserService {


    private final AppUserRepository appUserRepository;

    public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public AppUserEntity getUserById(int id) {
        return appUserRepository.findById(id)
                .map(user -> {
                    user.setPassword("");
                    return user;
                })
                .orElse(null);
    }

    public AppUserEntity registerUser(AppUserEntity tempAppUser) {
       if (appUserRepository.findByEmail(tempAppUser.getEmail()).isPresent()) {
           return null;
       }
//        WalletEntity wallet = tempAppUser.getWalletEntity();
       // walletService.save(wallet)
        AppUserEntity newUser = AppUserEntity.builder()
                .name(tempAppUser.getName())
                .email(tempAppUser.getEmail())
                .password(BCrypt.hashpw(tempAppUser.getPassword(), BCrypt.gensalt()))
                .walletEntity(tempAppUser.getWalletEntity())
                .verified(false)
                .textCV(tempAppUser.getTextCV())
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
}
