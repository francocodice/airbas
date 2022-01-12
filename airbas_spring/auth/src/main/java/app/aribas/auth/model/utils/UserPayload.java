package app.aribas.auth.model.utils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class UserPayload {
    private String email;
    private String password;
    private String firstname;
    private String secondname;
    private Date birthdate;
    private String creditcard;
    private String telephone;
}
