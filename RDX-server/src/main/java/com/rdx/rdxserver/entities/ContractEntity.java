package com.rdx.rdxserver.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "contract")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
    )
    @Access(AccessType.PROPERTY)
    private Integer id;

    //company id sa le leg
    //wallet sa le leg
    // user sa leg ca lista
    //embedding sa le leg
    private String profileText;
    private Boolean paid = false;
    private float budget;
    private LocalDate startDate;
    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "profile_embeddings_id", unique = true, referencedColumnName = "id")
    private EmbeddingsEntity embeddingsEntity;

    @JsonIgnore
    @OneToMany(mappedBy = "contract")
    private Set<ContractAppUserEntity> appUserAssoc;
    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private CompanyEntity companyEntity;

    @OneToOne
    @JoinColumn(name = "wallet_id", referencedColumnName = "id")
    @Cascade(org.hibernate.annotations.CascadeType.PERSIST)
    private WalletEntity walletEntity;

}
