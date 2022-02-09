package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.dto.ChargeRequestDto;
import com.playtomic.tests.wallet.dto.ResponseDto;
import com.playtomic.tests.wallet.dto.StripePaymentDto;
import com.playtomic.tests.wallet.dto.WalletDto;
import com.playtomic.tests.wallet.service.wallet.WalletService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * Updated by Orkun Cavdar on 08/02/2022
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/wallets")
public class WalletController {
    private final WalletService walletService;
    private final Logger log = LoggerFactory.getLogger(WalletController.class);

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    ResponseEntity<WalletDto> getWalletById(@PathVariable("id") Long id) {
        log.info("Incoming request for wallet by id: " + id);
        return new ResponseEntity<>(walletService.getWallet(id), HttpStatus.OK);
    }

    @RequestMapping(value = "/charge/{id}", method = RequestMethod.PATCH, consumes = "application/json", produces = "application/json")
    ResponseEntity<ResponseDto> chargeMoneyWalletByCreditCard(@PathVariable("id") Long id,
                                                              @RequestBody final ChargeRequestDto body) {
        log.info("Incoming request for charging wallet for: " + body);
        StripePaymentDto stripePaymentDto = walletService.chargeMoneyWalletByCreditCard(id, body);
        return new ResponseEntity<>(ResponseDto.builder().code(200).message("Successful Operation").body(new HashMap<>() {{
            put("paymentId", stripePaymentDto.getId());
        }}).build(),
                HttpStatus.OK);
    }

    @RequestMapping(value = "/refund/{paymentId}", method = RequestMethod.GET, produces = "application/json")
    ResponseEntity<ResponseDto> chargeMoneyBackWallet(@PathVariable("paymentId") String paymentId) {
        log.info("Incoming request for charging back payment for: " + paymentId);
        WalletDto walletDto = walletService.chargeMoneyBackWalletByCreditCard(paymentId);
        return new ResponseEntity<>(ResponseDto.builder().code(200).message("Successful Operation").body(new HashMap<>() {{
            put("walletId", String.valueOf(walletDto.getId()));
            put("currentAmount", String.valueOf(walletDto.getBalance()));
        }}).build(),
                HttpStatus.OK);
    }

    @RequestMapping("/")
    void log() {
        log.info("Logging from /");
    }
}
