package com.rdx.rdxserver.repositories;

import com.rdx.rdxserver.entities.EmbeddingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmbeddingsRepository extends JpaRepository<EmbeddingsEntity, Integer> {
}
