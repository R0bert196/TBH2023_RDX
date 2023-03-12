package com.rdx.rdxserver.repositories;

import com.rdx.rdxserver.entities.ContractAppUserEntity;
import com.rdx.rdxserver.entities.ContractEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContractRepository extends JpaRepository<ContractEntity, Integer> {
    List<ContractEntity> findByCompanyEntity_Id(int companyId);

}
