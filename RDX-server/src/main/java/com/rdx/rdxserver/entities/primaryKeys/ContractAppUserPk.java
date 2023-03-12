package com.rdx.rdxserver.entities.primaryKeys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode

public class ContractAppUserPk implements Serializable {

    private Integer appUser;
    private Integer contract;

}
