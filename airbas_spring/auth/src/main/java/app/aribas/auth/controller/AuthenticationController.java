package app.aribas.auth.controller;

import app.aribas.auth.model.AuthProvider;
import app.aribas.auth.model.UserBas;
import app.aribas.auth.model.utils.LoginRequest;
import app.aribas.auth.security.JWTAuthenticationManager;
import app.aribas.auth.service.AuthenticationService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final JWTAuthenticationManager jwtAuthenticationManager;
    private final AuthenticationService authenticationService;


    @GetMapping("/users")
    public ResponseEntity<?> users(){
        return new ResponseEntity(authenticationService.findAll(), HttpStatus.OK);
    }


    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserBas userBas) {
        try{
            authenticationService.createUser(userBas, AuthProvider.local);
        } catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        String jwt = jwtAuthenticationManager.generateJwtToken(userBas.getEmail());
        return new ResponseEntity(jwt, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> logIn(@RequestBody LoginRequest loginReq){
        System.out.println(loginReq.getPassword());
        String jwt;
        try {
            jwt = authenticationService.authenticateUser(loginReq);
        } catch (Exception e) {
            //throw new IllegalArgumentException(e.getMessage());
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(jwt, HttpStatus.OK);
    }

}
