package com.rdx.rdxserver.services;

import com.rdx.rdxserver.entities.WalletEntity;
import com.rdx.rdxserver.repositories.WalletRepository;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.stream.Collectors;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public WalletEntity createAndSaveWallet() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        String walletAddress =  random.ints(30, 0, chars.length())
                .mapToObj(i -> String.valueOf(chars.charAt(i)))
                .collect(Collectors.joining());
        WalletEntity wallet = WalletEntity.builder()
                        .address(walletAddress)
                        .build();
        return walletRepository.save(wallet);
    }
}
