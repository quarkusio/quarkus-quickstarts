package org.acme.security.webauthn.test;

import org.acme.security.webauthn.MyWebAuthnSetup;
import org.acme.security.webauthn.WebAuthnCredential;

import io.quarkus.security.webauthn.WebAuthnCredentialRecord;
import io.quarkus.test.Mock;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@Mock
@ApplicationScoped
public class TestUserProvider extends MyWebAuthnSetup {
    @Transactional
    @Override
    public Uni<Void> store(WebAuthnCredentialRecord credentialRecord) {
        // this user is handled in the LoginResource endpoint manually
        if (credentialRecord.getUsername().equals("scooby")) {
            return Uni.createFrom().voidItem();
        }
        return super.store(credentialRecord);
    }

    @Transactional
    @Override
    public Uni<Void> update(String credentialId, long counter) {
        WebAuthnCredential credential = WebAuthnCredential.findByCredentialId(credentialId);
        // this user is handled in the LoginResource endpoint manually
        if (credential.user.username.equals("scooby")) {
        	return Uni.createFrom().voidItem();
        }
        return super.update(credentialId, counter);
    }

}
