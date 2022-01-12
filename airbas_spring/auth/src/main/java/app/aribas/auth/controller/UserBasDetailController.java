package app.aribas.auth.controller;

import app.aribas.auth.model.AuthProvider;
import app.aribas.auth.model.UserBas;
import app.aribas.auth.model.UserBasDetail;
import app.aribas.auth.model.utils.LoginRequest;
import app.aribas.auth.model.utils.UserPayload;
import app.aribas.auth.service.AuthenticationService;
import app.aribas.auth.service.UserBasDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class UserBasDetailController {
    private final AuthenticationService authenticationService;
    private final UserBasDetailsService userBasDetailsService;


    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserPayload payload) {
        LoginRequest credential = new LoginRequest(payload.getEmail(), payload.getPassword());
        UserBas u;
        try{
            u = authenticationService.createUser(credential, AuthProvider.local);
        } catch (Exception e){
            return new ResponseEntity("Fail creating user : " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        try{
            userBasDetailsService.createDetailsUser(u,payload);
        } catch (Exception e){
            return new ResponseEntity("Fail creating user Detail : " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(payload, HttpStatus.OK);
    }


    @PostMapping("/details")
    public ResponseEntity<?> details(@RequestBody LoginRequest credentials){
        UserBas user = authenticationService.findUser(credentials.getEmail());
        return new ResponseEntity<>(userBasDetailsService.findDetails(user), HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody UserPayload payload){
        UserBas user;
        user = authenticationService.findUser(payload.getEmail());

        if(user != null){
            UserBasDetail userDetail = userBasDetailsService.findDetails(user);
            userBasDetailsService.updateDetailsUser(user,payload);

            return new ResponseEntity<>(userDetail, HttpStatus.OK);
        } else{
            return new ResponseEntity<>("User not found ", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
