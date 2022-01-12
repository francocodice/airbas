package app.aribas.auth.service;

import app.aribas.auth.model.UserBas;
import app.aribas.auth.model.UserBasDetail;
import app.aribas.auth.model.utils.UserPayload;
import app.aribas.auth.repo.UserBasDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserBasDetailsService {
    private final UserBasDetailRepository userBasDetailRepository;

    public UserBasDetail findDetails(UserBas userbas){
        return userBasDetailRepository.findByUserbas(userbas);
    }

    public boolean save(UserBasDetail user){
        userBasDetailRepository.save(user);
        return true;
    }

    public void createDetailsUser(UserBas ref, UserPayload payload){
        UserBasDetail userBasDetail = new UserBasDetail();

        userBasDetail.setBirthdate(payload.getBirthdate());
        userBasDetail.setCreditcard(payload.getCreditcard());
        userBasDetail.setFirstname(payload.getFirstname());
        userBasDetail.setSecondname(payload.getSecondname());
        userBasDetail.setTelephone(payload.getTelephone());
        userBasDetail.setUserbas(ref);

        ref.setUserbasdetail(userBasDetail);
        //userBasRepository.save(ref);
        userBasDetailRepository.save(userBasDetail);
    }

    public boolean updateDetailsUser(UserBas ref, UserPayload payload){
        UserBasDetail userBasDetail = userBasDetailRepository.findByUserbas(ref);

        if(!payload.getTelephone().isBlank()){
            userBasDetail.setTelephone(payload.getTelephone());
        }
        if(!payload.getCreditcard().isBlank()){
            userBasDetail.setCreditcard(payload.getCreditcard());
        }
        if(payload.getBirthdate() != null){
            userBasDetail.setBirthdate(payload.getBirthdate());
        }
        if(!payload.getFirstname().isBlank()){
            userBasDetail.setFirstname(payload.getFirstname());
        }
        if(!payload.getSecondname().isBlank()){
            userBasDetail.setFirstname(payload.getSecondname());
        }

        userBasDetailRepository.save(userBasDetail);
        return true;
    }

}
