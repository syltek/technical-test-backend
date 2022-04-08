package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.api.model.DepositByCreditCardModel;
import com.playtomic.tests.wallet.service.Wallet;
import com.playtomic.tests.wallet.service.WalletBalanceHistory;
import com.playtomic.tests.wallet.service.WalletService;
import com.playtomic.tests.wallet.service.dto.DepositByCreditCardRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController()
@RequestMapping("/api/v1/wallet")
public class WalletController {
    private Logger log = LoggerFactory.getLogger(WalletController.class);

    @Autowired
    WalletService walletService;

    @RequestMapping
    void log() {
        log.info("Logging from /");
    }


    @GetMapping("/{id}")
    ResponseEntity<Wallet> fetch(@PathVariable("id") long id) {
        return ResponseEntity.ok(walletService.fetchOneById(id));
    }

    @GetMapping("/{id}/balance")
    ResponseEntity<BigDecimal> fetchBalance(@PathVariable("id") long id) {
        return ResponseEntity.ok(walletService.fetchCurrentBalanceById(id));
    }

    @PostMapping("/{id}/deposit")
    ResponseEntity<WalletBalanceHistory> deposit(@PathVariable("id") long id, @RequestBody DepositByCreditCardModel model) {
        return ResponseEntity.ok(
                walletService.deposit(new DepositByCreditCardRequest(id, model.getCreditCard(), model.getAmount()))
        );
    }

}
