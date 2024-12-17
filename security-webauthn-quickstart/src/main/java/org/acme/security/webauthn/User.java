package org.acme.security.webauthn;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Table(name = "user_table")
@Entity
public class User extends PanacheEntity {

    @Column(unique = true)
    public String username;

    // non-owning side, so we can add more credentials later
    @OneToOne(mappedBy = "user")
    public WebAuthnCredential webAuthnCredential;

    public static User findByUsername(String username) {
        return User.find("username", username).firstResult();
    }
}
