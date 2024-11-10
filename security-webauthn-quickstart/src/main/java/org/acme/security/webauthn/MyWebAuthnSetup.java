package org.acme.security.webauthn;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.context.ApplicationScoped;

import io.quarkus.security.webauthn.WebAuthnUserProvider;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.auth.webauthn.AttestationCertificates;
import io.vertx.ext.auth.webauthn.Authenticator;
import jakarta.transaction.Transactional;

import static org.acme.security.webauthn.WebAuthnCredential.findByCredID;
import static org.acme.security.webauthn.WebAuthnCredential.findByUserName;

@Blocking
@ApplicationScoped
public class MyWebAuthnSetup implements WebAuthnUserProvider {

    @Transactional
    @Override
    public Uni<List<Authenticator>> findWebAuthnCredentialsByUserName(String userName) {
        return Uni.createFrom().item(toAuthenticators(findByUserName(userName)));
    }

    @Transactional
    @Override
    public Uni<List<Authenticator>> findWebAuthnCredentialsByCredID(String credID) {
        return Uni.createFrom().item(toAuthenticators(findByCredID(credID)));
    }

    @Transactional
    @Override
    public Uni<Void> updateOrStoreWebAuthnCredentials(Authenticator authenticator) {
        // leave the scooby user to the manual endpoint, because if we do it here it will be created/updated twice
        if(!authenticator.getUserName().equals("scooby")) {
            User user = User.findByUserName(authenticator.getUserName());
            if(user == null) {
                // new user
                User newUser = new User();
                newUser.userName = authenticator.getUserName();
                WebAuthnCredential credential = new WebAuthnCredential(authenticator, newUser);
                credential.persist();
                newUser.persist();
            } else {
                // existing user
                user.webAuthnCredential.counter = authenticator.getCounter();
            }
        }
        return Uni.createFrom().nullItem();
    }

    private static List<Authenticator> toAuthenticators(List<WebAuthnCredential> dbs) {
        return dbs.stream().map(MyWebAuthnSetup::toAuthenticator).collect(Collectors.toList());
    }

    private static Authenticator toAuthenticator(WebAuthnCredential credential) {
        Authenticator ret = new Authenticator();
        ret.setAaguid(credential.aaguid);
        AttestationCertificates attestationCertificates = new AttestationCertificates();
        attestationCertificates.setAlg(credential.alg);
        ret.setAttestationCertificates(attestationCertificates);
        ret.setCounter(credential.counter);
        ret.setCredID(credential.credID);
        ret.setFmt(credential.fmt);
        ret.setPublicKey(credential.publicKey);
        ret.setType(credential.type);
        ret.setUserName(credential.userName);
        return ret;
    }
    
    @Override
    public Set<String> getRoles(String userId) {
        if(userId.equals("admin")) {
            return Set.of("user", "admin");
        }
        return Collections.singleton("user");
    }
}
