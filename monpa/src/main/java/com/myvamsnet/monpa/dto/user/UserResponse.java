package com.myvamsnet.monpa.dto.user;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@JsonPropertyOrder({
        "id",
        "firstName",
        "lastName",
        "countryName",
        "phoneNumber",
        "email",
})
public class UserResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private String CountryName;

    @Pattern(
            regexp = "^\\+?[0-9]{10,15}$",
            message = "Invalid phone number"
    )
    private String phoneNumber;

    private String email;

    // getters and setters
}
