package com.playtomic.tests.wallet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playtomic.tests.wallet.services.wallet.WalletService;
import com.playtomic.tests.wallet.web.controller.WalletController;
import com.playtomic.tests.wallet.web.model.TopUpDto;
import com.playtomic.tests.wallet.web.model.WalletDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.playtomic.tests.wallet.WalletTestUtil.createValidTopUpDto;
import static com.playtomic.tests.wallet.WalletTestUtil.createValidWalletDto;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(WalletController.class)
class WalletControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private WalletService mockWalletService;

    @Test
    void getWalletTest() throws Exception {
        WalletDto walletDto = createValidWalletDto();

        Mockito.when(mockWalletService.getWalletById(walletDto.getId())).thenReturn(walletDto);

        mockMvc.perform(get("/api/v1/wallet/" + walletDto.getId().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(walletDto.getId().toString())));
    }

    @Test
    void createNewWalletTest() throws Exception {
        WalletDto walletDto = createValidWalletDto();
        String walletDtoJson = objectMapper.writeValueAsString(walletDto);

        mockMvc.perform(post("/api/v1/wallet/create-with-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(walletDtoJson))
                .andExpect(status().isCreated());
    }


    @Test
    void topUptoWalletTest() throws Exception {
        WalletDto walletDto = createValidWalletDto();
        TopUpDto topUpDto = createValidTopUpDto(walletDto);

        String topUpDtoJson = objectMapper.writeValueAsString(topUpDto);
        mockMvc.perform(patch("/api/v1/wallet/top-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(topUpDtoJson))
                .andExpect(status().isOk());
    }


}
