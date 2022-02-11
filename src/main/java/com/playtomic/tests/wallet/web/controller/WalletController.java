package com.playtomic.tests.wallet.web.controller;

import com.playtomic.tests.wallet.services.wallet.WalletService;
import com.playtomic.tests.wallet.web.model.TopUpDto;
import com.playtomic.tests.wallet.web.model.WalletDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wallet")
public class WalletController {

    private final WalletService walletService;


    @GetMapping("/{walletId}")
    public ResponseEntity<WalletDto> getWalletById(@NonNull @PathVariable("walletId") UUID walletId) {
        return new ResponseEntity<>(walletService.getWalletById(walletId), HttpStatus.OK);
    }

    @PostMapping("/create-with-user")
    public ResponseEntity<WalletDto> createWalletByUserId(@RequestBody WalletDto walletDto) {
        return new ResponseEntity<>(walletService.createWalletByUserId(walletDto.getUserId()), HttpStatus.CREATED);
    }

    @PatchMapping("/top-up")
    public ResponseEntity<WalletDto> topUptoWallet(@RequestBody TopUpDto topUpDto) {
        return new ResponseEntity<>(walletService.topUpAmountToWallet(topUpDto), HttpStatus.OK);
    }
}
