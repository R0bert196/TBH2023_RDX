package com.rdx.rdxserver.repositories;

import com.rdx.rdxserver.entities.ContractAppUserEntity;
import com.rdx.rdxserver.entities.ContractEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContractRepository extends JpaRepository<ContractEntity, Integer> {
    List<ContractEntity> findByCompanyEntity_Id(int companyId);

    @Query(value = "select * from contract inner join contract_appuser ca on contract.id = ca.contract_id where ca.user_id = :userId;", nativeQuery = true)
    List<ContractEntity> findAllByUserId(int userId);

}
