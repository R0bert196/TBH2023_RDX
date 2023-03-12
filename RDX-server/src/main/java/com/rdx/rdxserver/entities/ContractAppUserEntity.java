package com.rdx.rdxserver.entities;

import com.rdx.rdxserver.entities.primaryKeys.ContractAppUserPk;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "contract_appuser")
@IdClass(ContractAppUserPk.class)
public class ContractAppUserEntity {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private AppUserEntity appUser;

    @Id
    @ManyToOne
    @JoinColumn(name = "contract_id", referencedColumnName = "id")
    private ContractEntity contract;

    @Column(columnDefinition = "numeric")
    private Float cosineSimilarity;
}
