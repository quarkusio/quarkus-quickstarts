package org.acme.tokenmanager.services;

import org.acme.tokenmanager.controllers.dtos.TokenInfo;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

import static java.lang.String.format;

@ApplicationScoped
public class TokenInfoGenerator {

    public TokenInfo generateTokenInfo(String token) {

        URI tokenUri = UriBuilder.fromPath(format("/tokens/%s", token)).build();
        TokenInfo tokenInfo = new TokenInfo(token, tokenUri);

        return tokenInfo;
    }
}
