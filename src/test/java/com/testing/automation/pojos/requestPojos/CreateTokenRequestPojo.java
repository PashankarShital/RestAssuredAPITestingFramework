package com.testing.automation.pojos.requestPojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTokenRequestPojo {
    private String username;
    private String password;
}
