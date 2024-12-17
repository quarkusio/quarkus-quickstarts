package org.acme.security.webauthn;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import io.quarkus.security.webauthn.WebAuthnCredentialRecord;
import io.quarkus.security.webauthn.WebAuthnUserProvider;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@Blocking
@ApplicationScoped
public class MyWebAuthnSetup implements WebAuthnUserProvider {

    @Transactional
    @Override
    public Uni<List<WebAuthnCredentialRecord>> findByUsername(String userId) {
        return Uni.createFrom().item(WebAuthnCredential.findByUsername(userId).stream().map(WebAuthnCredential::toWebAuthnCredentialRecord).toList());
    }

    @Transactional
    @Override
    public Uni<WebAuthnCredentialRecord> findByCredentialId(String credId) {
        WebAuthnCredential creds = WebAuthnCredential.findByCredentialId(credId);
        if(creds == null)
            return Uni.createFrom().failure(new RuntimeException("No such credential ID"));
        return Uni.createFrom().item(creds.toWebAuthnCredentialRecord());
    }

    @Transactional
    @Override
    public Uni<Void> store(WebAuthnCredentialRecord credentialRecord) {
        User newUser = new User();
        newUser.username = credentialRecord.getUsername();
        WebAuthnCredential credential = new WebAuthnCredential(credentialRecord, newUser);
        credential.persist();
        newUser.persist();
        return Uni.createFrom().voidItem();
    }

    @Transactional
    @Override
    public Uni<Void> update(String credentialId, long counter) {
        WebAuthnCredential credential = WebAuthnCredential.findByCredentialId(credentialId);
        credential.counter = counter;
        return Uni.createFrom().voidItem();
    }

    @Override
    public Set<String> getRoles(String userId) {
        if(userId.equals("admin")) {
            return Set.of("user", "admin");
        }
        return Collections.singleton("user");
    }
}
