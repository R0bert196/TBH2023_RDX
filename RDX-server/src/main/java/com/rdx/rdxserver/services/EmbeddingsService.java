package com.rdx.rdxserver.services;

import com.rdx.rdxserver.repositories.EmbeddingsRepository;
import org.springframework.stereotype.Service;

@Service
public class EmbeddingsService {

    private final EmbeddingsRepository embeddingsRepository;

    public EmbeddingsService(EmbeddingsRepository embeddingsRepository) {
        this.embeddingsRepository = embeddingsRepository;
    }
}
