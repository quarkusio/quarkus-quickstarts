package org.acme.security.webauthn;

import java.util.List;
import java.util.UUID;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.quarkus.security.webauthn.WebAuthnCredentialRecord;
import io.quarkus.security.webauthn.WebAuthnCredentialRecord.RequiredPersistedData;
import io.smallrye.mutiny.Uni;
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
    
    // this is the owning side
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

    public static Uni<List<WebAuthnCredential>> findByUsername(String username) {
        return list("user.username", username);
    }
    
    public static Uni<WebAuthnCredential> findByCredentialId(String credentialId) {
        return findById(credentialId);
    }

    public <T> Uni<T> fetch(T association) {
        return getSession().flatMap(session -> session.fetch(association));
    }
}
