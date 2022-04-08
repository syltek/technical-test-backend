package com.playtomic.tests.wallet.service;


import lombok.Data;

import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
@Data
public class Wallet extends AuditableEntity<Long> {

    private Long ownerId;

    private BigDecimal currentBalance;
}
