package com.rdx.rdxserver.repositories;

import com.rdx.rdxserver.entities.ContractEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<ContractEntity, Integer> {
    List<ContractEntity> findByCompanyEntity_Id(int companyId);
}
