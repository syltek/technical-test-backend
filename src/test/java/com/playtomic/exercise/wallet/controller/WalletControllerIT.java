package com.playtomic.exercise.wallet.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playtomic.exercise.payment.provider.stripe.exception.StripeAmountTooSmallException;
import com.playtomic.exercise.wallet.dto.TopUpRequestDTO;
import com.playtomic.exercise.wallet.dto.WalletResponseDTO;
import com.playtomic.exercise.wallet.exception.WalletNotFoundException;
import com.playtomic.exercise.wallet.service.WalletService;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(WalletController.class)
public class WalletControllerIT {
  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private WalletService walletService;

  @Autowired
  private ObjectMapper objectMapper;

  private UUID walletId = UUID.randomUUID();
  private WalletResponseDTO walletResponseDTO = WalletResponseDTO
    .builder()
    .id(walletId)
    .owner("John Doe")
    .balance(new BigDecimal("100.00"))
    .build();

  @Test
  @DisplayName("Test getting a wallet by ID successfully")
  public void testGetWalletById() throws Exception {
    Mockito
      .when(walletService.getWalletById(walletId))
      .thenReturn(walletResponseDTO);

    mockMvc
      .perform(get("/api/wallets/{id}", walletId))
      .andExpect(status().isOk())
      .andExpect(
        content().json(objectMapper.writeValueAsString(walletResponseDTO))
      );
  }

  @Test
  @DisplayName("Test getting a wallet by ID when it is not found")
  public void testGetWalletById_NotFound() throws Exception {
    Mockito
      .when(walletService.getWalletById(walletId))
      .thenThrow(new WalletNotFoundException("Wallet not found"));

    mockMvc
      .perform(get("/api/wallets/{id}", walletId))
      .andExpect(status().isNotFound())
      .andExpect(content().string("Wallet not found"));
  }

  @Test
  @DisplayName("Test topping up a wallet successfully")
  public void testTopUpWallet() throws Exception {
    final TopUpRequestDTO topUpRequestDTO = TopUpRequestDTO
      .builder()
      .walletId(walletId)
      .creditCardNumber("4111111111111111")
      .amount(new BigDecimal("50.00"))
      .build();

    final WalletResponseDTO updatedWalletResponseDTO = WalletResponseDTO
      .builder()
      .id(walletId)
      .owner("John Doe")
      .balance(new BigDecimal("150.00"))
      .build();

    Mockito
      .when(
        walletService.topUpWallet(
          walletId,
          "4111111111111111",
          new BigDecimal("50.00")
        )
      )
      .thenReturn(updatedWalletResponseDTO);

    mockMvc
      .perform(
        post("/api/wallets/top-up")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(topUpRequestDTO))
      )
      .andExpect(status().isOk())
      .andExpect(
        content()
          .json(objectMapper.writeValueAsString(updatedWalletResponseDTO))
      );
  }

  @Test
  @DisplayName("Test topping up a wallet with an amount too small")
  public void testTopUpWallet_StripeAmountTooSmall() throws Exception {
    final TopUpRequestDTO topUpRequestDTO = TopUpRequestDTO
      .builder()
      .walletId(walletId)
      .creditCardNumber("4111111111111111")
      .amount(new BigDecimal("0.50"))
      .build();

    Mockito
      .when(
        walletService.topUpWallet(
          walletId,
          "4111111111111111",
          new BigDecimal("0.50")
        )
      )
      .thenThrow(new StripeAmountTooSmallException("Amount too small"));

    mockMvc
      .perform(
        post("/api/wallets/top-up")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(topUpRequestDTO))
      )
      .andExpect(status().isUnprocessableEntity())
      .andExpect(content().string("Amount too small"));
  }

  @Test
  @DisplayName("Test topping up a wallet with an invalid request")
  public void testTopUpWallet_InvalidRequest() throws Exception {
    final TopUpRequestDTO topUpRequestDTO = TopUpRequestDTO
      .builder()
      .walletId(walletId)
      .creditCardNumber("1234") // Invalid credit card number
      .amount(new BigDecimal("-50.00")) // Invalid amount
      .build();

    mockMvc
      .perform(
        post("/api/wallets/top-up")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(topUpRequestDTO))
      )
      .andExpect(status().isBadRequest())
      .andExpect(
        content()
          .string(
            "{\"amount\":\"Amount must be greater than zero\",\"creditCardNumber\":\"Credit card number must be between 16 and 19 characters\"}"
          )
      );
  }
}
