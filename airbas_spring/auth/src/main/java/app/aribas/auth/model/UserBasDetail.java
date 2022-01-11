package app.aribas.auth.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "userbasdetail")
public class UserBasDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="firstname")
    private String firstname;

    @Column(name="secondname")
    private String secondname;

    @Column(name="birthdate")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date birthdate;

    @Column(name="creditcard")
    private Date creditcard;

}
