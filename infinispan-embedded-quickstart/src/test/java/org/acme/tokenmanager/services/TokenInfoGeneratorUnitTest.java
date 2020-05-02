package org.acme.tokenmanager.services;

import org.acme.tokenmanager.controllers.dtos.TokenInfo;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class TokenInfoGeneratorUnitTest {

    public static final String TOKEN= "1d14e284-7a60-4e8e-b0dd-1dd8ff82b8df";

    TokenInfoGenerator generator = new TokenInfoGenerator();

    @Test
    void test_generate_token_info() {

        TokenInfo tokenInfo = generator.generateTokenInfo(TOKEN);

        assertThat(tokenInfo).isNotNull();
        assertThat(tokenInfo.getUri()).isNotNull();

        assertDoesNotThrow(() -> tokenInfo.getUriPathString());
    }
}