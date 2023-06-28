package com.gangbean.stockservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gangbean.stockservice.entity.Member;
import javax.validation.constraints.Pattern;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;

    @NotNull
    @Size(min = 3, max = 50)
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    @Size(min = 3, max = 100)
    private String password;

    @NotNull
    @Size(min = 3, max = 50)
    private String nickname;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    @Pattern(regexp = "^[0-9]{8}$", message = "생일은 8자리 숫자가 입력되어야 합니다.")
    private String birthday;

    private Set<AuthorityDto> authorityDtoSet;

    public static UserDto from(Member member) {
        if(member == null) return null;

        return UserDto.builder()
            .id(member.getId())
            .username(member.getUsername())
            .nickname(member.getNickname())
            .authorityDtoSet(member.getAuthorities().stream()
                .map(authority -> AuthorityDto.builder().authorityName(authority.getAuthorityName()).build())
                .collect(Collectors.toSet()))
            .build();
    }
}