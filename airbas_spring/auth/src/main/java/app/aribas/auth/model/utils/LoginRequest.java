package app.aribas.auth.model.utils;

import lombok.ToString;

import javax.validation.constraints.Email;

@ToString
public class LoginRequest {
    @Email
    private String email;

    private String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
