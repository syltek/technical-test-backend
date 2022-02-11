package com.playtomic.tests.wallet;

import com.playtomic.tests.wallet.web.controller.WalletController;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles(profiles = "test")
class WalletApplicationIT {

    @Test
    void contextLoads(ApplicationContext context) {
        assertNotNull(context);
        WalletController walletController = (WalletController) context.getBean("walletController");
        assertNotNull(walletController);
    }
}
