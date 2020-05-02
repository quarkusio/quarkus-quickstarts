package org.acme.tokenmanager.services;

import org.acme.tokenmanager.controllers.dtos.Entry;
import org.infinispan.Cache;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CacheServiceUnitTest {

    public static final String TAX_CODE = "CBTPGA90B13B345H";

    @InjectMocks
    CacheService cacheService;
    @Mock
    Cache<String, Entry> cache;

    @Test
    void test_put_entry_in_cache() {

        //given
        Entry entry = new Entry(TAX_CODE);
        //when
        String token = cacheService.putInCache(entry);
        //then
        assertThat(token).isNotBlank();
        assertDoesNotThrow(() -> UUID.fromString(token));
    }
}