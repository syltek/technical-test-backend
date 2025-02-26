package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.domain.Wallet;
import com.playtomic.tests.wallet.service.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * possible enhancement: create a specific wallet response class to detached from the domain object.
 */
@RestController
public class WalletController {
    private Logger log = LoggerFactory.getLogger(WalletController.class);

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @RequestMapping("/")
    void log() {
        log.info("Logging from /");
    }

    @GetMapping("/wallet/{wallet_id}")
    public ResponseEntity<Wallet> getWallet(@PathVariable String wallet_id) {
        return ResponseEntity.ok(
                this.walletService.getWallet(wallet_id)
        );
    }
}
