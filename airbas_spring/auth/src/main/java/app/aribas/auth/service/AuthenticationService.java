package app.aribas.auth.service;

import app.aribas.auth.model.AuthProvider;
import app.aribas.auth.model.ERole;
import app.aribas.auth.model.UserBas;
import app.aribas.auth.model.UserBasDetail;
import app.aribas.auth.model.utils.LoginRequest;
import app.aribas.auth.repo.UserBasRepository;
import app.aribas.auth.security.JWTAuthenticationManager;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @RequiredArgsConstructor generates a constructor with 1 parameter for each field that requires special handling.
 * All non-initialized final fields get a parameter, as well as any fields that are marked as @NonNull that aren't
 * initialized where they are declared. For those fields marked with @NonNull, an explicit null check is also generated.
 * The constructor will throw a NullPointerException if any of the parameters intended for the fields marked with
 * @NonNull contain null. The order of the parameters match the order in which the fields appear in your class.
 */

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserBasRepository userBasRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTAuthenticationManager jwtAuthenticationManager;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public List<UserBas> findAll(){
        return userBasRepository.findAll();
    }

    public UserBas createUser(LoginRequest request, AuthProvider provider){
        if (userBasRepository.findByEmail(request.getEmail()) != null)
            throw new IllegalArgumentException("Email already exists");

        UserBas newUser = new UserBas();
        UserBasDetail detail = new UserBasDetail();
        //necessario aggiornare entrambe le referenze
        detail.setUserbas(newUser);

        newUser.setEmail(request.getEmail());
        newUser.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        newUser.setRole(ERole.ROLE_USER);
        newUser.setProvider(provider);
        newUser.setUserbasdetail(detail);

        userBasRepository.save(newUser);
        return newUser;
    }

    public UserBas findUser(String email){
        return userBasRepository.findByEmail(email);
    }

    public boolean exisitUser(String email){
        return userBasRepository.findByEmail(email) != null;
    }

    public String authenticateUser(LoginRequest credentials) throws UsernameNotFoundException,
            BadCredentialsException {
        if (findUser(credentials.getEmail()) == null)
            throw new IllegalArgumentException("Not registred");


        //TO-DO Gestire eccezione al login se ci si registra
        // Throws exception if user is not found or credentials are invalid
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    credentials.getEmail(),
                    credentials.getPassword()));
        }catch (Exception e){
            System.out.println("AuthService : " + e.getMessage());
        }
        return jwtAuthenticationManager.generateJwtToken(credentials.getEmail());
    }


}
