package com.rdx.rdxserver.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.LinkedHashSet;
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
    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "profile_embeddings_id", unique = true, referencedColumnName = "id")
    private EmbeddingsEntity embeddingsEntity;

    @ManyToMany
    @JoinTable(name = "contract_app_user",
            joinColumns = @JoinColumn(name = "contract_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "app_user_id", referencedColumnName = "id"))
    private Set<AppUserEntity> appUserEntities = new LinkedHashSet<>();

    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private CompanyEntity companyEntity;

    @OneToOne
    @JoinColumn(name = "wallet_id", referencedColumnName = "id")
    private WalletEntity walletEntity;

}
