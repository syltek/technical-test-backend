package com.playtomic.exercise.payment.provider.stripe.service;

import com.playtomic.exercise.payment.provider.stripe.exception.StripeAmountTooSmallException;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

/**
 * Error handler for handling Stripe REST template responses.
 */
@Component
public class StripeRestTemplateResponseErrorHandler
  implements ResponseErrorHandler {

  /**
   * Determines whether the given response has any errors (4xx or 5xx HTTP status codes).
   *
   * @param response the {@link ClientHttpResponse} to check for errors
   * @return {@code true} if the response has errors; {@code false} otherwise
   * @throws IOException if an I/O error occurs
   */
  @Override
  public boolean hasError(ClientHttpResponse response) throws IOException {
    return (
      response.getStatusCode().is5xxServerError() ||
      response.getStatusCode().is4xxClientError()
    );
  }

  /**
   * Handles errors in the given response. If the status code is 422 UNPROCESSABLE ENTITY,
   * it throws a {@link StripeAmountTooSmallException}.
   *
   * @param response the {@link ClientHttpResponse} to handle
   * @throws IOException if an I/O error occurs
   * @throws StripeAmountTooSmallException if the status code is 422 UNPROCESSABLE ENTITY
   */
  @Override
  public void handleError(ClientHttpResponse response) throws IOException {
    if (response.getStatusCode() == HttpStatus.UNPROCESSABLE_ENTITY) {
      throw new StripeAmountTooSmallException(
        "Stripe payment amount is too small"
      );
    }
  }
}
