package com.playtomic.exercise.payment.provider.stripe.service;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.playtomic.exercise.payment.model.Payment;
import com.playtomic.exercise.payment.provider.stripe.config.StripeProperties;
import com.playtomic.exercise.payment.provider.stripe.exception.StripeServiceException;
import java.math.BigDecimal;
import java.net.URI;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
@ActiveProfiles("test")
public class StripeServiceTest {
  @MockBean
  private RestTemplate restTemplate;

  @Autowired
  private StripeProperties stripeProperties;

  @Autowired
  private StripeService stripeService;

  private static final String CREDIT_CARD_NUMBER = "1234567890123456";
  private static final BigDecimal AMOUNT = new BigDecimal("20.00");
  private static final String PAYMENT_ID = "payment123";

  @Test
  @DisplayName("Test successful charge")
  @SneakyThrows
  void testCharge_Successful() {
    Payment expectedPayment = new Payment("123");
    when(restTemplate.postForObject(any(), any(), any()))
      .thenReturn(expectedPayment);

    Payment result = stripeService.charge(CREDIT_CARD_NUMBER, AMOUNT);

    verify(restTemplate, times(1)).postForObject(any(URI.class), any(), any());
    assertSame(
      expectedPayment,
      result,
      "Expected payment to be the same as the result"
    );
  }

  @Test
  @DisplayName("Test charge with server error and retry")
  void testCharge_ServerError() {
    when(restTemplate.postForObject(any(), any(), any()))
      .thenThrow(new HttpServerErrorException(INTERNAL_SERVER_ERROR));

    assertThrows(
      HttpServerErrorException.class,
      () -> stripeService.charge(CREDIT_CARD_NUMBER, AMOUNT)
    );

    verify(
        restTemplate,
        times(stripeProperties.getRequest().getCharge().getMaxAttempts())
      )
      .postForObject(any(URI.class), any(), any());
  }

  @Test
  @DisplayName("Test successful refund")
  void testRefund_Successful() throws StripeServiceException {
    final ResponseEntity<Object> responseEntity = ResponseEntity.ok().build();
    when(
        restTemplate.postForEntity(
          anyString(),
          any(),
          eq(Object.class),
          eq(PAYMENT_ID)
        )
      )
      .thenReturn(responseEntity);

    stripeService.refund(PAYMENT_ID);

    verify(restTemplate, times(1))
      .postForEntity(anyString(), any(), eq(Object.class), eq(PAYMENT_ID));
  }

  @Test
  @DisplayName("Test refund with server error and retry")
  void testRefund_ServerError() {
    doThrow(new HttpServerErrorException(INTERNAL_SERVER_ERROR))
      .when(restTemplate)
      .postForEntity(anyString(), any(), eq(Object.class), anyString());

    assertThrows(
      HttpServerErrorException.class,
      () -> stripeService.refund(PAYMENT_ID)
    );

    verify(
        restTemplate,
        times(stripeProperties.getRequest().getRefund().getMaxAttempts())
      )
      .postForEntity(anyString(), any(), eq(Object.class), eq(PAYMENT_ID));
  }
}
