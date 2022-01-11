package app.aribas.auth.controller;

import app.aribas.auth.model.AuthProvider;
import app.aribas.auth.model.UserBas;
import app.aribas.auth.model.utils.LoginRequest;
import app.aribas.auth.model.utils.TokenDto;
import app.aribas.auth.service.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OauthController {
    private final AuthenticationService authenticationService;

    @Value("${spring.security.oauth2.client.registration.google.clientId}")
    String googleClientId;

    @Value("${spring.security.oauth2.client.registration.facebook.clientId}")
    String facebookClientId;

    @Value("${security.pswExtUser}")
    String pswExtUser;


    @PostMapping("/google")
    public ResponseEntity<?> google(@RequestBody TokenDto tokenGoogle) throws IOException {
        final NetHttpTransport netHttpTransport = new NetHttpTransport();
        final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

        GoogleIdTokenVerifier.Builder verifier
                = new GoogleIdTokenVerifier.Builder(netHttpTransport, JSON_FACTORY)
                .setAudience(Collections.singleton(googleClientId));

        final GoogleIdToken googleToken = GoogleIdToken.parse(verifier.getJsonFactory(), tokenGoogle.getValue());
        final GoogleIdToken.Payload payload = googleToken.getPayload();

        UserBas currentUser = new UserBas();
        if(!authenticationService.exisitUser(payload.getEmail())){
            currentUser.setEmail(payload.getEmail());
            currentUser.setPassword(pswExtUser);
            System.out.println(payload.get("name"));
            currentUser.setUsername((String)payload.get("name"));
            authenticationService.createUser(currentUser, AuthProvider.google);
        }

        String jwt = authenticationService.authenticateUser(new LoginRequest(payload.getEmail(), pswExtUser));
        TokenDto tokenDto = new TokenDto();
        tokenDto.setValue(jwt);
        return new ResponseEntity<>(tokenDto,HttpStatus.OK);
    }

    @PostMapping("/facebook")
    public ResponseEntity<?> facebook(@RequestBody TokenDto tokenFacebook) throws IOException {
        System.out.println("facebook method");
        Facebook facebook = new FacebookTemplate(tokenFacebook.getValue());
        final String [] fields = {"email", "name"};
        User user = facebook.fetchObject("me", User.class, fields);

        UserBas currentUser = new UserBas();
        if(!authenticationService.exisitUser(user.getEmail())){
            currentUser.setEmail(user.getEmail());
            currentUser.setPassword(pswExtUser);
            currentUser.setUsername(user.getName());

            authenticationService.createUser(currentUser, AuthProvider.facebook);
        }

        String jwt = authenticationService.authenticateUser(new LoginRequest(user.getEmail(), pswExtUser));
        TokenDto tokenDto = new TokenDto();
        tokenDto.setValue(jwt);
        return new ResponseEntity(tokenDto, HttpStatus.OK);
    }


    @PostMapping("/amazon")
    public ResponseEntity<?> amazon(@RequestBody TokenDto tokenAmazon) throws IOException {
        HttpGet request = new HttpGet("https://api.amazon.com/user/profile");
        request.addHeader("Authorization", "bearer " + tokenAmazon.getValue());

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(request);
        HttpEntity entity = response.getEntity();

        String result = EntityUtils.toString(entity);
        Map<String,String> detailUser =
                new ObjectMapper().readValue(result, HashMap.class);
        System.out.println(detailUser);

        UserBas currentUser = new UserBas();
        if(!authenticationService.exisitUser(detailUser.get("email"))){
            currentUser.setEmail(detailUser.get("email"));
            currentUser.setUsername(detailUser.get("name"));
            currentUser.setPassword(pswExtUser);

            authenticationService.createUser(currentUser, AuthProvider.github);
        }

        String jwt = authenticationService.authenticateUser(new LoginRequest(detailUser.get("email"), pswExtUser));

        TokenDto tokenDto = new TokenDto();
        tokenDto.setValue(jwt);
        return new ResponseEntity(tokenDto, HttpStatus.OK);

    }




}
