package com.rdx.rdxserver.repositories;

import com.rdx.rdxserver.entities.ContractAppUserEntity;
import com.rdx.rdxserver.entities.primaryKeys.ContractAppUserPk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ContractAppUserRepository extends JpaRepository<ContractAppUserEntity, ContractAppUserPk> {


    @Query(value = "select * from contract_appuser where contract_id = :contractId and cosine_similarity > 0.5",nativeQuery = true)
    List<ContractAppUserEntity> findAllByContractId(int contractId);
}
