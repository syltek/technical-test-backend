package com.playtomic.exercise.wallet.model;

import com.playtomic.exercise.wallet.dto.WalletResponseDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Wallet} and its DTO {@link WalletResponseDTO}.
 */
@Mapper(componentModel = "spring")
public interface WalletMapper {
  /**
   * Converts a {@link Wallet} entity to a {@link WalletResponseDTO}.
   *
   * @param wallet the wallet entity
   * @return the wallet response DTO
   */
  WalletResponseDTO walletToWalletResponseDTO(Wallet wallet);
}
