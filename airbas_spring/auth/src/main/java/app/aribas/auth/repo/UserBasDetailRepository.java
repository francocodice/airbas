package app.aribas.auth.repo;

import app.aribas.auth.model.UserBas;
import app.aribas.auth.model.UserBasDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBasDetailRepository extends JpaRepository<UserBasDetail, Long> {
    UserBasDetail findByUserbas(UserBas userbas);
}
