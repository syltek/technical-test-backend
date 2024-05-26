package com.playtomic.exercise.payment.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
public class Payment {
  @NonNull
  private String id;

  @JsonCreator
  public Payment(@JsonProperty(value = "id", required = true) String id) {
    this.id = id;
  }
}
