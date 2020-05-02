package org.acme.tokenmanager.controllers.dtos;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;
import java.net.URI;

public class TokenInfo {

    private String token;

    @JsonbTransient
    private URI uri;

    public TokenInfo() {
    }

    public TokenInfo(String token, URI uri) {
        this.token = token;
        this.uri = uri;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    @JsonbProperty(value = "uri")
    public String getUriPathString(){
        return uri.getPath();
    }
}
