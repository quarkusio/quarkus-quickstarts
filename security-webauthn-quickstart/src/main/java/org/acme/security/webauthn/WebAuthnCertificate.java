package org.acme.security.webauthn;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;


@Entity
public class WebAuthnCertificate extends PanacheEntity {
    
    @ManyToOne
    public WebAuthnCredential webAuthnCredential;
    
    /**
     * The list of X509 certificates encoded as base64url.
     */
    public String x5c;
}
