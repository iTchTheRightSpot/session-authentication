package com.example.sessionauth.session;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.EAGER;

@Table(name = "SPRING_SESSION")
@Entity
@NoArgsConstructor @Getter @Setter @EqualsAndHashCode
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

    @OneToMany(mappedBy = "springSession", fetch = EAGER, cascade = {PERSIST, MERGE, REMOVE}, orphanRemoval = true)
    private Set<SessionAttributes> attributes = new HashSet<>();

    /**
     * Method is needed when a user signs out. It is called in config ->security -> CustomLogoutHandler class
     *
     * @param sessionAttributes
     * @return void
     * */
    public void removeAttribute(SessionAttributes sessionAttributes) {
        this.attributes.remove(sessionAttributes);
        sessionAttributes.setSpringSession(this);
    }

    public void addAttribute(SessionAttributes sessionAttributes) {
        this.attributes.add(sessionAttributes);
        sessionAttributes.setSpringSession(this);
    }

}
