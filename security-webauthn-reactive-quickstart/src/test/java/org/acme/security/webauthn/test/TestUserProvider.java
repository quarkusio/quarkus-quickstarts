package org.acme.security.webauthn.test;

import org.acme.security.webauthn.MyWebAuthnSetup;
import org.acme.security.webauthn.WebAuthnCredential;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.security.webauthn.WebAuthnCredentialRecord;
import io.quarkus.test.Mock;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@Mock
@ApplicationScoped
public class TestUserProvider extends MyWebAuthnSetup {
    @WithTransaction
    @Override
    public Uni<Void> store(WebAuthnCredentialRecord credentialRecord) {
        // this user is handled in the LoginResource endpoint manually
        if (credentialRecord.getUsername().equals("scooby")) {
            return Uni.createFrom().voidItem();
        }
        return super.store(credentialRecord);
    }

    @WithTransaction
    @Override
    public Uni<Void> update(String credentialId, long counter) {
        return WebAuthnCredential.findByCredentialId(credentialId)
        		.flatMap(credential -> {
        			// this user is handled in the LoginResource endpoint manually
        			if (credential.user.username.equals("scooby")) {
        				return Uni.createFrom().voidItem();
        			}
        			return super.update(credentialId, counter);
        		});
    }

}
