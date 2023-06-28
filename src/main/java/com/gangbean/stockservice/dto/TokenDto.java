package com.gangbean.stockservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {

    private String accessToken;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String refreshToken;
}