package com.havving.tutorial.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author HAVVING
 * @since 2021-08-25
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

    @NotNull
    @Size(min = 3, max = 50)
    private String userName;

    @NotNull
    @Size(min = 3, max = 100)
    private String password;
}
