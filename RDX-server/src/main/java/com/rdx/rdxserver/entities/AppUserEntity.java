package com.rdx.rdxserver.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "app_user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUserEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
    )
    @Access(AccessType.PROPERTY)
    private Integer id;
    private String name;
    private String email;
    private String password;
    private String textCV;
    private Boolean verified;
    private String phoneNr;

    @Column(length = 2000)
    private String idealTextProfile;

    @JsonIgnore
    @OneToMany(mappedBy = "appUser")
    private Set<ContractAppUserEntity> companyAssoc;


    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "embeddings_cv_id", referencedColumnName = "id")
    private EmbeddingsEntity embeddingsEntity;

    @ManyToOne
    @JoinColumn(name = "wallet_id", referencedColumnName = "id")
    @Cascade(org.hibernate.annotations.CascadeType.PERSIST)
    private WalletEntity walletEntity;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "company_id")
    private CompanyEntity companyEntity;

}
