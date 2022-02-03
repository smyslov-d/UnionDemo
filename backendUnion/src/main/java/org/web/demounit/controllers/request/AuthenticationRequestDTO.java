package org.web.demounit.controllers.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import javax.validation.constraints.Email;

@Data
public class AuthenticationRequestDTO {

    @Email(message = "is invalid")
    @NotEmpty(message = "can't be blank")
    @Size(min = 3, message = "is too short (min is 3 characters)")
    @Size(max = 20, message = "is too long (maz is 16 characters)")
    private String email;

    @NotEmpty(message = "can't be blank")
    @Size(min = 3, message = "is too short (min is 3 characters)")
    @Size(max = 20, message = "is too long (maz is 16 characters)")
    private String password;

    private String username;
}
