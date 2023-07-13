package com.gangbean.stockservice.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotNull
    @Size(min = 3, max = 50)
    private String username;

    @NotNull
    @Size(min = 3, max = 100)
    private String password;
}