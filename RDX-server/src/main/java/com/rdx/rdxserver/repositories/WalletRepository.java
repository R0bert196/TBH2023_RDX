package com.rdx.rdxserver.repositories;

import com.rdx.rdxserver.entities.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<WalletEntity, Integer> {

}
