package com.playtomic.tests.wallet;

import com.playtomic.tests.wallet.entity.Wallet;
import com.playtomic.tests.wallet.enums.Currency;
import com.playtomic.tests.wallet.repository.WalletRepository;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * Updated by Orkun Cavdar on 08/02/2022
 */
@SpringBootApplication
@EnableJpaRepositories("com.playtomic.tests.wallet.*")
@EntityScan("com.playtomic.tests.wallet.*")
@ComponentScan(basePackages = {"com.playtomic.tests.wallet.*"})
public class WalletApplication {

    public static void main(String[] args) {
        SpringApplication.run(WalletApplication.class, args);
    }

    @Bean
    public CommandLineRunner loadData(WalletRepository walletRepository) {
        return args -> {
            List<Wallet> walletList =
                    Arrays.asList(Wallet.builder().currency(Currency.USD).balance(BigDecimal.valueOf(100)).iban("ES2604879457091371855657").build(),
                            Wallet.builder().currency(Currency.USD).balance(BigDecimal.valueOf(100)).iban("ES0521005134909988165288").build(),
                            Wallet.builder().currency(Currency.EUR).balance(BigDecimal.valueOf(200)).iban("ES5420955393601824236215").build(),
                            Wallet.builder().currency(Currency.EUR).balance(BigDecimal.valueOf(200)).iban("ES6830047695641298588925").build(),
							Wallet.builder().currency(Currency.EUR).balance(BigDecimal.valueOf(300)).iban("ES2700816212137522522156").build());
            walletRepository.saveAll(walletList);
        };
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }
}
