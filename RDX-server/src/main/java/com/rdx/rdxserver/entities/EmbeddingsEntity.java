package com.rdx.rdxserver.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "embeddings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmbeddingsEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
    )
    @Access(AccessType.PROPERTY)
    private Integer id;

    private float[] values;

}
