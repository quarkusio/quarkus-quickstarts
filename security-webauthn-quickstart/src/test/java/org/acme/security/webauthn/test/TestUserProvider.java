package org.acme.security.webauthn.test;

import javax.enterprise.context.ApplicationScoped;

import org.acme.security.webauthn.MyWebAuthnSetup;

import io.quarkus.test.Mock;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.auth.webauthn.Authenticator;

@Mock
@ApplicationScoped
public class TestUserProvider extends MyWebAuthnSetup {
    @Override
    public Uni<Void> updateOrStoreWebAuthnCredentials(Authenticator authenticator) {
        // delegate the scooby user to the manual endpoint, because if we do it here it will be
        // created/udated twice
        if(authenticator.getUserName().equals("scooby"))
            return Uni.createFrom().nullItem();
        return super.updateOrStoreWebAuthnCredentials(authenticator);
    }
}
