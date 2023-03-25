package com.example.sessionauth.session;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.EAGER;

@Table(name = "SPRING_SESSION")
@Entity
@NoArgsConstructor
@Getter
@Setter
public class SpringSession {

    @Id
    @Column(name = "PRIMARY_ID", nullable = false)
    private String id;

    @Column(name = "SESSION_ID", nullable = false, unique = true)
    private String sessionID;

    @Column(name = "CREATION_TIME", nullable = false)
    private Long creationTime;

    @Column(name = "LAST_ACCESS_TIME", nullable = false)
    private Long lastAccessedTime;

    @Column(name = "MAX_INACTIVE_INTERVAL", nullable = false)
    private Long maxInActive;

    @Column(name = "EXPIRY_TIME", nullable = false)
    private Long expiryTime;

    @Column(name = "PRINCIPAL_NAME")
    private String principal;

    @OneToMany(mappedBy = "springSession", fetch = EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SessionAttributes> attributes = new ArrayList<>();

    /**
     * Method is needed when a user signs out. It is called in config ->security -> CustomLogoutHandler class
     *
     * @param sessionAttributes
     * @return void
     * */
    public void removeAttribute(SessionAttributes sessionAttributes) {
        if (attributes.contains(sessionAttributes)) {
            this.attributes.remove(sessionAttributes);
            sessionAttributes.setSpringSession(this);
        }
    }

}
