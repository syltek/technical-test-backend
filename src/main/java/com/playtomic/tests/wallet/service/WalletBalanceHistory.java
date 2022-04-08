package com.playtomic.tests.wallet.service;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Entity
@Table(indexes = {@Index(columnList = "walletId, createdDate desc")})
public class WalletBalanceHistory extends AuditableEntity<Long> {

    Long walletId;

    BigDecimal oldBalance;

    BigDecimal newBalance;

    WalletBalanceChangeReasonEnum changeReason;

    String relatedPaymentId;

    String changeNote;

}
