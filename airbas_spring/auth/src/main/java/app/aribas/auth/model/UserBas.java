package app.aribas.auth.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "userbas")
public class UserBas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="email")
    private String email;

    @Column(name="password")
    private String password;

    @Column(name="provider")
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @Column(name="usename")
    private String username;

    @Column(name="role")
    @Enumerated(EnumType.STRING)
    private ERole role;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userbasdetailId")
    private UserBasDetail detail;

    public UserBas(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserBas() { }

}
