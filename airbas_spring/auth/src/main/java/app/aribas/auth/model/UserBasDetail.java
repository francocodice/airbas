package app.aribas.auth.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@Table(name = "userbasdetail")
public class UserBasDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@Column(name="firstname")
    private String firstname;

    //@Column(name="secondname")
    private String secondname;

    //@Column(name="birthdate")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date birthdate;

    //@Column(name="creditcard")
    private String creditcard;

    //@Column(name="telephone")
    private String telephone;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userbas_id", nullable = false)
    @JsonBackReference
    private UserBas userbas;

}
