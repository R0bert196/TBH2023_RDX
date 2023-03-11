package com.rdx.rdxserver.entities;

import com.rdx.rdxserver.converters.FloatArrayConverter;
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

    @Convert(converter = FloatArrayConverter.class)
    @Column(length = 483647)
    private float[] values;

}
