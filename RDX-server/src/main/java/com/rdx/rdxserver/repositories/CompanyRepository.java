package com.rdx.rdxserver.repositories;

import com.rdx.rdxserver.entities.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<CompanyEntity, Integer> {
}
