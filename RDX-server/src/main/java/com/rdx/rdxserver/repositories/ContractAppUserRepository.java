package com.rdx.rdxserver.repositories;

import com.rdx.rdxserver.entities.ContractAppUserEntity;
import com.rdx.rdxserver.entities.primaryKeys.ContractAppUserPk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractAppUserRepository extends JpaRepository<ContractAppUserEntity, ContractAppUserPk> {
}
