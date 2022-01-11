package app.aribas.auth.repo;

import app.aribas.auth.model.UserBas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBasRepository extends JpaRepository<UserBas, Long> {
    UserBas findByEmail(String email);
}
