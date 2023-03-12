package com.rdx.rdxserver.repositories;

import com.rdx.rdxserver.entities.AppUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUserEntity, Integer> {

    Optional<AppUserEntity> findByEmail(String email);

    Optional<AppUserEntity> findByEmailAndPassword(String email, String password);

    List<AppUserEntity> findByCompanyAssoc_Contract_Id(Integer contractId);
}
