package com.playtomic.tests.wallet;

import com.playtomic.tests.wallet.domain.TopUpRequest;
import com.playtomic.tests.wallet.domain.Wallet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;

import java.math.BigDecimal;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Possible improvement: Have a separate integration test profile and integration step on the build
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = WalletApplication.class)
@ActiveProfiles(profiles = "test")
@EnableWireMock(
		@ConfigureWireMock(
				port = 9999
		)
)
public class WalletApplicationIT {

	@Value("${stripe.simulator.charges-uri}")
	private String chargesUri;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void getWallet_shouldReturnWallet() {
		ResponseEntity<Wallet> result = restTemplate.getForEntity("/wallet/7ceec1ca-89b1-4f18-a7b4-ef76c6f643b9", Wallet.class);

		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody().getBalance()).isEqualTo(500.0);
	}

	@Test
	public void getWallet_shouldReturnErrorForNotFoundWallet() {
		ResponseEntity<String> result = restTemplate.getForEntity("/wallet/7ceec1ca-89b1-4f18-a7b4-ef76c6f643bT", String.class);

		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		assertThat(result.getBody()).isEqualTo("Resource not found: 7ceec1ca-89b1-4f18-a7b4-ef76c6f643bT");
	}

	@Test
	public void topup_shouldReturnUpdatedWallet() {
		stubFor(post(urlEqualTo("/charges"))
				.willReturn(aResponse()
						.withStatus(HttpStatus.OK.value())
				));

		HttpEntity<TopUpRequest> request = new HttpEntity<>(TopUpRequest.builder().amount(BigDecimal.ONE).creditCardNumber("4242 4242 4242 4242").build());

		ResponseEntity<Wallet> result = restTemplate.exchange(
				"/wallet/7ceec1ca-89b1-4f18-a7b4-ef76c6f643b9/topup",
				HttpMethod.PUT,
				request,
				Wallet.class
		);

		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody().getBalance()).isEqualTo(501.0);
	}

	@Test
	public void topup_shouldReturnValidationErrorForNonPositiveAmount() {
		HttpEntity<TopUpRequest> request = new HttpEntity<>(TopUpRequest.builder().build());

		ResponseEntity<String> result = restTemplate.exchange(
				"/wallet/7ceec1ca-89b1-4f18-a7b4-ef76c6f643b9/topup",
				HttpMethod.PUT,
				request,
				String.class
		);

		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(result.getBody()).contains("Please provide a credit card number");
	}
}
