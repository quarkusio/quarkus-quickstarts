package org.acme.security.webauthn;

import java.util.List;
import java.util.UUID;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.security.webauthn.WebAuthnCredentialRecord;
import io.quarkus.security.webauthn.WebAuthnCredentialRecord.RequiredPersistedData;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class WebAuthnCredential extends PanacheEntityBase {
    
	@Id
    public String credentialId;

    public byte[] publicKey;
    public long publicKeyAlgorithm;
    public long counter;
    public UUID aaguid;
    
    // owning side
    @OneToOne
    public User user;

    public WebAuthnCredential() {
    }
    
    public WebAuthnCredential(WebAuthnCredentialRecord credentialRecord, User user) {
        RequiredPersistedData requiredPersistedData = credentialRecord.getRequiredPersistedData();
        aaguid = requiredPersistedData.aaguid();
        counter = requiredPersistedData.counter();
        credentialId = requiredPersistedData.credentialId();
        publicKey = requiredPersistedData.publicKey();
        publicKeyAlgorithm = requiredPersistedData.publicKeyAlgorithm();
        this.user = user;
        user.webAuthnCredential = this;
    }

    public WebAuthnCredentialRecord toWebAuthnCredentialRecord() {
        return WebAuthnCredentialRecord
                .fromRequiredPersistedData(
                        new RequiredPersistedData(user.username, credentialId, aaguid, publicKey, publicKeyAlgorithm, counter));
    }

    public static List<WebAuthnCredential> findByUsername(String username) {
        return list("user.username", username);
    }
    
    public static WebAuthnCredential findByCredentialId(String credentialId) {
        return findById(credentialId);
    }
}
