package com.rdx.rdxserver.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "wallet")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
    )
    @Access(AccessType.PROPERTY)
    private Integer id;

    private String address;


}
