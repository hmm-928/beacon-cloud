package com.hmm.webmaster.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author hmm
 * @description
 */
@Data
public class UserDTO {

    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String captcha;

    private Boolean rememberMe = false;


}
